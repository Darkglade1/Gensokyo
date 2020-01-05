package Gensokyo.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ExhaustCardHook {
    public static Consumer<ArrayList<AbstractCard>> callback;

    @SpirePatch(clz = ExhaustAction.class, method = "update")
    public static class GetExhaustedCards {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(ExhaustAction __instance) {
            if (callback != null) {
                callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "iterator");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}