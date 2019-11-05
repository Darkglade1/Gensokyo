package Gensokyo.dungeon;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.scenes.TheBottomScene;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gensokyo extends AbstractDungeon {
    public static Gensokyo datasource;

    public static String ID = "Gensokyo:Gensokyo";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public static final String NUM = TEXT[1];

    public int weakpreset;
    public int strongpreset;
    public int elitepreset;
    protected boolean genericEvents;
    protected AbstractScene savedScene;
    protected Color savedFadeColor;
    public boolean finalAct;
    private Class<? extends AbstractEvent> onEnter = null;
    public boolean hasEvent() {
        return onEnter != null;
    }

    private String eventImg;

    public Gensokyo() {
        super(NAME, ID, AbstractDungeon.player, new ArrayList<>());
        this.name = NAME;
        scene = new TheBottomScene();
        scene.randomizeScene();
        this.savedScene = scene;
        this.eventImg = "images/ui/event/panel.png";
        this.genericEvents = true;
        this.savedFadeColor = Color.valueOf("0f220aff");
        this.finalAct = false;

        this.weakpreset = 3;
        this.strongpreset = 12;
        this.elitepreset = 10;

        if(AbstractDungeon.actNum > 0) {
            setupMisc(this, AbstractDungeon.actNum);
        }
    }

    //Constructor for when you encounter this act through progression.
    public Gensokyo(Gensokyo cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(NAME, ID, p, emptyList);
        setupMisc(cd, AbstractDungeon.actNum);


        AbstractDungeon.currMapNode = new MapRoomNode(0, -1);

        if(cd.onEnter != null || AbstractDungeon.floorNum < 1) {
            try {
                if(cd.onEnter == null) {
                    throw new ArithmeticException();
                }
                //Set the starting event
                AbstractEvent ae = cd.onEnter.newInstance();

                AbstractDungeon.currMapNode.room = new EventRoom();
                AbstractDungeon.currMapNode.room.event = ae;
                AbstractDungeon.overlayMenu.proceedButton.hide();
                //If you try entering the room, it sets it to a random event, so just call onEnterRoom on the event instead.
                ae.onEnterRoom();
            } catch(Exception ex) {
                if(!(ex instanceof ArithmeticException)) {
                    ex.printStackTrace();
                }
                //Default Neow event.
                AbstractDungeon.currMapNode.room = new NeowRoom(false);
            }
            AbstractDungeon.rs = RenderScene.EVENT;
            AbstractDungeon.screen = CurrentScreen.NONE;
            AbstractDungeon.isScreenUp = false;
            AbstractDungeon.previousScreen = null;
        } else {
            AbstractDungeon.currMapNode.room = new EmptyRoom();
        }
    }
    //Constructor for when you load this act from a savefile.
    public Gensokyo(Gensokyo cd, AbstractPlayer p, SaveFile saveFile) {
        super(NAME, p, saveFile);
        CardCrawlGame.dungeon = this;
        setupMisc(cd, saveFile.act_num);

        if(AbstractDungeon.lastCombatMetricKey == null) {
            AbstractDungeon.lastCombatMetricKey = "";
        }

        miscRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + saveFile.floor_num);
        firstRoomChosen = true;

        populatePathTaken(saveFile);
    }
    private void setupMisc(Gensokyo cd, int actNum) {
        //Copying data from the instance that was used for initialization.
        if (scene != null && scene != cd.savedScene) {
            scene.dispose();
        }
        scene = new TheBottomScene();
        scene.randomizeScene();
        fadeColor = cd.savedFadeColor;
        //event bg needs to be set here, because it can't be set when the constructor of AbstractDungeon is executed yet.
        AbstractDungeon.eventBackgroundImg = ImageMaster.loadImage(cd.eventImg);
        initializeLevelSpecificChances();
        mapRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + actNum * 100);
        generateMap();

        if(cd.mainmusic != null) {
            CardCrawlGame.music.changeBGM(cd.ID);
        } else {
//            switch(actNum) {
//                case EXORDIUM:
//                    CardCrawlGame.music.changeBGM(Exordium.ID);
//                    break;
//                case THECITY:
//                    CardCrawlGame.music.changeBGM(TheCity.ID);
//                    break;
//                case THEBEYOND:
//                    CardCrawlGame.music.changeBGM(TheBeyond.ID);
//                    break;
//                case THEENDING:
//                    CardCrawlGame.music.changeBGM(TheEnding.ID);
//                    break;
//            }
        }
    }

    //Use of Reflection allows for instantiation, only requiring the 3 simple, mandatory constructors.
    public Gensokyo fromProgression(AbstractPlayer p) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        datasource = this;
        return this.getClass().getConstructor(Gensokyo.class, AbstractPlayer.class, ArrayList.class)
                .newInstance(this, p, this.genericEvents  ? AbstractDungeon.specialOneTimeEventList : new ArrayList<>());
    }
    public Gensokyo fromSaveFile(AbstractPlayer p, SaveFile saveFile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        datasource = this;
        return this.getClass().getConstructor(Gensokyo.class, AbstractPlayer.class, SaveFile.class).newInstance(this, p, saveFile);
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        cardUpgradedChance = 0.0F;
    }

    //Flag determining if this act requires the 3 keys (if it's at or later than The Ending).
    public void isFinalAct(boolean fin) {
        this.finalAct = fin;
    }
    //Event that is executed at the start of the act.
    public void onEnterEvent(Class<? extends AbstractEvent> event) {
        this.onEnter = event;
    }


    @Override
    protected void generateMonsters() {
        generateWeakEnemies(weakpreset);
        generateStrongEnemies(strongpreset);
        generateElites(elitepreset);
    }

    @Override
    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo("Cultist", 2.0F));
        monsters.add(new MonsterInfo("Jaw Worm", 2.0F));
        monsters.add(new MonsterInfo("2 Louse", 2.0F));
        monsters.add(new MonsterInfo("Small Slimes", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo("Blue Slaver", 2.0F));
        monsters.add(new MonsterInfo("Gremlin Gang", 1.0F));
        monsters.add(new MonsterInfo("Looter", 2.0F));
        monsters.add(new MonsterInfo("Large Slime", 2.0F));
        monsters.add(new MonsterInfo("Lots of Slimes", 1.0F));
        monsters.add(new MonsterInfo("Exordium Thugs", 1.5F));
        monsters.add(new MonsterInfo("Exordium Wildlife", 1.5F));
        monsters.add(new MonsterInfo("Red Slaver", 1.0F));
        monsters.add(new MonsterInfo("3 Louse", 2.0F));
        monsters.add(new MonsterInfo("2 Fungi Beasts", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo("Gremlin Nob", 1.0F));
        monsters.add(new MonsterInfo("Lagavulin", 1.0F));
        monsters.add(new MonsterInfo("3 Sentries", 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }
    @Override
    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList();
        String var2 = monsterList.get(monsterList.size() - 1);
        byte var3 = -1;
        switch(var2.hashCode()) {
            case -2013219467:
                if (var2.equals("Looter")) {
                    var3 = 0;
                }
                break;
            case -1879712874:
                if (var2.equals("2 Louse")) {
                    var3 = 4;
                }
                break;
            case -1508851536:
                if (var2.equals("Cultist")) {
                    var3 = 2;
                }
                break;
            case -548386477:
                if (var2.equals("Jaw Worm")) {
                    var3 = 1;
                }
                break;
            case 70731812:
                if (var2.equals("Small Slimes")) {
                    var3 = 5;
                }
                break;
            case 1637395457:
                if (var2.equals("Blue Slaver")) {
                    var3 = 3;
                }
        }

        switch(var3) {
            case 0:
                retVal.add("Exordium Thugs");
            case 1:
            case 2:
            default:
                break;
            case 3:
                retVal.add("Red Slaver");
                retVal.add("Exordium Thugs");
                break;
            case 4:
                retVal.add("3 Louse");
                break;
            case 5:
                retVal.add("Large Slime");
                retVal.add("Lots of Slimes");
        }

        return retVal;
    }

    @Override
    protected void initializeBoss() {
        bossList.clear();
        // Bosses are added via BaseMod in GensokyoMod.receivePostInitialize()
    }

    @Override
    protected void initializeEventList() {
        // Events are added via BaseMod in GensokyoMod.receivePostInitialize()
    }

    @Override
    protected void initializeShrineList() {
        //No shrines
    }

    @Override
    public void initializeSpecialOneTimeEventList() {
        //None of these either
        System.out.println(eventList);
        specialOneTimeEventList.clear();
        System.out.println(eventList);
    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }
        eventBackgroundImg = ImageMaster.loadImage(eventImg);
    }

    //The main datafields this mod uses.
    public static Map<Integer, ArrayList<String>> actnumbers = new HashMap<>();
    public static Map<String, Gensokyo> dungeons = new HashMap<>();

    //Give this the ID of a basegame act, and your act, and it'll register it.
    public static void addAct(String replaces, Gensokyo cd) {
        int actReplacement;
        switch (replaces) {
            case Exordium.ID:
                actReplacement = EXORDIUM;
                break;
            case TheCity.ID:
                actReplacement = THECITY;
                break;
            case TheBeyond.ID:
                actReplacement = THEBEYOND;
                break;
            case TheEnding.ID:
                actReplacement = THEENDING;
                break;

            default:
                if(replaces.matches("\\d")) {
                    actReplacement = Integer.parseInt(replaces);
                } else {
                    BaseMod.logger.error("Unable to add act \"" + cd.ID + "\".");
                    return;
                }
        }
        addAct(actReplacement, cd);
    }
    //Works with just a number, too. Exordium is 1 in this case.
    public static void addAct(int actReplacement, Gensokyo cd) {
        if(!dungeons.containsKey(Gensokyo.ID)) {
            if(!actnumbers.containsKey(actReplacement)) {
                actnumbers.put(actReplacement, new ArrayList<>());
                actnumbers.get(actReplacement).add(Gensokyo.ID);
            }
            dungeons.put(Gensokyo.ID, cd);
        } else {
            BaseMod.logger.error("Act \"" + Gensokyo.ID + "\" already present.");
        }
    }
    //Both the above functions can be called as object methods as well.
    public void addAct(int actReplacement) {
        addAct(actReplacement, this);
    }
    public void addAct(String replaces) {
        addAct(replaces, this);
    }


    public static final int EXORDIUM = 1;
    public static final int THECITY = 2;
    public static final int THEBEYOND = 3;
    public static final int THEENDING = 4;


    //Very simple music functionality.
    public String mainmusic = null;
    public static Map<String, String> tempmusic = new HashMap<>();

    public void setMainMusic(String path) {
        mainmusic = path;
    }
    public void addTempMusic(String key, String path) {
        if(tempmusic.containsKey(key)) {
            BaseMod.logger.error("Temp Music key \"" + key + "\" already taken!");
        } else {
            BaseMod.logger.error("Adding Temp Music key: \"" + key + "\"");
            tempmusic.put(key, path);
        }
    }
}