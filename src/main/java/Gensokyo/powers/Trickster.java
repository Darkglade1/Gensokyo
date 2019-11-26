package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Trickster extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("Trickster");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Trickster84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Trickster32.png"));

    public Trickster(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        this.loadRegion("afterImage");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0) {
            this.flash();
            action.exhaustCard = true;
            this.amount = 0;
        }
    }

    @Override
    public void atStartOfTurn() {
        this.amount = 1;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
