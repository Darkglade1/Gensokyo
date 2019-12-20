//package Gensokyo.patches;
//
//import Gensokyo.powers.DisguisePower;
//import com.evacipated.cardcrawl.modthespire.lib.ByRef;
//import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
//import com.evacipated.cardcrawl.modthespire.lib.Matcher;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import javassist.CtBehavior;
//
//@SpirePatch(
//        clz = AbstractMonster.class,
//        method = "damage",
//        paramtypez={
//                DamageInfo.class,
//        }
//
//)
//// A patch to make to check if a monster loses HP for the Disguise Power
//public class onLoseHPPatch {
//    @SpireInsertPatch(locator = onLoseHPPatch.Locator.class, localvars = {"damageAmount"})
//    public static void triggerOnLoseHP(AbstractMonster instance, DamageInfo info, @ByRef int[] damageAmount) {
//        if (damageAmount[0] > 0) {
//            if (instance.hasPower(DisguisePower.POWER_ID)) {
//                instance.getPower(DisguisePower.POWER_ID).onLoseHp(damageAmount[0]);
//            }
//        }
//    }
//    private static class Locator extends SpireInsertLocator {
//        @Override
//        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
//            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "currentHealth");
//            return LineFinder.findInOrder(ctMethodToPatch, fieldAccessMatcher);
//        }
//    }
//}