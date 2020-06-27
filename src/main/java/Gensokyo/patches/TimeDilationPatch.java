package Gensokyo.patches;

import Gensokyo.powers.act3.TimeDilation;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

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
                    if (power.isActive) {
                        c.dontTriggerOnUseCard = true;
                        power.increment(c, m);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}