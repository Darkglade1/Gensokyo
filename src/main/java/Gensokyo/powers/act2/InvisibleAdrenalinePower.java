package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class InvisibleAdrenalinePower extends TwoAmountPower implements InvisiblePower {

    public static final String POWER_ID = GensokyoMod.makeID("InvisibleAdrenalinePower");
    public boolean enabled = true;

    public InvisibleAdrenalinePower(AbstractCreature owner, int energy, int draw) {
        name = "";
        ID = POWER_ID;

        this.owner = owner;
        this.amount = energy;
        this.amount2 = draw;

        this.loadRegion("storm");

        type = PowerType.BUFF;
        isTurnBased = false;
        description = "";
    }

    @Override
    public void onEnergyRecharge() {
        if (enabled) {
            AbstractDungeon.player.gainEnergy(this.amount);
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, amount2));
        }
    }
}
