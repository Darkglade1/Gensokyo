package Gensokyo.actions;

import Gensokyo.monsters.act2.Tenshi;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class TakeSecondTurnAction extends AbstractGameAction {
    Tenshi mo;

    public TakeSecondTurnAction(Tenshi mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        mo.takeSecondTurn();

        this.isDone = true;
    }
}


