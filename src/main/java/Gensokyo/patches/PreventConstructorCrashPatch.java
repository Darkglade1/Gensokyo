package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez={
                String.class,
                String.class,
                AbstractPlayer.class,
                ArrayList.class
        }
)
public class PreventConstructorCrashPatch {
    public static SpireReturn Prefix(AbstractDungeon __instance, String a, String levelId, AbstractPlayer p, ArrayList c) {
        //The constructor for AbstractDungeon requires an AbstractPlayer to be present or it crashes.
        if(p == null) {
            return SpireReturn.Return(null);
        } else {
            if(__instance instanceof Gensokyo) {
                //Important for monster generation.
                //((Gensokyo)__instance).ID = Gensokyo.datasource.id;
                ((Gensokyo)__instance).weakpreset = Gensokyo.datasource.weakpreset;
                ((Gensokyo)__instance).strongpreset = Gensokyo.datasource.strongpreset;
                ((Gensokyo)__instance).elitepreset = Gensokyo.datasource.elitepreset;
            }
            return SpireReturn.Continue();
        }
    }
}