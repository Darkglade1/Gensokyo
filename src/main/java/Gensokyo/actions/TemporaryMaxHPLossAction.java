package Gensokyo.actions;

import Gensokyo.powers.act3.BorderOfDeath;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
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
        int playerTempHP = TempHPField.tempHp.get(AbstractDungeon.player);
        if (playerTempHP >= amount) {
            System.out.println("Losing temp hp: " + amount);
            TempHPField.tempHp.set(AbstractDungeon.player, playerTempHP - amount);
            isDone = true;
        } else {
            int maxHPAmountToLose = amount - playerTempHP;
            System.out.println("Current temp HP: " + playerTempHP);
            System.out.println("Losing this much max hp: " + maxHPAmountToLose);
            int loseableMaxHP = AbstractDungeon.player.maxHealth - 1;
            int maxHPLoss = Math.min(loseableMaxHP, maxHPAmountToLose);
            if (playerTempHP > 0) {
                TempHPField.tempHp.set(AbstractDungeon.player, 0);
            }
            if (maxHPLoss > 0) {
                AbstractDungeon.player.decreaseMaxHealth(maxHPLoss);
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BorderOfDeath(AbstractDungeon.player, maxHPLoss), maxHPLoss));
            }
            isDone = true;
        }
    }
}


