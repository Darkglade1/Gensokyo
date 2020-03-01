package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class DemonMask extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("DemonMask");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DemonMask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DemonMask.png"));

    private static final int STRENGTH = 2;
    private boolean gainStrength = false;

    public DemonMask() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.pulse = false;
    }

    @Override
    public void atPreBattle() {
        this.flash();
        this.gainStrength = true;
        if (!this.pulse) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.beginPulse();
        this.pulse = true;
        if (this.gainStrength) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
        }
        this.gainStrength = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            this.gainStrength = false;
            this.pulse = false;
        }

    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH + DESCRIPTIONS[1];
    }

}
