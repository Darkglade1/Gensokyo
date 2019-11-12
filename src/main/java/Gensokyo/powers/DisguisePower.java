package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Mamizou;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
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
    private Hitbox originalIntentHb;
    private int counter = 0;

    public DisguisePower(AbstractCreature owner, Hitbox originalHitbox) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.originalIntentHb = originalHitbox;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("confusion");

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        switchDisguise();
        if (owner instanceof Mamizou) {
            ((Mamizou)owner).hasDisguise = true;
        }
    }
    @Override
    public void onRemove() {
        currentDisguise = null;
        if (owner instanceof AbstractMonster) {
            AbstractMonster mo = (AbstractMonster)owner;
            mo.intentHb = originalIntentHb;
        }
    }

    public void switchDisguise() {
        int disguise = counter;
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
        counter++;
        if (counter > 2) {
            counter = 0;
        }
    }

    private void switching (AbstractMonster mo) {
        currentDisguise = mo;
        currentDisguise.drawX = this.owner.drawX;
        currentDisguise.drawY = this.owner.drawY;
        if (mo instanceof GremlinNob) {
            currentDisguise.drawX += 80.0F; //Centers Nob correctly
        }
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            m.intentHb = mo.intentHb; //Moves the intent icon to more appropriate spot
        }
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