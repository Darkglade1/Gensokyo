package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act3.Kasen;
import Gensokyo.monsters.act3.Kume;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class KasenMinon extends AbstractPower implements InvisiblePower {
    public static final String POWER_ID = GensokyoMod.makeID("KasenMinon");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Kasen kasen;

    public KasenMinon(AbstractCreature owner, Kasen kasen) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.kasen = kasen;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void onDeath() {
        if (kasen != null) {
            AbstractPower power = kasen.getPower(AnimalFriend.POWER_ID);
            if (power != null) {
                power.onSpecificTrigger();
            }
            if (owner.id.equals(Kume.ID) || owner.id.equals(Byrd.ID)) {
                kasen.minions[0] = null;
            }
            if (owner.id.equals(JawWorm.ID) || owner.id.equals(Snecko.ID)) {
                kasen.minions[1] = null;
            }
        }
    }
}
