package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.ManorOfTheDishes;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class ManorOfTheDishesPower extends TwoAmountPower {

    public static final String POWER_ID = GensokyoMod.makeID("ManorOfTheDishesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int cardThreshold = ManorOfTheDishes.cardThreshold;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Manor84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Manor32.png"));

    public ManorOfTheDishesPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount2 = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        this.flash();
        this.amount2++;
        if (this.amount2 % cardThreshold == 0) {
            this.amount2 = this.amount2 - cardThreshold;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, this.amount), this.amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + cardThreshold + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
