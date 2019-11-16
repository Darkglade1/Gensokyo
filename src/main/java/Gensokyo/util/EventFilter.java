package Gensokyo.util;

import Gensokyo.events.ClashOfLegends;
import Gensokyo.events.DemonBookSeller;
import Gensokyo.events.FieldTripToAnotherWorld;
import Gensokyo.events.GardenOfTheSun;
import Gensokyo.events.GoodsFromTheOutsideWorld;
import Gensokyo.events.HakureiShrine;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.HappyFlower;

import java.util.ArrayList;

public class EventFilter {
    public static ArrayList<String> FilterEvents(ArrayList<String> events) {
        ArrayList<String> eventsToRemove = new ArrayList<>();
        for (String event : events) {
            if (event.equals(DemonBookSeller.ID)) {
                if (!(AbstractDungeon.player.gold >= DemonBookSeller.COST)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(GoodsFromTheOutsideWorld.ID)) {
                if (!(AbstractDungeon.player.gold >= GoodsFromTheOutsideWorld.commonCost)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(HakureiShrine.ID)) {
                if (!(AbstractDungeon.player.gold >= HakureiShrine.upgradeCost || (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth && AbstractDungeon.player.gold >= 2))) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(ClashOfLegends.ID)) {
                if (!(AbstractDungeon.player.maxHealth > 1)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(GardenOfTheSun.ID)) {
                if (AbstractDungeon.player.hasRelic(HappyFlower.ID) || !GardenOfTheSun.hasNonStrikeAttack()) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(FieldTripToAnotherWorld.ID)) {
                if (!FieldTripToAnotherWorld.hasNonBasicCard()) {
                    eventsToRemove.add(event);
                }
            }
        }
        return eventsToRemove;
    }
}
