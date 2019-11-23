package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetDeadAction extends AbstractGameAction {
    AbstractMonster mo;

    public SetDeadAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.5F;
        this.mo = mo;
    }

    public void update() {
        this.tickDuration();
        if (this.duration <= 0.0F) {
            mo.isDying = true;
            mo.isDead = true;
        }
    }
}


