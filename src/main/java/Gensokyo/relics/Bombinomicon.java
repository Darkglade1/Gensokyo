package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.SpecialBombToHand;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class Bombinomicon extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("Bombinomicon");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Bombinomicon.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Bombinomicon.png"));

    public Bombinomicon() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new SpecialBombToHand());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
