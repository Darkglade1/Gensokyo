package Gensokyo.patches;

import Gensokyo.dungeon.CustomDungeon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@SpirePatch(cls = "infinitespire.patches.AbstractDungeonPatch$InitFirstRoomPatch", method = "initQuestLog", optional = true)
public class PatchingaPatchInInfiniteBecauseRAAAAAAGH {
    public static SpireReturn<Void> Prefix(AbstractDungeon __instance,
                                              String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
        if(__instance instanceof CustomDungeon && p == null) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}