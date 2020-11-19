package Gensokyo.rooms.nitori.helpers;

import Gensokyo.cards.Evolve.BlemishedSteel;
import Gensokyo.cards.Evolve.DepletedGenerator;
import Gensokyo.cards.Evolve.ExoticEgg;
import Gensokyo.cards.Evolve.LockedMedkit;
import Gensokyo.cards.Evolve.MysteriousEgg;
import Gensokyo.cards.Evolve.RustyChest;
import Gensokyo.cards.Evolve.ScrapIron;
import Gensokyo.cards.Evolve.Shovel;
import Gensokyo.cards.Evolve.TarnishedGold;
import Gensokyo.cards.Evolve.TrainingManual;
import Gensokyo.cards.Item.LifeforceConverter;
import Gensokyo.cards.Item.MedicineKit;
import Gensokyo.cards.Item.OverchargedCore;
import Gensokyo.cards.Item.RPG;
import Gensokyo.cards.Item.ReactiveArmor;
import Gensokyo.cards.Item.Taser;
import Gensokyo.relics.act1.OccultBall;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

public class gensokyoCardHelper {

    public static ArrayList<AbstractCard> getNitoriShopCards() {
        ArrayList<AbstractCard> itemList = getAllItemCards();
        Collections.shuffle(itemList, AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> cards = new ArrayList<>(new ArrayList<>(itemList.subList(0, 3)));

        ArrayList<AbstractCard> list = OccultBall.getAllUrbanLegends();
        Collections.shuffle(list, AbstractDungeon.cardRandomRng.random);
        cards.addAll(new ArrayList<>(list.subList(0, 2)));

        ArrayList<AbstractCard> evolveList = getAllEvolveCards();
        Collections.shuffle(evolveList, AbstractDungeon.cardRandomRng.random);
        cards.addAll(new ArrayList<>(evolveList.subList(0, 5)));
        return cards;
    }

    public static ArrayList<AbstractCard> getAllEvolveCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new BlemishedSteel());
        list.add(new DepletedGenerator());
        list.add(new ExoticEgg());
        list.add(new LockedMedkit());
        list.add(new MysteriousEgg());
        list.add(new RustyChest());
        list.add(new ScrapIron());
        list.add(new Shovel());
        list.add(new TarnishedGold());
        list.add(new TrainingManual());
        return list;
    }

    public static ArrayList<AbstractCard> getAllItemCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new RPG());
        list.add(new ReactiveArmor());
        list.add(new LifeforceConverter());
        list.add(new MedicineKit());
        list.add(new OverchargedCore());
        list.add(new Taser());
        return list;
    }
}
