package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.SpontaneousHumanCombustion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class SpontaneousHumanCombustionPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("SpontaneousHumanCombustionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int selfDamageIncrease = SpontaneousHumanCombustion.SELF_DAMAGE;

    public SpontaneousHumanCombustionPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.loadRegion("combust");

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        selfDamageIncrease += SpontaneousHumanCombustion.SELF_DAMAGE;
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        selfDamageIncrease -= SpontaneousHumanCombustion.SELF_DAMAGE;
        super.reducePower(reduceAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        this.amount += selfDamageIncrease;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + selfDamageIncrease + DESCRIPTIONS[2];
    }
}
