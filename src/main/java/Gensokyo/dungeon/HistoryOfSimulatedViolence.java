package Gensokyo.dungeon;

import Gensokyo.events.AHistoryOfViolence;
import Gensokyo.scenes.GensokyoScene;
import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

public class HistoryOfSimulatedViolence extends CustomDungeon {

    public static String ID = "Gensokyo:HistoryOfViolence";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public HistoryOfSimulatedViolence() {
        super(NAME, ID, "images/ui/event/panel.png");
        this.onEnterEvent(AHistoryOfViolence.class);
    }

    public HistoryOfSimulatedViolence(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public HistoryOfSimulatedViolence(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new GensokyoScene();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.00F;
        restRoomChance = 0.00F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 1.00F;
        eliteRoomChance = 0.00F;

        smallChestChance = 30;
        mediumChestChance = 35;
        largeChestChance = 35;

        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;

        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.325F;
        } else {
            cardUpgradedChance = 0.45F;
        }
    }

    @Override
    protected void makeMap() {
        map = new ArrayList();
        ArrayList<MapRoomNode> row1 = new ArrayList();
        MapRoomNode restNode = new MapRoomNode(3, 0);
        restNode.room = new RestRoom();
        MapRoomNode shopNode = new MapRoomNode(3, 1);
        shopNode.room = new ShopRoom();
        MapRoomNode enemyNode = new MapRoomNode(3, 2);
        enemyNode.room = new MonsterRoomElite();
        MapRoomNode bossNode = new MapRoomNode(3, 3);
        bossNode.room = new MonsterRoomBoss();
        MapRoomNode victoryNode = new MapRoomNode(3, 4);
        victoryNode.room = new TrueVictoryRoom();
        this.connectNode(restNode, shopNode);
        this.connectNode(shopNode, enemyNode);
        enemyNode.addEdge(new MapEdge(enemyNode.x, enemyNode.y, enemyNode.offsetX, enemyNode.offsetY, bossNode.x, bossNode.y, bossNode.offsetX, bossNode.offsetY, false));
        row1.add(new MapRoomNode(0, 0));
        row1.add(new MapRoomNode(1, 0));
        row1.add(new MapRoomNode(2, 0));
        row1.add(restNode);
        row1.add(new MapRoomNode(4, 0));
        row1.add(new MapRoomNode(5, 0));
        row1.add(new MapRoomNode(6, 0));
        ArrayList<MapRoomNode> row2 = new ArrayList();
        row2.add(new MapRoomNode(0, 1));
        row2.add(new MapRoomNode(1, 1));
        row2.add(new MapRoomNode(2, 1));
        row2.add(shopNode);
        row2.add(new MapRoomNode(4, 1));
        row2.add(new MapRoomNode(5, 1));
        row2.add(new MapRoomNode(6, 1));
        ArrayList<MapRoomNode> row3 = new ArrayList();
        row3.add(new MapRoomNode(0, 2));
        row3.add(new MapRoomNode(1, 2));
        row3.add(new MapRoomNode(2, 2));
        row3.add(enemyNode);
        row3.add(new MapRoomNode(4, 2));
        row3.add(new MapRoomNode(5, 2));
        row3.add(new MapRoomNode(6, 2));
        ArrayList<MapRoomNode> row4 = new ArrayList();
        row4.add(new MapRoomNode(0, 3));
        row4.add(new MapRoomNode(1, 3));
        row4.add(new MapRoomNode(2, 3));
        row4.add(bossNode);
        row4.add(new MapRoomNode(4, 3));
        row4.add(new MapRoomNode(5, 3));
        row4.add(new MapRoomNode(6, 3));
        ArrayList<MapRoomNode> row5 = new ArrayList();
        row5.add(new MapRoomNode(0, 4));
        row5.add(new MapRoomNode(1, 4));
        row5.add(new MapRoomNode(2, 4));
        row5.add(victoryNode);
        row5.add(new MapRoomNode(4, 4));
        row5.add(new MapRoomNode(5, 4));
        row5.add(new MapRoomNode(6, 4));
        map.add(row1);
        map.add(row2);
        map.add(row3);
        map.add(row4);
        map.add(row5);
        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        firstRoomChosen = false;
        fadeIn();
    }

    private void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    protected void generateMonsters() {
        monsterList = new ArrayList();
        monsterList.add("Shield and Spear");
        monsterList.add("Shield and Spear");
        monsterList.add("Shield and Spear");
        eliteMonsterList = new ArrayList();
        eliteMonsterList.add("Shield and Spear");
        eliteMonsterList.add("Shield and Spear");
        eliteMonsterList.add("Shield and Spear");
    }

    protected void initializeBoss() {
        bossList.add("The Heart");
        bossList.add("The Heart");
        bossList.add("The Heart");
    }

    @Override
    public void Ending() {
        CardCrawlGame.music.fadeOutBGM();
        MapRoomNode node = new MapRoomNode(1, 1);
        node.room = new VictoryRoom(VictoryRoom.EventType.HEART);
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.nextRoomTransitionStart();
    }

//    private void connectNode(MapRoomNode src, MapRoomNode dst) {
//        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
//    }
//
//    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index) {
//        return singleNodeArea(room, index, true);
//    }
//
//    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index, boolean connected) {
//        ArrayList<MapRoomNode> result = new ArrayList<>();
//        MapRoomNode mrn;
//        result.add(new MapRoomNode(0, index));
//        result.add(new MapRoomNode(1, index));
//        result.add(new MapRoomNode(2, index));
//        mrn = new MapRoomNode(3, index);
//        mrn.room = room;
//        result.add(mrn);
//        result.add(new MapRoomNode(4, index));
//        result.add(new MapRoomNode(5, index));
//        result.add(new MapRoomNode(6, index));
//
//        if (connected) {
//            linkNonMonsterAreas(result);
//        }
//
//        return result;
//    }
}