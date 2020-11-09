package Gensokyo.powers.extra;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.PlayIdCardAction;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class DeEnergized extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("DeEnergized");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DeEnergized84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DeEnergized32.png"));

    private static final int LOSE_ENERGY_AMOUNT = 1;
    public DeEnergized(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        type = PowerType.DEBUFF;
        isTurnBased = false;
        this.canGoNegative = false;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new LoseEnergyAction(LOSE_ENERGY_AMOUNT));
        addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        updateDescription();
    }

    @Override
    public void updateDescription() { description = this.amount  > 1 ? String.format(DESCRIPTIONS[1], this.amount, LOSE_ENERGY_AMOUNT) : String.format(DESCRIPTIONS[0], LOSE_ENERGY_AMOUNT); }
}
