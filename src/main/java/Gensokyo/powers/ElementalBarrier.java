package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.marisaMonsters.Patchouli;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class ElementalBarrier extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("ElementalBarrier");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int maxAmt;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Evasive84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Evasive32.png"));

    public ElementalBarrier(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.maxAmt = amount;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.priority = 99;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > this.amount) {
            damageAmount = this.amount;
        }

        this.amount -= damageAmount;
        if (this.amount < 0) {
            this.amount = 0;
        }

        this.updateDescription();
        return damageAmount;
    }

    public void atStartOfTurn() {
        this.amount = this.maxAmt;
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.maxAmt += stackAmount;
        if (this.maxAmt == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + Patchouli.INVINCIBLE_INCREMENT + DESCRIPTIONS[2];
    }
}
