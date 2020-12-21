package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class AliceEventEnergy extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("AliceEventEnergy");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int turns;

    public AliceEventEnergy(AbstractCreature owner, int turns, int energy) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.turns = turns;
        this.amount2 = -1;
        this.amount = energy;
        this.loadRegion("energized_blue");
        updateDescription();
    }

    @Override
    public void onEnergyRecharge() {
        amount2++;
        if (amount2 % turns == 0) {
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
