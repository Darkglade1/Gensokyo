package Gensokyo.actions;

import Gensokyo.powers.act2.ThirdEyePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEye;

public class ThirdEyeAction extends AbstractGameAction {
    public ThirdEyeAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = false;

        AbstractPlayer p = AbstractDungeon.player;

        for (AbstractRelic r : p.relics) {
            if (r.relicId.equals(FrozenEye.ID)) {
                this.isDone = true;
                return;
            }
        }

        AbstractRelic r = new FrozenEye();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ThirdEyePower(p, r)));
        this.isDone = true;
        return;
    }
}


