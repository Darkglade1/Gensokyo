package Gensokyo.powers.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class HopeMask extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("HopeMask");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int originalAmount;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("MaskOfHope84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("MaskOfHope32.png"));

    public HopeMask(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.originalAmount = amount;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
            this.flash();
            this.reducePower(damageAmount);
            this.updateDescription();
            AbstractDungeon.onModifyPower();
        }
        return damageAmount;
    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount - reduceAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount = 1; //Minimum amount is 1
        } else {
            this.fontScale = 8.0F;
            this.amount -= reduceAmount;
        }
    }

    @Override
    public void atEndOfRound() {
        this.amount = originalAmount;
        this.updateDescription();
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        if (!this.owner.halfDead && !this.owner.isDying && !this.owner.isDead) {
            AbstractDungeon.actionManager.addToBottom(new HealAction(this.owner, this.owner, this.amount));
        }

    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
