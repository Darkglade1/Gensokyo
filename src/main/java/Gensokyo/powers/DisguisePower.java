package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class DisguisePower extends AbstractPower implements OnLoseTempHpPower {

    public static final String POWER_ID = GensokyoMod.makeID("DisguisePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static AbstractMonster currentDisguise = null;
    private float HB_X;
    private float HB_Y;
    private float HB_W;
    private float HB_H;

    public DisguisePower(AbstractCreature owner, float originalHB_X, float originalHB_Y, float originalHB_W, float originalHB_H) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.HB_X = originalHB_X;
        this.HB_Y = originalHB_Y;
        this.HB_W = originalHB_W;
        this.HB_H = originalHB_H;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("confusion");

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        switchDisguise(0);
    }
    @Override
    public void onRemove() {
        currentDisguise = null;
        //this.owner.hb_x = this.HB_X;
        //this.owner.hb_y = this.HB_Y;
        //this.owner.hb_w = this.HB_W;
        //this.owner.hb_h = this.HB_H;
    }

    public void switchDisguise(int disguise) {
        switch(disguise) {
            case 0:
                GremlinNob nob = new GremlinNob(0, 0);
                switching(nob);
                break;
            case 1:
                Lagavulin laga = new Lagavulin(false);
                switching(laga);
                break;
            case 2:
                Sentry sentry = new Sentry(0, 0);
                switching(sentry);
                break;
        }
    }

    private void switching (AbstractMonster mo) {
        currentDisguise = mo;
        currentDisguise.drawX = this.owner.drawX;
        currentDisguise.drawY = this.owner.drawY;
        //this.owner.hb_x = currentDisguise.hb_x;
        //this.owner.hb_y = currentDisguise.hb_y;
        //this.owner.hb_w = currentDisguise.hb_w;
        //this.owner.hb_h = currentDisguise.hb_h;
        updateDescription();
    }

    @Override
    public void onVictory() {
        this.onRemove();
    }

    public int onLoseTempHp(DamageInfo info, int damageAmount) {
        if (currentDisguise != null) {
            currentDisguise.useStaggerAnimation();
        }
        return damageAmount;
    }

    //Needed a patch to get this to be called
    @Override
    public int onLoseHp(int damageAmount) {
        this.onRemove();
        updateDescription();
        return damageAmount;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == this.owner) {
            if (currentDisguise != null) {
                if (Settings.FAST_MODE) {
                    currentDisguise.useFastAttackAnimation();
                } else {
                    currentDisguise.useSlowAttackAnimation();
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (currentDisguise == null) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }
}