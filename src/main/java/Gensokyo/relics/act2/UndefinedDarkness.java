package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class UndefinedDarkness extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("UndefinedDarkness");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Darkness.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Darkness.png"));

    public static final ArrayList<AbstractCard> obscuredCards = new ArrayList<>();
    private static final int COMBATS = 3;

    public UndefinedDarkness() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = COMBATS;
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        obscuredCards.addAll(AbstractDungeon.player.drawPile.group);
        obscuredCards.addAll(AbstractDungeon.player.discardPile.group);
        obscuredCards.addAll(AbstractDungeon.player.hand.group);
    }

    @Override
    public void onVictory() {
        obscuredCards.clear();
        this.counter--;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + COMBATS + DESCRIPTIONS[1];
    }

}
