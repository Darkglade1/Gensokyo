package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetDeadAction extends AbstractGameAction {
    AbstractMonster mo;

    public SetDeadAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        mo.isDying = true;
        mo.isDead = true;
        mo.halfDead = false;

        this.isDone = true;
    }
}


