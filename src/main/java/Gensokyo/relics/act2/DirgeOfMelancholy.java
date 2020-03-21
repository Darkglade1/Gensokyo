package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class DirgeOfMelancholy extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("DirgeOfMelancholy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Violin.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Violin.png"));

    private static final int STR_DOWN = 2;

    private boolean firstTurn;

    public DirgeOfMelancholy() {
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
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new LoseEnergyAction(1));
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                this.addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new StrengthPower(mo, -STR_DOWN), -STR_DOWN));
            }
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STR_DOWN + DESCRIPTIONS[1];
    }

}
