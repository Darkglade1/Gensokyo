package Gensokyo.patches.relics;

import Gensokyo.relics.CelestialsFlawlessClothing;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
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
public class CelestialsFlawlessClothingPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<Void> trigger(AbstractPlayer instance, DamageInfo info) {
        if (instance.hasRelic(CelestialsFlawlessClothing.ID) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (instance.currentHealth < 1) { //double checks in case another rez item triggered
                CelestialsFlawlessClothing relic = (CelestialsFlawlessClothing)instance.getRelic(CelestialsFlawlessClothing.ID);
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.contains(relic.elite)) {
                    relic.onTrigger();
                    return SpireReturn.Return(null);
                } else {
                    if (relic.elite.isDeadOrEscaped() || relic.elite.isDying) {
                        relic.onTrigger();
                        return SpireReturn.Return(null);
                    }
                }
            }
        }
        return SpireReturn.Continue();
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDead");
            return LineFinder.findInOrder(ctMethodToPatch, fieldAccessMatcher);
        }
    }
}
