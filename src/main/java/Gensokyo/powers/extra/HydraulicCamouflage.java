package Gensokyo.powers.extra;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class HydraulicCamouflage extends AbstractPower implements OnReceivePowerPower {

    public static final String POWER_ID = GensokyoMod.makeID("HydraulicCamouflage");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int THRESHOLD = 2;
    private boolean justStrengthDown = false;
    private boolean negateGainStrengthUp = false;
    private boolean isEX = false;
    private float DAMAGE_REDUCTION = 0.1f;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HydraulicCamouflage84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HydraulicCamouflage32.png"));
    private static final Texture tex84_2 = TextureLoader.getTexture(makePowerPath("HydraulicCamouflage_inv84.png"));
    private static final Texture tex32_2 = TextureLoader.getTexture(makePowerPath("HydraulicCamouflage_inv32.png"));

    public HydraulicCamouflage(AbstractCreature owner, boolean extraSwitch) {
        ID = POWER_ID;
        if(extraSwitch){
            name = DESCRIPTIONS[2];
            this.isEX = true;
            this.region128 = new TextureAtlas.AtlasRegion(tex84_2, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32_2, 0, 0, 32, 32);
        }
        else {
            name = NAME;
            this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        }
        this.owner = owner;
        this.amount = -1;

        type = PowerType.BUFF;
        isTurnBased = false;

        updateDescription();
    }
    public boolean returnIsExtra(){ return isEX; }
    public void extraTransition(){
        name = DESCRIPTIONS[2];
        this.isEX = true;
        this.region128 = new TextureAtlas.AtlasRegion(tex84_2, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32_2, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (returnIsExtra()) {
            return damage * DAMAGE_REDUCTION;
        } else { return damage; }
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.type == PowerType.DEBUFF) {
            //Handle temp Strength down effects
            if (power instanceof StrengthPower) {
                if (this.amount < THRESHOLD) {
                    justStrengthDown = true;
                } else {
                    negateGainStrengthUp = true;
                }
            }
            if (power instanceof GainStrengthPower) {
                if (justStrengthDown) {
                    justStrengthDown = false;
                    return true;
                }
                if (negateGainStrengthUp) {
                    negateGainStrengthUp = false;
                    return false;
                }
            }
            //Actual code
            if (this.amount >= THRESHOLD) {
                this.amount = 1;
                justStrengthDown = false;
                return false;
            } else {
                this.amount++;
                negateGainStrengthUp = false;
            }
        }
        return true;
    }

    @Override
    public void updateDescription() {
        description = returnIsExtra() ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
