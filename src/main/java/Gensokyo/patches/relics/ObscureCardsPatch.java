package Gensokyo.patches.relics;

import Gensokyo.CardMods.ObscureMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ObscureCardsPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCardTip"
    )
    public static class ObscureTips {
        public static SpireReturn Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (CardModifierManager.hasModifier(__instance, ObscureMod.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

}
