package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class MaskOfHope extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("MaskOfHope");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MaskOfHope.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("MaskOfHope.png"));

    private static final int TEMP_HP = 8;

    public MaskOfHope() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, TEMP_HP));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TEMP_HP + DESCRIPTIONS[1];
    }

}
