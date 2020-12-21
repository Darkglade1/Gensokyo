package Gensokyo.powers.act3;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.CurlUpPower;

public class BetterCurlUp extends CurlUpPower {

    private boolean triggered = false;

    public BetterCurlUp(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!this.triggered && damageAmount < this.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.triggered = true;
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        return damageAmount;
    }
}
