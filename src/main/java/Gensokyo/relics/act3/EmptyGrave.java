package Gensokyo.relics.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class EmptyGrave extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("EmptyGrave");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Tombstone.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Tombstone.png"));

    private static final int FLOORS = 3;
    private static final float HEAL = 0.5f;

    public EmptyGrave() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
    }

    public void onEnterRoom(AbstractRoom room) {
        if (this.counter < FLOORS) {
            this.flash();
            this.counter++;
            if (this.counter >= FLOORS) {
                int amountToGain = (int)(AbstractDungeon.player.maxHealth * HEAL);
                AbstractDungeon.player.heal(amountToGain);
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FLOORS + DESCRIPTIONS[1] + ((int)(HEAL * 100)) + DESCRIPTIONS[2];
    }

}
