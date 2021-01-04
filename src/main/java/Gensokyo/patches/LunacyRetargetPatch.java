package Gensokyo.patches;

import Gensokyo.monsters.act3.Flandre;
import Gensokyo.powers.act1.LunacyPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;


// A patch to make these enemies properly target someone else
public class LunacyRetargetPatch {
    @SpirePatch(
            clz = DamageAction.class,
            method = "update"

    )
    public static class DamageActionRetarget {
        @SpireInsertPatch(locator = DamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(DamageAction instance, @ByRef DamageInfo[] info) {
            if (instance.source != null) {
                if (instance.source.hasPower(LunacyPower.POWER_ID) && info[0].type == DamageInfo.DamageType.NORMAL) {
                    if (instance.source instanceof Flandre) {
                        instance.target = instance.source; //hardcode to make Flandre always hit herself LOL
                    } else {
                        instance.target = LunacyPower.randomTarget;
                        if (instance.target == null) {
                            LunacyPower.randomTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                            instance.target = LunacyPower.randomTarget;
                        }
                    }
                    if (instance.target != null) {
                        info[0].applyPowers(instance.source, instance.target);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = VampireDamageAction.class,
            method = "update"

    )
    public static class VampireDamageActionRetarget {
        @SpireInsertPatch(locator = VampireDamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(VampireDamageAction instance, @ByRef DamageInfo[] info) {
            if (instance.source != null) {
                if (instance.source.hasPower(LunacyPower.POWER_ID) && info[0].type == DamageInfo.DamageType.NORMAL) {
                    if (instance.source instanceof Flandre) {
                        instance.target = instance.source; //hardcode to make Flandre always hit herself LOL
                    } else {
                        instance.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    }
                    if (instance.target != null) {
                        info[0].applyPowers(instance.source, instance.target);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}