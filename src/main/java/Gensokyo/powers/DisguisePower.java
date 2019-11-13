package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Mamizou;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class DisguisePower extends AbstractPower implements OnLoseTempHpPower {

    public static final String POWER_ID = GensokyoMod.makeID("DisguisePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    Mamizou mamizou;

    public DisguisePower(AbstractCreature owner, Mamizou mamizou) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.mamizou = mamizou;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("confusion");

        description = DESCRIPTIONS[0];
    }

    @Override
    public int onLoseTempHp(DamageInfo info, int damageAmount) {
        if (mamizou != null && mamizou.currentDisguise != null) {
            mamizou.currentDisguise.useStaggerAnimation();
        }
        return damageAmount;
    }

    @Override
    public void onRemove() {
        //Reapply power in case player somehow removes it
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DisguisePower(this.owner, this.mamizou)));
    }

    //Needed a patch to get this to be called
    @Override
    public int onLoseHp(int damageAmount) {
        if (mamizou != null) {
            mamizou.removeDisguise();
            updateDescription();
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        if (mamizou != null && mamizou.currentDisguise == null) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }
}