package Gensokyo.relics.act2;

import Gensokyo.CardMods.ObscureMod;
import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class UndefinedDarkness extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("UndefinedDarkness");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Darkness.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Darkness.png"));

    public static final int COMBATS = 1;

    public UndefinedDarkness() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = COMBATS;
    }

    @Override
    public void atBattleStartPreDraw() {
        if (this.counter > 0) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                CardModifierManager.addModifier(card, new ObscureMod());
            }
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                CardModifierManager.addModifier(card, new ObscureMod());
            }
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                CardModifierManager.addModifier(card, new ObscureMod());
            }
            for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                CardModifierManager.addModifier(card, new ObscureMod());
            }
        }
    }

    @Override
    public void onVictory() {
        this.counter--;
        if (this.counter <= 0) {
            counter = 0;
            AbstractRelic relic = RelicLibrary.getRelic(ConquerorOfFear.ID).makeCopy();
            AbstractDungeon.getCurrRoom().addRelicToRewards(relic);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + COMBATS + DESCRIPTIONS[1];
    }

}
