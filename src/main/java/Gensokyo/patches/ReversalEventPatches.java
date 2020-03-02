package Gensokyo.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class ReversalEventPatches {

    private static int startOfBattleHP;
    private static int endOfBattleHP;
    private static int goldReward;
    private static String name;
    private static ArrayList<AbstractMonster> monsters;
    public static ArrayList<ReversalRewardItem> eliteEncounters = new ArrayList<>();
    public static ArrayList<ReversalRewardItem> normalEncounters = new ArrayList<>();

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="applyStartOfCombatLogic"
    )
    public static class getStartOfBattleStats {
        @SpirePostfixPatch
        public static void getStuff(AbstractPlayer _instance) {
            Reset();
            startOfBattleHP = _instance.currentHealth;
            monsters = AbstractDungeon.getMonsters().monsters;
        }
    }

    @SpirePatch(
            clz= MonsterRoomElite.class,
            method="dropReward"
    )
    public static class GetPostBattleStats {
        @SpirePostfixPatch
        public static void getStuff(MonsterRoomElite _instance) {
            endOfBattleHP = AbstractDungeon.player.currentHealth;
            AbstractRelic relic = null;
            for (RewardItem reward : AbstractDungeon.getCurrRoom().rewards) {
                if (reward.relic != null) {
                    relic = reward.relic;
                }
            }
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
                String name = null;
                for (AbstractMonster monster : monsters) {
                    if (monster.type == AbstractMonster.EnemyType.ELITE) {
                        name = monster.name;
                    }
                }
                if (name == null) {
                    name = monsters.get(0).name;
                }
                System.out.println(startOfBattleHP);
                System.out.println(endOfBattleHP);
                System.out.println(name);
                System.out.println(relic);
                eliteEncounters.add(new ReversalRewardItem(startOfBattleHP - endOfBattleHP, relic.relicId, 0, null, name));
            }
        }
    }

    @SpirePatch(
            clz= AbstractRoom.class,
            method="addGoldToRewards"
    )
    public static class GetGold {
        @SpirePostfixPatch
        public static void getGold(AbstractRoom _instance, int goldAmt) {
            goldReward = goldAmt;
            endOfBattleHP = AbstractDungeon.player.currentHealth;
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom && !AbstractDungeon.getCurrRoom().eliteTrigger) {
                for (AbstractMonster monster : monsters) {
                    if (monster.type == AbstractMonster.EnemyType.NORMAL) {
                        name = monster.name;
                    }
                }
                if (name == null) {
                    name = monsters.get(0).name;
                }
            }
        }
    }

    @SpirePatch(
            clz= CardRewardScreen.class,
            method="acquireCard"
    )
    public static class GetAcquiredCard {
        @SpirePrefixPatch
        public static void getStuff(CardRewardScreen _instance, AbstractCard hoveredCard) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom && !AbstractDungeon.getCurrRoom().eliteTrigger) {
                System.out.println(startOfBattleHP);
                System.out.println(endOfBattleHP);
                System.out.println(goldReward);
                System.out.println(name);
                System.out.println(hoveredCard);
                normalEncounters.add(new ReversalRewardItem(startOfBattleHP - endOfBattleHP, null, goldReward, hoveredCard.cardID, name));
                Reset();
            }
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="nextRoomTransition",
            paramtypez = SaveFile.class
    )
    public static class GetNoCardReward {
        @SpirePrefixPatch
        public static void getNoCard(AbstractDungeon _instance, SaveFile saveFile) {
            System.out.println(startOfBattleHP);
            System.out.println(endOfBattleHP);
            System.out.println(goldReward);
            System.out.println(name);
            if (startOfBattleHP != 0 && endOfBattleHP != 0) {
                normalEncounters.add(new ReversalRewardItem(startOfBattleHP - endOfBattleHP, null, goldReward, null, name));
            }
        }
    }

    private static void Reset() {
        startOfBattleHP = 0;
        endOfBattleHP = 0;
        goldReward = 0;
        name = null;
        monsters = null;
    }

    public static class ReversalRewardItem {
        public int heal;
        public String relicID;
        public int gold;
        public String cardID;
        public String monsterName;

        public ReversalRewardItem(int heal, String relic, int gold, String card, String monsterName) {
            this.heal = heal;
            this.relicID = relic;
            this.gold = gold;
            this.cardID = card;
            this.monsterName = monsterName;
        }
    }

}