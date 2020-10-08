package Gensokyo.rooms.nitori.helpers;

import Gensokyo.cards.AbstractUrbanLegendCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class gensokyoRelicHelper {
    public static ArrayList<AbstractRelic> gensokyoRelicList = new ArrayList<>();
    public static ArrayList<AbstractRelic> getRandomRelics() {
        return getRandomRelics(10);
    }
    public static ArrayList<AbstractRelic> getRandomRelics(int amount) {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        int relicsToGive = amount;
        int attempts = 1000;
        do {
            boolean isUnique = true;
            AbstractRelic relic = getRandomRelic();
            for(AbstractRelic r : relics) {
                if(r.relicId.equals(relic.relicId)) { isUnique = false; } }
            if(isUnique) {
                relics.add(relic);
                relicsToGive--;
            }
            attempts--;
        } while(relicsToGive > 0 && attempts >= 0);
        return relics;
    }
    public static AbstractRelic getRandomRelic() {
        AbstractRelic relic =  gensokyoRelicList.get(AbstractDungeon.merchantRng.random(gensokyoRelicList.size() -1));
        UnlockTracker.markRelicAsSeen(relic.relicId);
        relic.isSeen = true;
        return relic;
    }
    public static void addRelic(AbstractRelic relic) {
        gensokyoRelicList.add(relic);
    }
    public static AbstractRelic[] getGensokyoRelicListArray() {
        return gensokyoRelicList.toArray(new AbstractRelic[0]);
    }
    public static boolean gensokyoRelicListContains(AbstractRelic relic){ return gensokyoRelicList.contains(relic); }
    public static ArrayList<AbstractRelic> getGensokyoRelicList(){ return gensokyoRelicList;}
}