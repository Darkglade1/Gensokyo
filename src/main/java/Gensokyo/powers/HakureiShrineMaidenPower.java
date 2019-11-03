package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Reimu;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class HakureiShrineMaidenPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("HakureiShrineMaidenPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HakureiShrineMaidenPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("storm");

        updateDescription();
    }

    @Override
    public void duringTurn() {
        if (owner instanceof Reimu && !owner.isDying) {
            Reimu reimu = (Reimu)owner;
            if (reimu.orbNum() == 0) {
                reimu.spawnOrb(2);
                reimu.spawnOrb(3);
            } else {
                reimu.spawnOrb(3);
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
