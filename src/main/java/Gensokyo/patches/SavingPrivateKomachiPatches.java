package Gensokyo.patches;

import Gensokyo.monsters.Komachi;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.city.BronzeAutomaton;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

//Stop Komachi from getting yeeted by enemies that indiscriminately kill everything in the room
public class SavingPrivateKomachiPatches {

    @SpirePatch(clz = Darkling.class, method = "damage")
    public static class DarklingsPlease {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("die")) {
                        m.replace("{" +
                                "if(!(m instanceof " + Komachi.class.getName() + ")) {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = BronzeAutomaton.class, method = "die")
    public static class AutomatonPlease {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("addToTop")) {
                        m.replace("{" +
                                "if(!(m instanceof " + Komachi.class.getName() + ")) {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = TheCollector.class, method = "die")
    public static class CollectorPlease {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("addToTop")) {
                        m.replace("{" +
                                "if(!(m instanceof " + Komachi.class.getName() + ")) {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

}