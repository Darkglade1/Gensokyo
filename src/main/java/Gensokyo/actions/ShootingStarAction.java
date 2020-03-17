package Gensokyo.actions;

import Gensokyo.cards.ShootingStar;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ShootingStarAction extends AbstractGameAction {
    public static final String[] TEXT;
    private CardGroup cards;

    public ShootingStarAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.cards = new CardGroup(AbstractDungeon.player.masterDeck, CardGroup.CardGroupType.UNSPECIFIED);
        ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
        for (AbstractCard card : cards.group) {
            if (card.cardID.equals(ShootingStar.ID)) {
                cardsToRemove.add(card);
            }
        }
        for (AbstractCard card : cardsToRemove) {
            this.cards.removeCard(card);
        }
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (!this.cards.isEmpty()) {
                AbstractDungeon.gridSelectScreen.open(cards, 1, TEXT[0], false);
                this.tickDuration();
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    c = c.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.player.hand.addToHand(c);
                    }
                    c.applyPowers();
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            this.tickDuration();
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;
    }
}
