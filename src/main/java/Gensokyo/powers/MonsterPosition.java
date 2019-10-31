package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class MonsterPosition extends TwoAmountPower {

    public static final String POWER_ID = GensokyoMod.makeID("MonsterPositionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MonsterPosition(AbstractCreature owner, int amount, int position) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount2 = position;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("storm");

        updateDescription();
    }

    @Override
    public void duringTurn() {
        if (this.amount > 1 || this.owner.isDying) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, ID, 1));
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }

    }
}
