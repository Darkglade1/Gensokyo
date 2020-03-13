package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayIdCardAction extends AbstractGameAction {
    private boolean exhaustCards;
    private AbstractCard card;

    public PlayIdCardAction(AbstractCard card, AbstractCreature target, boolean exhausts) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
        this.exhaustCards = exhausts;
        this.card = card;
    }

    public void update() {

        card.exhaustOnUseOnce = this.exhaustCards;
        card.applyPowers();
        this.addToTop(new NewQueueCardAction(card, this.target, false, false));

        this.isDone = true;

    }
}
