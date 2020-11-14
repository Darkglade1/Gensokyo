package Gensokyo.minions;

import Gensokyo.cards.Pets.SummonFieryMouse;
import Gensokyo.cards.Pets.SummonJeweledCobra;
import Gensokyo.cards.Pets.SummonYinYangFox;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import java.util.ArrayList;
import java.util.Collections;

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

    public static AbstractCard getRandomPetCard() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new SummonYinYangFox());
        list.add(new SummonFieryMouse());
        list.add(new SummonJeweledCobra());
        Collections.shuffle(list, AbstractDungeon.cardRandomRng.random);
        return list.get(0);
    }
}