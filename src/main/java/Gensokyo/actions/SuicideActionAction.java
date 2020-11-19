package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//This action exists because FRIENDLY MINIONS GUY added to bottom instead of adding to top
public class SuicideActionAction extends AbstractGameAction {
    AbstractMonster mo;

    public SuicideActionAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
        this.isDone = true;
    }
}


