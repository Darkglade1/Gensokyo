package Gensokyo.relics.dummyRelics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class DummyBloodcastTooltip extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("DummyBloodcastTooltip");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Trumpet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Trumpet.png"));

    public DummyBloodcastTooltip() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
