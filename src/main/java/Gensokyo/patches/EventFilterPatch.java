package Gensokyo.patches;

import Gensokyo.events.ClashOfLegends;
import Gensokyo.events.DemonBookSeller;
import Gensokyo.events.FieldTripToAnotherWorld;
import Gensokyo.events.GardenOfTheSun;
import Gensokyo.events.GoodsFromTheOutsideWorld;
import Gensokyo.events.HakureiShrine;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.HappyFlower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getEvent",
        paramtypez = Random.class

)
// A patch to make certain events only appear if the player fulfills some condition
public class EventFilterPatch {
    @SpireInsertPatch(locator = EventFilterPatch.Locator.class, localvars = {"tmp"})
    public static void FilterEvent(Random rng, @ByRef ArrayList<String>[] tmp) {
        ArrayList<String> eventsToRemove = new ArrayList<>();
        for (String event : tmp[0]) {
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
        for (String event : eventsToRemove) {
            tmp[0].remove(event);
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Random.class, "random");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}