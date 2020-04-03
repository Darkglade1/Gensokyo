package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class ChorusOfJoy extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("ChorusOfJoy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Trumpet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Trumpet.png"));

    private boolean firstTurn;
    private static final int ENERGY = 1;
    private static final int DISCARD = 2;

    public ChorusOfJoy() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new GainEnergyAction(ENERGY));
            this.addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, DISCARD, false));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        if (DISCARD == 1) {
            return DESCRIPTIONS[0] + DISCARD + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0] + DISCARD + DESCRIPTIONS[2];
        }
    }

}
