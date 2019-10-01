package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class PerfectCherryBlossom extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("PerfectCherryBlossom");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PerfectCherryBlossom.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PerfectCherryBlossom.png"));

    public PerfectCherryBlossom() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
