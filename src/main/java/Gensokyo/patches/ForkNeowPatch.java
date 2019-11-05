package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.neow.NeowRoom;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch(
        clz = Exordium.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez={
                AbstractPlayer.class,
                ArrayList.class
        }
)
public class ForkNeowPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    //Sets a forkevent just before the Neow room is created
    public static SpireReturn<Void> Insert(Exordium __instance, AbstractPlayer p, ArrayList bleh) {
        ArrayList<String> availableActs = new ArrayList<>();
        if(Gensokyo.actnumbers.containsKey(Gensokyo.EXORDIUM)) {
            for(final String s : Gensokyo.actnumbers.get(Gensokyo.EXORDIUM)) {
                Gensokyo cd = Gensokyo.dungeons.get(s);
                if(!cd.finalAct) {
                    availableActs.add(s);
                }
            }
        }
        if(!availableActs.isEmpty()) {
            AbstractDungeon.actNum = Gensokyo.EXORDIUM - 1;
            AbstractDungeon.currMapNode.room = new GoToNextDungeonPatch.ForkEventRoom(null, false);
            AbstractDungeon.currMapNode.room.onPlayerEntry();
            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }


    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(NeowRoom.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}