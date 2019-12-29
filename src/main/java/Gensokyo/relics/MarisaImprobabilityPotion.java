package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class MarisaImprobabilityPotion extends CustomRelic implements ClickableRelic {

    public static final String ID = GensokyoMod.makeID("MarisaImprobabilityPotion");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Potion.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Potion.png"));

    private boolean usedThisCombat = false;
    private static int HP_LOSS = 5;
    private static int STR_DEX_BUFF = 3;

    public MarisaImprobabilityPotion() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || usedThisCombat) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            usedThisCombat = true;
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, HP_LOSS));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR_DEX_BUFF), STR_DEX_BUFF));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, STR_DEX_BUFF), STR_DEX_BUFF));
        }
    }

    @Override
    public void onVictory() {
        this.usedThisCombat = false;
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(MarisaIngredientList.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof MarisaIngredientList) {
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
        return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + HP_LOSS + DESCRIPTIONS[1] + STR_DEX_BUFF + DESCRIPTIONS[2];
    }

}
