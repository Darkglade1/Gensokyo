package Gensokyo.actions;

import Gensokyo.monsters.act2.Reisen;
import Gensokyo.powers.act2.LunaticRedEyes;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MarkHandAction extends AbstractGameAction {
    Reisen reisen;

    public MarkHandAction(Reisen reisen) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.reisen = reisen;
    }

    public void update() {
        this.isDone = false;

        if (reisen.hasPower(LunaticRedEyes.POWER_ID)) {
            LunaticRedEyes eyes = (LunaticRedEyes)reisen.getPower(LunaticRedEyes.POWER_ID);
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                eyes.markedCards.add(card);
                card.isEthereal = true;
                card.selfRetain = false;
                card.retain = false;
                card.flash();
            }
        }

        this.isDone = true;
    }
}


