package Gensokyo.rooms.nitori;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.SpecialBombToHand;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class NitoriBomb extends CustomRelic {

    public static final String ID = GensokyoMod.makeID(NitoriBomb.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NitoriBomb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NitoriBomb.png"));
    private static final int REMOVE_AMOUNT = 1;
    public NitoriBomb() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    @Override
    public String getUpdatedDescription() { return String.format(DESCRIPTIONS[0], REMOVE_AMOUNT); }
}
