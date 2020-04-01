package Gensokyo.relics.act1;

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

    private boolean triggered = false;
    private static int TRIGGER_COUNT = 2;
    private static int BLOCK = 4;

    public Mercy() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
        this.triggered = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL && !triggered) {
            ++this.counter;
            if (this.counter % TRIGGER_COUNT == 0) {
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
                this.triggered = true;
            }
        }
    }

    public void onVictory() {
        this.counter = -1;
        this.triggered = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGER_COUNT + DESCRIPTIONS[1] + BLOCK + DESCRIPTIONS[2];
    }

}
