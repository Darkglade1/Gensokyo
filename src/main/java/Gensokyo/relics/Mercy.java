package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class Mercy extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("Mercy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Mercy.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Mercy.png"));

    private boolean gainBlock = false;
    private static int BLOCK = 6;

    public Mercy() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atPreBattle() {
        this.flash();
        this.gainBlock = true;
        if (!this.pulse) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.beginPulse();
        this.pulse = true;
        if (this.gainBlock) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
        }
        this.gainBlock = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.gainBlock = false;
            this.pulse = false;
        }
    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK + DESCRIPTIONS[1];
    }

}
