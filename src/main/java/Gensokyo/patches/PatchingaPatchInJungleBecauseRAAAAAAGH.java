package Gensokyo.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;

public class PatchingaPatchInJungleBecauseRAAAAAAGH
{
    @SpirePatch(
            cls="theAct.patches.GoToNextDungeonPatch",
            method="Insert",
            paramtypez={
                    ProceedButton.class,
                    AbstractRoom.class
            },
            optional = true
    )
    public static class GoToNextDungeonPatch
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(Instanceof f) throws CannotCompileException
                {
                    f.replace("if (" + AbstractDungeon.class.getName() + ".actNum == 1) {" +
                            "$_ = true;" +
                            "} else {" +
                            "$_ = $proceed($$);" +
                            "}");

                }
            };
        }
    }
}