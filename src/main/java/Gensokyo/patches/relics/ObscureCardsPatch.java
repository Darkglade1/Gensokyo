package Gensokyo.patches.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act2.UndefinedDarkness;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ObscureCardsPatch {

    private static String obscuredText = CardCrawlGame.languagePack.getRelicStrings(UndefinedDarkness.ID).DESCRIPTIONS[2];

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderTitle"
    )
    public static class ObscureTitle {
        public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (UndefinedDarkness.obscuredCards.contains(__instance)) {
                __instance.name = obscuredText;
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderDescription"
    )
    public static class ObscureDescription {
        public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (UndefinedDarkness.obscuredCards.contains(__instance)) {
                __instance.rawDescription = obscuredText;
                __instance.initializeDescription();
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderPortrait"
    )
    public static class ObscurePortrait {
        static Texture attackImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOAttack.png"));
        static TextureAtlas.AtlasRegion attackImgAtlas = new TextureAtlas.AtlasRegion(attackImg, 0, 0, attackImg.getWidth(), attackImg.getHeight());

        static Texture skillImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOSkill.png"));
        static TextureAtlas.AtlasRegion skillImgAtlas = new TextureAtlas.AtlasRegion(skillImg, 0, 0, skillImg.getWidth(), skillImg.getHeight());

        static Texture powerImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOPower.png"));
        static TextureAtlas.AtlasRegion powerImgAtlas = new TextureAtlas.AtlasRegion(powerImg, 0, 0, powerImg.getWidth(), powerImg.getHeight());

        public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (UndefinedDarkness.obscuredCards.contains(__instance)) {
                if (__instance.type == AbstractCard.CardType.ATTACK) {
                    __instance.portrait = attackImgAtlas;
                }
                if (__instance.type == AbstractCard.CardType.SKILL) {
                    __instance.portrait = skillImgAtlas;
                }
                if (__instance.type == AbstractCard.CardType.POWER) {
                    __instance.portrait = powerImgAtlas;
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCardTip"
    )
    public static class ObscureTips {
        public static SpireReturn Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (UndefinedDarkness.obscuredCards.contains(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

}
