package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ExhaustRandomCurseInHandAction extends AbstractGameAction {
    AbstractRelic relic;

    public ExhaustRandomCurseInHandAction(AbstractRelic relic) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.relic = relic;
    }

    public void update() {
        this.isDone = false;

        ArrayList<AbstractCard> curses = new ArrayList<>();
        ArrayList<AbstractCard> etherealCurses = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE) {
                if (!card.isEthereal) {
                    curses.add(card);
                } else {
                    etherealCurses.add(card);
                }
            }
        }

        //Prioritize non-Ethereal curses since they exhaust anyway
        if (curses.size() > 0) {
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            AbstractDungeon.player.hand.moveToExhaustPile(curses.get(AbstractDungeon.cardRandomRng.random(curses.size() - 1)));
        } else if (etherealCurses.size() > 0) {
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            AbstractDungeon.player.hand.moveToExhaustPile(etherealCurses.get(AbstractDungeon.cardRandomRng.random(etherealCurses.size() - 1)));
        }

        this.isDone = true;
        return;
    }
}


