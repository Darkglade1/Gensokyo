package Gensokyo.patches;

import Gensokyo.powers.act3.TimeDilation;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class TimeDilationPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class DelayUseCard {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace("{" +
                                "if(!(" + TimeDilationPatch.class.getName() + ".DelayCardPlay(c, monster))) {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

    public static boolean DelayCardPlay(AbstractCard c, AbstractMonster m) {
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.hasPower(TimeDilation.POWER_ID)) {
                    TimeDilation power = (TimeDilation)monster.getPower(TimeDilation.POWER_ID);
                    if (!ContainsCard(power.playingCards, c)) {
                        c.dontTriggerOnUseCard = true;
                        power.increment(c, m);
                        return true;
                    } else {
                        TimeDilation.CardInfo info = GetCard(power.playingCards, c);
                        if (info != null && c.cost == -1) {
                            c.energyOnUse = info.energyOnUse; //Make X costs work properly
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static boolean ContainsCard(ArrayList<TimeDilation.CardInfo> arr, AbstractCard card) {
        for (TimeDilation.CardInfo info : arr) {
            if (info.card == card) {
                return true;
            }
        }
        return false;
    }

    public static TimeDilation.CardInfo GetCard(ArrayList<TimeDilation.CardInfo> arr, AbstractCard card) {
        for (TimeDilation.CardInfo info : arr) {
            if (info.card == card) {
                return info;
            }
        }
        return null;
    }


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"

    )
    public static class TriggerAtStartOfPlayerTurn {
        @SpireInsertPatch(locator = TriggerAtStartOfPlayerTurn.Locator.class)
        public static void Trigger(GameActionManager instance) {
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
                for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (monster.hasPower(TimeDilation.POWER_ID)) {
                        TimeDilation power = (TimeDilation)monster.getPower(TimeDilation.POWER_ID);
                        power.playCards();
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "gameHandSize");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}