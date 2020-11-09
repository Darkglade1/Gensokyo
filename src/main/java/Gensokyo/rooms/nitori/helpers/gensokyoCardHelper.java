package Gensokyo.rooms.nitori.helpers;

import Gensokyo.cards.AbstractUrbanLegendCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class gensokyoCardHelper {
    public static ArrayList<AbstractUrbanLegendCard> legendCards = new ArrayList<>();
    public static ArrayList<AbstractCard> getNitoriShopCards() {
        return getNitoriShopCards(10);
    }
    public static ArrayList<AbstractCard> getNitoriShopCards(int amount) {
        ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
        int amountOfCardsToGive = amount;
        int attempts = 1000;
        do {
            boolean isUnique = true;
            AbstractCard card = getRandomLegendCard().makeStatEquivalentCopy();
            for(AbstractCard c : cards) { if(c.cardID.equals(card.cardID)) { isUnique = false; } }
            if(isUnique) {
                cards.add(card);
                amountOfCardsToGive--;
            }
            attempts--;
        } while(amountOfCardsToGive > 0 && attempts >= 0);
        return cards;
    }
    public static AbstractUrbanLegendCard getRandomLegendCard() {
        AbstractUrbanLegendCard card =  legendCards.get(AbstractDungeon.cardRng.random(legendCards.size() -1));
        UnlockTracker.markCardAsSeen(card.cardID);
        card.isSeen = true;
        return (AbstractUrbanLegendCard) card.makeStatEquivalentCopy();
    }
    public static void addCard(AbstractCard card) {
        if(card instanceof AbstractUrbanLegendCard) { legendCards.add((AbstractUrbanLegendCard) card); }
    }
    public static AbstractUrbanLegendCard[] getlegendCards() {
        return legendCards.toArray(new AbstractUrbanLegendCard[0]);
    }
    public static boolean legendCardsContains(AbstractUrbanLegendCard card){ return legendCards.contains(card); }
    public static ArrayList<AbstractUrbanLegendCard> getUrbanLegendCards(){ return legendCards;}
}
