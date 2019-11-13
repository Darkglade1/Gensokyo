package Gensokyo.actions;

import Gensokyo.monsters.Mamizou;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class DisguiseAction extends AbstractGameAction {
    Mamizou mo;

    public DisguiseAction(Mamizou mo) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
    }

    public void update() {
        this.isDone = false;

        mo.switchDisguise();

        this.isDone = true;
    }
}


