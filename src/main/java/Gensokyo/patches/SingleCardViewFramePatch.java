package Gensokyo.patches;

import Gensokyo.cards.AbstractUrbanLegendCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "renderFrame"
)
// A patch to make the singlecardview load the custom frames for urban legends
public class SingleCardViewFramePatch {
    @SpireInsertPatch(locator = SingleCardViewFramePatch.Locator.class, localvars = {"tmpImg", "card"})
    public static void FixFrames(SingleCardViewPopup _instance, SpriteBatch sb,  @ByRef TextureAtlas.AtlasRegion tmpImg[], AbstractCard card) {
        if (card instanceof AbstractUrbanLegendCard) {
            switch (card.type) {
                case ATTACK:
                    tmpImg[0] = AbstractUrbanLegendCard.frames[3];
                    break;
                case SKILL:
                    tmpImg[0] = AbstractUrbanLegendCard.frames[4];
                    break;
                case POWER:
                    tmpImg[0] = AbstractUrbanLegendCard.frames[5];
                    break;
            }
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderHelper");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}