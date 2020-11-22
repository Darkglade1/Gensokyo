package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class Doom extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("Doom");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final float DOOM_PERCENT = 0.50f;
    private boolean attacked = false;
    public boolean wearOff;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DemonMask84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DemonMask32.png"));

    public Doom(AbstractCreature owner, int amount, boolean wearOff) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.wearOff = wearOff;
        this.type = PowerType.DEBUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        attacked = true;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * ((DOOM_PERCENT * amount) + 1);
        } else {
            return damage;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            attacked = true;
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if (wearOff) {
            if (attacked) {
                attacked = false;
            } else {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (int)(DOOM_PERCENT * amount * 100) + DESCRIPTIONS[1];
        if (wearOff) {
            description += DESCRIPTIONS[2];
        }
    }
}
