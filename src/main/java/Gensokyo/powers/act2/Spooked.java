package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class Spooked extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("Spooked");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int threshold;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Spooked84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Spooked32.png"));

    public Spooked(AbstractCreature owner, int threshold, int powerAmt) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = powerAmt;
        this.threshold = threshold;
        this.amount2 = threshold;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            this.flash();
            if (damageAmount > this.owner.currentHealth) {
                damageAmount = this.owner.currentHealth; //prevents overkill damage from contributing
            }
            this.reducePower(damageAmount);
            this.updateDescription();
            AbstractDungeon.onModifyPower();
        }
        return damageAmount;
    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount2 - reduceAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount2 = threshold;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, amount)));
        } else {
            this.fontScale = 8.0F;
            this.amount2 -= reduceAmount;
        }
    }

    @Override
    public void atEndOfRound() {
        this.amount2 = threshold;
        this.updateDescription();
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
