package Gensokyo.actions;

import Gensokyo.powers.act3.BorderOfDeath;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
        AbstractDungeon.player.decreaseMaxHealth(loseableMaxHP);
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BorderOfDeath(AbstractDungeon.player, loseableMaxHP), loseableMaxHP));
        isDone = true;
    }
}


