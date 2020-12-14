package Gensokyo.powers.act3;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EntanglePower;

public class BetterEntangle extends EntanglePower {

    public BetterEntangle(AbstractCreature owner, int amount) {
        super(owner);
        this.amount = amount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }

    }
}
