package Gensokyo.actions;

import Gensokyo.monsters.NormalEnemies.AbstractFairy;
import Gensokyo.monsters.bossRush.Eiki;
import Gensokyo.monsters.Kaguya;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetFlipAction extends AbstractGameAction {
    AbstractMonster mo;

    public SetFlipAction(AbstractMonster mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        if (mo instanceof AbstractFairy) {
            ((AbstractFairy)mo).setFlip(true, false);;
        }
        if (mo instanceof Eiki) {
            ((Eiki)mo).setFlip(true, false);;
        }
        if (mo instanceof Kaguya) {
            ((Kaguya)mo).setFlip(true, false);;
        }

        this.isDone = true;
    }
}


