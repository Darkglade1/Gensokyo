package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class FoxMask extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("FoxMask");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FoxMask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FoxMask.png"));

    private static final int CARD_DRAW = 1;

    public FoxMask() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.STATUS) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, CARD_DRAW));
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + CARD_DRAW + DESCRIPTIONS[1];
    }

}
