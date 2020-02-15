package Gensokyo.relics.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.ExhaustRandomCurseInHandAction;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class NagashiBinaDoll extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("NagashiBinaDoll");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NagashiBinaDoll.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NagashiBinaDoll.png"));

    public NagashiBinaDoll() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onPlayerEndTurn() {
        AbstractDungeon.actionManager.addToBottom(new ExhaustRandomCurseInHandAction(this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
