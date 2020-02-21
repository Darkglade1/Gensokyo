package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class WishfulSoul extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("WishfulSoul");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCreature source;
    private boolean justApplied = true;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Evasive84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Evasive32.png"));

    public WishfulSoul(AbstractCreature owner, AbstractCreature source, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (justApplied) {
            justApplied = false;
            return;
        }
        if (this.owner.hasPower(StrengthPower.POWER_ID)) {
            int amount = this.owner.getPower(StrengthPower.POWER_ID).amount;
            if (amount > 0) {
                int drain = Math.min(amount, this.amount);
                this.owner.getPower(StrengthPower.POWER_ID).amount -= drain; //Don't want this to count as a debuff
                if (this.owner.getPower(StrengthPower.POWER_ID).amount == 0) {
                    this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, StrengthPower.POWER_ID));
                }
                this.addToBot(new ApplyPowerAction(this.source, this.source, new StrengthPower(this.source, drain), drain));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                AbstractDungeon.onModifyPower();
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
