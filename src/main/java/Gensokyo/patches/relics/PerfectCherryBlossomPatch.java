package Gensokyo.patches.relics;

import Gensokyo.relics.PerfectCherryBlossom;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage",
        paramtypez = DamageInfo.class
)

// A patch that increases the drop rate of Arcana cards
public class PerfectCherryBlossomPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void trigger(AbstractPlayer instance, DamageInfo info) {
        if (instance.hasRelic(PerfectCherryBlossom.ID) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            instance.getRelic(PerfectCherryBlossom.ID).onTrigger();
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
        }
    }
}
