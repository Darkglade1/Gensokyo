package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class VigorPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("VigorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final float DAMAGE_BOOST = 1.33F;

    public VigorPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        this.loadRegion("envenom");

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, ID, 1));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * DAMAGE_BOOST;
        } else {
            return damage;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
