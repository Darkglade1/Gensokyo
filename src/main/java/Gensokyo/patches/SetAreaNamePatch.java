package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import javassist.CtBehavior;

@SpirePatch(
        clz = DungeonTransitionScreen.class,
        method = "setAreaName"
)
public class SetAreaNamePatch {
    //Makes the right text and actnumber display at the start of a new act.
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(DungeonTransitionScreen __instance, String key) {
        if (Gensokyo.dungeons.containsKey(key)) {
            Gensokyo cd = Gensokyo.dungeons.get(key);
            __instance.levelName = cd.NAME;
            __instance.levelNum = DungeonTransitionScreen.TEXT[2].replace("1", Integer.toString(AbstractDungeon.actNum + 1));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "name");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}