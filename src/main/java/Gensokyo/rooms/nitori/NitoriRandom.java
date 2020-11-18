//package Gensokyo.rooms.nitori;
//
//import Gensokyo.GensokyoMod;
//import Gensokyo.util.TextureLoader;
//import basemod.abstracts.CustomRelic;
//import com.badlogic.gdx.graphics.Texture;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//import com.megacrit.cardcrawl.relics.Circlet;
//
//import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
//import static Gensokyo.GensokyoMod.makeRelicPath;
//
//public class NitoriRandom extends CustomRelic {
//
//    public static final String ID = GensokyoMod.makeID(NitoriRandom.class.getSimpleName());
//    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NitoriRandom.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NitoriRandom.png"));
//    public NitoriRandom() {
//        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
//    }
//    @Override
//    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }
//}
