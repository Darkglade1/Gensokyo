package Gensokyo.minions;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class PetUtils {

    public static boolean playerHasPet() {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(AbstractDungeon.player);
        for (AbstractMonster mo : playerMinions.monsters) {
            if (mo instanceof AbstractPet) {
                return true;
            }
        }
        return false;
    }

    public static AbstractPet getPet() {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(AbstractDungeon.player);
        for (AbstractMonster mo : playerMinions.monsters) {
            if (mo instanceof AbstractPet) {
                return (AbstractPet) mo;
            }
        }
        return null;
    }
}