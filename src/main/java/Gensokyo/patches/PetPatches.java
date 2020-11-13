package Gensokyo.patches;

import Gensokyo.minions.AbstractPet;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class PetPatches {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="onVictory"
    )
    public static class OnVictoryPatch {
        public static void Prefix(AbstractPlayer __instance) {
            MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(__instance);
            for (AbstractMonster mo : playerMinions.monsters) {
                if (mo instanceof AbstractPet) {
                    AbstractPet pet = (AbstractPet)mo;
                    pet.onVictoryUpdateHP();
                }
            }
        }
    }

//    @SpirePatch(
//            clz= AbstractPlayer.class,
//            method="update"
//    )
//    public static class UpdatePatch {
//        public static void Prefix(AbstractPlayer __instance) {
//            if(AbstractDungeon.player.hasPower(PolymorphPower.POWER_ID)) {
//                PolymorphPower power = (PolymorphPower)AbstractDungeon.player.getPower(PolymorphPower.POWER_ID);
//                Animal animal = power.animal;
//                if(animal != null) {
//                    animal.update();
//                }
//            }
//        }
//    }
}