package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class ConquerorOfFear extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("ConquerorOfFear");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Darkness.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Darkness.png"));

    private static final int THRESHOLD = 10;
    private static final int BONUS_STATS = 2;
    private boolean active = false;

    public ConquerorOfFear() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onRefreshHand() {
        if (AbstractDungeon.player.discardPile.group.size() >= THRESHOLD && !active) {
            active = true;
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, BONUS_STATS), BONUS_STATS));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, BONUS_STATS), BONUS_STATS));
        } else if (AbstractDungeon.player.discardPile.group.size() < THRESHOLD && active) {
            active = false;
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, -BONUS_STATS), -BONUS_STATS));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, -BONUS_STATS), -BONUS_STATS));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + THRESHOLD + DESCRIPTIONS[1] + BONUS_STATS + DESCRIPTIONS[2];
    }

}
