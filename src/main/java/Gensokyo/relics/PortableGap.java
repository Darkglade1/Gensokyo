package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class PortableGap extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("PortableGap");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PortableGap.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PortableGap.png"));
    private static final int MAX_HP_LOSS = 3;
    private boolean justUsed = false;
    private int distanceTraveled = 0;

    public PortableGap() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (justUsed) {
            justUsed = false;
            AbstractDungeon.floorNum += distanceTraveled - 1;
            distanceTraveled = 0;
        }
    }

    public void onTrigger(int distance)
    {
        this.flash();
        justUsed = true;
        distanceTraveled = distance;
        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
        AbstractDungeon.player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_HP_LOSS + DESCRIPTIONS[1];
    }

}
