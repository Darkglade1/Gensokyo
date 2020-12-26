package Gensokyo.patches;

import Gensokyo.powers.act3.SomeoneElse;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.DeadBranch;

public class DisableDeadBranchPatch {
    @SpirePatch(
            clz= DeadBranch.class,
            method="onExhaust"
    )
    //disable deadbranch for the kaguya fight
    public static class DisablePatch {
        public static SpireReturn Prefix(DeadBranch __instance, AbstractCard card) {
            if (AbstractDungeon.player.hasPower(SomeoneElse.POWER_ID)) {
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}