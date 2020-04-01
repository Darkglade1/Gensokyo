package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RezAction extends AbstractGameAction {
    AbstractMonster mo;

    public RezAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        AbstractDungeon.actionManager.addToTop(new HealAction(mo, mo, mo.maxHealth));
        mo.halfDead = false;

        this.isDone = true;
    }
}


