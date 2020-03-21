package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class ChorusOfJoy extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("ChorusOfJoy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Trumpet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Trumpet.png"));

    private boolean firstTurn;

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
            this.addToTop(new GainEnergyAction(1));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NoDrawPower(AbstractDungeon.player)));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
