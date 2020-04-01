package Gensokyo.actions;

import Gensokyo.monsters.act1.Cirno;
import Gensokyo.monsters.act1.GreaterFairy;
import Gensokyo.monsters.act1.SunflowerFairy;
import Gensokyo.monsters.act1.ZombieFairy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LookForLeaderAction extends AbstractGameAction {
    AbstractMonster mo;

    public LookForLeaderAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;
        Cirno leader = null;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m instanceof Cirno) {
                leader = (Cirno)m;
            }
        }
        if (mo instanceof GreaterFairy) {
            ((GreaterFairy)mo).leader = leader;
        }
        if (mo instanceof SunflowerFairy) {
            ((SunflowerFairy)mo).leader = leader;
        }
        if (mo instanceof ZombieFairy) {
            ((ZombieFairy)mo).leader = leader;
        }

        this.isDone = true;
    }
}


