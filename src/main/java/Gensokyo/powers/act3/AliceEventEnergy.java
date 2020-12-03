package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static Gensokyo.GensokyoMod.makePowerPath;

public class AliceEventEnergy extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("AliceEventEnergy");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int turns;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Reflowering84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Reflowering32.png"));

    public AliceEventEnergy(AbstractCreature owner, int turns, int energy) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.turns = turns;
        this.amount2 = 0;
        this.amount = energy;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onEnergyRecharge() {
        amount2++;
        if (amount2 >= turns) {
            amount2 = 0;
            this.flash();
            AbstractDungeon.player.gainEnergy(this.amount);
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + turns + DESCRIPTIONS[1];
        for (int i = 0; i < amount; i++) {
            description += " [E]";
        }
        description += DESCRIPTIONS[2];
    }
}
