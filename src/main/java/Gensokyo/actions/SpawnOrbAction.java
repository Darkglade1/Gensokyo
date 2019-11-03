package Gensokyo.actions;

import Gensokyo.monsters.Reimu;
import Gensokyo.monsters.YinYangOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;

public class SpawnOrbAction extends AbstractGameAction {
    Reimu reimu;
    int column;

    public SpawnOrbAction(Reimu reimu, int column) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.reimu = reimu;
        this.column = column;
    }

    public void update() {
        this.isDone = false;

        if (reimu.isDying || reimu.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }
        int i = column - 1;
        ArrayList<Integer> emptySpots = new ArrayList<>();
        for (int j = 0; j < reimu.orbs[i].length; j++) {
            if (reimu.orbs[i][j].isEmpty()) {
                emptySpots.add(j);
            }
        }
        Collections.shuffle(emptySpots, AbstractDungeon.monsterRng.random);
        while (emptySpots.size() > 1) {
            int position = emptySpots.remove(0) + 1;
            int delay = i + 1;
            int type = AbstractDungeon.monsterRng.random(1, 3);
            float x = -reimu.orbOffset * (4 - delay);
            float y = reimu.orbOffset * (position - 1);
            AbstractMonster orb = new YinYangOrb(x, y, type, position, delay, reimu);
            reimu.orbs[delay - 1][position - 1].add(orb);
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(orb, true));
        }

        this.isDone = true;
    }
}


