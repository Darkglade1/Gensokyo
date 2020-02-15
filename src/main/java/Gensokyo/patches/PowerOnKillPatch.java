package Gensokyo.patches;

import Gensokyo.powers.act1.OnKillPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = boolean.class
)

// A patch that allows a power to trigger upon a monster's death
public class PowerOnKillPatch {
    @SpirePostfixPatch
    public static void triggerOnKillPowers(AbstractMonster instance, boolean triggerRelics) {
        if (triggerRelics) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof OnKillPower) {
                    ((OnKillPower)power).onKill(instance.hasPower(MinionPower.POWER_ID));
                }
            }
        }
    }
}
