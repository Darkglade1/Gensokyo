package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class SongOfSouls extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("SongOfSouls");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Piano.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Piano.png"));

    public SongOfSouls() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE) || card.tags.contains(AbstractCard.CardTags.STARTER_DEFEND)) {
            this.flash();
            action.exhaustCard = true;
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
