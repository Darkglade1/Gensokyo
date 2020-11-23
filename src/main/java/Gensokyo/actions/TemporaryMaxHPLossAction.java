package Gensokyo.actions;

import Gensokyo.powers.act3.BorderOfDeath;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TemporaryMaxHPLossAction extends AbstractGameAction {
    int amount;

    public TemporaryMaxHPLossAction(int amount) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
    }

    public void update() {
        if (AbstractDungeon.player.maxHealth == 1) {
            isDone = true;
            return;
        }
        int loseableMaxHP = AbstractDungeon.player.maxHealth - 1;
        int maxHPLoss = Math.min(loseableMaxHP, amount);
        AbstractDungeon.player.decreaseMaxHealth(maxHPLoss);
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BorderOfDeath(AbstractDungeon.player, maxHPLoss), maxHPLoss));
        isDone = true;
    }
}


