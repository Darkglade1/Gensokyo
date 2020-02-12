package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MonkeysPawGainGoldAction extends AbstractGameAction {
    int initialHP;
    int goldRatio;

    public MonkeysPawGainGoldAction(int initialHP, int goldRatio) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.initialHP = initialHP;
        this.goldRatio = goldRatio;
    }

    public void update() {
        this.isDone = false;

        int afterwardHP = AbstractDungeon.player.currentHealth;
        int difference = initialHP - afterwardHP;
        if (difference > 0) {
            int goldGain = goldRatio * difference;
            AbstractDungeon.player.gainGold(goldGain);
        }

        this.isDone = true;
    }
}


