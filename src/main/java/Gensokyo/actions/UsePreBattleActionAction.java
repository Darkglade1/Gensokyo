package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UsePreBattleActionAction extends AbstractGameAction {
    AbstractMonster mo;

    public UsePreBattleActionAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        mo.usePreBattleAction();

        this.isDone = true;
        return;
    }
}


