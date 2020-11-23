package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SetMaxHealthToCurrentAction extends AbstractGameAction {

    public SetMaxHealthToCurrentAction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (AbstractDungeon.player.maxHealth == 1) {
            isDone = true;
            return;
        }
        int loseableMaxHP = AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth;
        if (loseableMaxHP > 0) {
            AbstractDungeon.actionManager.addToTop(new TemporaryMaxHPLossAction(loseableMaxHP));
        }
        isDone = true;
    }
}


