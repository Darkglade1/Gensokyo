package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.TransferBlockToPetAction;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class Companionship extends CustomRelic implements ClickableRelic {

    public static final String ID = GensokyoMod.makeID("Companionship");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Companionship.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Companionship.png"));

    private static int BLOCK = 5;

    public Companionship() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new TransferBlockToPetAction(BLOCK));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + BLOCK + DESCRIPTIONS[1];
    }

}
