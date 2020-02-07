package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;

import static Gensokyo.GensokyoMod.makePowerPath;

public class Judgement extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Judgement");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Judgement84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Judgement32.png"));

    public Judgement(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        this.owner.decreaseMaxHealth(this.amount);
        AbstractDungeon.effectList.add(new StrikeEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, this.amount));
    }

    @Override
    public void onVictory() {
        this.owner.increaseMaxHp(this.amount, false);
    }

    @Override
    public void onRemove() {
        this.owner.increaseMaxHp(this.amount, false);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
