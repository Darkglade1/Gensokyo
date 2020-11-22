package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class EyesOfDeath extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("EyesOfDeath");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean wearOff = true;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("EyesOfDeath84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("EyesOfDeath32.png"));

    public EyesOfDeath(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.addToBot(new ApplyPowerAction(target, this.owner, new Doom(target, this.amount, wearOff), this.amount, true));
        }

    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
