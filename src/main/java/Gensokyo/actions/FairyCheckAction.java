package Gensokyo.actions;

import Gensokyo.monsters.act1.NormalEnemies.AbstractFairy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FairyCheckAction extends AbstractGameAction {
    AbstractFairy mo;

    public FairyCheckAction(AbstractFairy mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        if (mo.isLastFairy()) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }

        this.isDone = true;
    }
}


