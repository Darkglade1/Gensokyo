package Gensokyo.patches;

import Gensokyo.monsters.act3.Remilia;
import Gensokyo.monsters.act3.Shinki.Shinki;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateSingleTargetInput"

)
// A patch to make allies untargetable by the player
public class MakeAlliesUntargetable {
    @SpireInsertPatch(locator = MakeAlliesUntargetable.Locator.class, localvars = {"hoveredMonster"})
    public static void MakeHoveredMonsterNull(AbstractPlayer instance, @ByRef AbstractMonster[] hoveredMonster) {
        if (hoveredMonster[0] != null && (hoveredMonster[0].id.equals(Remilia.ID) || hoveredMonster[0].id.equals(Shinki.ID))) {
            hoveredMonster[0] = null;
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}