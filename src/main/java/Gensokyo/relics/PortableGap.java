package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class PortableGap extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("PortableGap");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PerfectCherryBlossom.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PerfectCherryBlossom.png"));

    public PortableGap() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
