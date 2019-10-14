package Gensokyo.patches.relics;

import Gensokyo.relics.PortableGap;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.Iterator;

public class PortableGapPatch
{
    @SpirePatch(
            clz= MapRoomNode.class,
            method="isConnectedTo"
    )
    public static class IsConnectedTo
    {

        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node)
        {
            Iterator var2 = __instance.getEdges().iterator();
            MapEdge edge;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                edge = (MapEdge)var2.next();
                if (AbstractDungeon.player.hasRelic(PortableGap.ID) && node.y == edge.dstY) {
                    return true;
                }
            } while(node.y != edge.dstY || !AbstractDungeon.player.hasRelic(PortableGap.ID));

            return __result;
        }
    }
}