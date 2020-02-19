package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class ConquerorOfFear extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("ConquerorOfFear");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Conqueror.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Conqueror.png"));

    private static final int THRESHOLD = 10;
    private static final int BONUS_STATS = 3;
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
    public void instantObtain() {
        if (AbstractDungeon.player.hasRelic(UndefinedDarkness.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof UndefinedDarkness) {
                    relicIndex = i;
                    break;
                }
            }
            if (relicIndex >= 0) {
                super.instantObtain(AbstractDungeon.player, relicIndex, false);
            } else {
                super.instantObtain();
            }
        } else {
            super.instantObtain();
        }
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (AbstractDungeon.player.hasRelic(UndefinedDarkness.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof UndefinedDarkness) {
                    relicIndex = i;
                    break;
                }
            }
            if (relicIndex >= 0) {
                super.instantObtain(AbstractDungeon.player, relicIndex, false);
            } else {
                super.instantObtain(p, slot, callOnEquip);
            }
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(UndefinedDarkness.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof UndefinedDarkness) {
                    relicIndex = i;
                    break;
                }
            }
            if (relicIndex >= 0) {
                this.instantObtain(AbstractDungeon.player, relicIndex, false);
            } else {
                super.obtain();
            }
        } else {
            super.obtain();
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + THRESHOLD + DESCRIPTIONS[1] + BONUS_STATS + DESCRIPTIONS[2];
    }

}
