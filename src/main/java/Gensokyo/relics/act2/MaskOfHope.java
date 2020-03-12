package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class MaskOfHope extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("MaskOfHope");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MaskOfHope.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("MaskOfHope.png"));

    private static final int TEMP_HP = 10;
    private boolean usedThisCombat;

    public MaskOfHope() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        this.pulse = true;
        this.beginPulse();
    }

    @Override
    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !usedThisCombat) {
            this.flash();
            this.pulse = false;
            this.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, TEMP_HP));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            usedThisCombat = true;
            this.grayscale = true;
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TEMP_HP + DESCRIPTIONS[1];
    }

}
