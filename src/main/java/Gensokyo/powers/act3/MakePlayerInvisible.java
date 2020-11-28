package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class MakePlayerInvisible extends AbstractPower implements InvisiblePower {

    public static final String POWER_ID = GensokyoMod.makeID("MakePlayerInvisible");

    public MakePlayerInvisible(AbstractCreature owner) {
        name = "";
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public void updateDescription() {
        description = "";
    }
}
