package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class MosesMiracle extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("MosesMiracle");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Moses.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Moses.png"));

    public MosesMiracle() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onShuffle() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Miracle()));
    }

    @Override
    public void instantObtain() {
        if (AbstractDungeon.player.hasRelic(DayTheSeaSplit.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof DayTheSeaSplit) {
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
        if (AbstractDungeon.player.hasRelic(DayTheSeaSplit.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof DayTheSeaSplit) {
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
        if (AbstractDungeon.player.hasRelic(DayTheSeaSplit.ID)) {
            int relicIndex = -1;
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i) instanceof DayTheSeaSplit) {
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
        return DESCRIPTIONS[0];
    }

}
