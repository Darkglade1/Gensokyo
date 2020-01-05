package Gensokyo.patches;

import Gensokyo.cards.MarisaTwilightSpark;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import javassist.CtBehavior;


// A patch to fix shit for Twilight Spark
public class TwilightSparkHackyPatch {

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class FixCardType {
        @SpireInsertPatch(locator = FixCardType.Locator.class, localvars = {"targetCard"})
        public static void HackyCardTypeSwap(UseCardAction instance, @ByRef AbstractCard[] targetCard) {
            if (targetCard[0] instanceof MarisaTwilightSpark) {
                targetCard[0].type = AbstractCard.CardType.ATTACK;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce");
                return LineFinder.findInOrder(ctMethodToPatch, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(
            clz = DoubleTapPower.class,
            method = "onUseCard"
    )
    public static class FixDoubleTap {
        @SpireInsertPatch(locator = FixDoubleTap.Locator.class, localvars = {"tmp"})
        public static void MakeFreeToPlay(DoubleTapPower instance, AbstractCard card, UseCardAction action, @ByRef AbstractCard[] tmp) {
            if (tmp[0] instanceof MarisaTwilightSpark) {
                tmp[0].freeToPlayOnce = true;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "target_y");
                return LineFinder.findInOrder(ctMethodToPatch, fieldAccessMatcher);
            }
        }
    }


}