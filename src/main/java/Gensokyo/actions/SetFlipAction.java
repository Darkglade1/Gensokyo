package Gensokyo.actions;

import Gensokyo.monsters.NormalEnemies.AbstractFairy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class SetFlipAction extends AbstractGameAction {
    AbstractFairy mo;

    public SetFlipAction(AbstractFairy mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        mo.setFlip(true, false);;

        this.isDone = true;
    }
}


