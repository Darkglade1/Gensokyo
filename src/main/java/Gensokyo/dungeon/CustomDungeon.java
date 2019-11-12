package Gensokyo.dungeon;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomDungeon extends AbstractDungeon {
    public static CustomDungeon datasource;

    public String name;
    public String id;

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

    public String eventImg;

    public CustomDungeon(AbstractScene scene, String NAME, String ID) {
        this(scene, NAME, ID, "images/ui/event/panel.png", false);
    }
    public CustomDungeon(AbstractScene scene, String NAME, String ID, boolean genericEvents) {
        this(scene, NAME, ID, "images/ui/event/panel.png", genericEvents);
    }
    public CustomDungeon(AbstractScene scene, String NAME, String ID, String eventImg) {
        this(scene, NAME, ID, eventImg, false);
    }
    public CustomDungeon(AbstractScene scene, String NAME, String ID, String eventImg, boolean genericEvents) {
        this(scene, NAME, ID, eventImg, genericEvents, 3, 12, 10);
    }
    public CustomDungeon(AbstractScene scene, String NAME, String ID, String eventImg, boolean genericEvents, int weakpreset, int strongpreset, int elitepreset) {
        super(NAME, ID, AbstractDungeon.player, new ArrayList<>());
        this.id = ID;
        this.name = NAME;
        this.savedScene = scene;
        this.eventImg = eventImg;
        this.genericEvents = genericEvents;
        this.savedFadeColor = Color.valueOf("0f220aff");
        this.finalAct = false;

        this.weakpreset = weakpreset;
        this.strongpreset = strongpreset;
        this.elitepreset = elitepreset;

        if(AbstractDungeon.actNum > 0) {
            setupMisc(this, AbstractDungeon.actNum);
        }
    }

    //Constructor for when you encounter this act through progression.
    public CustomDungeon(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd.name, cd.id, p, emptyList);

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
    public CustomDungeon(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd.name, p, saveFile);

        CardCrawlGame.dungeon = this;
        setupMisc(cd, saveFile.act_num);

        if(AbstractDungeon.lastCombatMetricKey == null) {
            AbstractDungeon.lastCombatMetricKey = "";
        }

        miscRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + saveFile.floor_num);
        firstRoomChosen = true;

        populatePathTaken(saveFile);
    }

    public void setupMisc(CustomDungeon cd, int actNum) {
        //Copying data from the instance that was used for initialization.
        if (scene != null && scene != cd.savedScene) {
            scene.dispose();
        }
        scene = cd.savedScene;
        fadeColor = cd.savedFadeColor;
        this.name = cd.name;
        //event bg needs to be set here, because it can't be set when the constructor of AbstractDungeon is executed yet.
        AbstractDungeon.eventBackgroundImg = ImageMaster.loadImage(cd.eventImg);
        initializeLevelSpecificChances();
        mapRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + actNum * 100);
        generateMap();

        if(cd.mainmusic != null) {
            CardCrawlGame.music.changeBGM(cd.id);
        } else {
            switch(actNum) {
                case EXORDIUM:
                    CardCrawlGame.music.changeBGM(Exordium.ID);
                    break;
                case THECITY:
                    CardCrawlGame.music.changeBGM(TheCity.ID);
                    break;
                case THEBEYOND:
                    CardCrawlGame.music.changeBGM(TheBeyond.ID);
                    break;
                case THEENDING:
                    CardCrawlGame.music.changeBGM(TheEnding.ID);
                    break;
            }
        }
    }

    //Use of Reflection allows for instantiation, only requiring the 3 simple, mandatory constructors.
    public CustomDungeon fromProgression(AbstractPlayer p) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        datasource = this;
        return this.getClass().getConstructor(CustomDungeon.class, AbstractPlayer.class, ArrayList.class)
                .newInstance(this, p, this.genericEvents  ? AbstractDungeon.specialOneTimeEventList : new ArrayList<>());
    }
    public CustomDungeon fromSaveFile(AbstractPlayer p, SaveFile saveFile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        datasource = this;
        return this.getClass().getConstructor(CustomDungeon.class, AbstractPlayer.class, SaveFile.class).newInstance(this, p, saveFile);
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
    protected void initializeBoss() {
        bossList.clear();
        // Bosses are added via BaseMod in MyAct.receivePostInitialize()
    }

    @Override
    protected void initializeEventList() {
        // Events are added via BaseMod in MyAct.receivePostInitialize()
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
    public static Map<String, CustomDungeon> dungeons = new HashMap<>();

    //Give this the ID of a basegame act, and your act, and it'll register it.
    public static void addAct(String replaces, CustomDungeon cd) {
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
                    BaseMod.logger.error("Unable to add act \"" + cd.id + "\".");
                    return;
                }
        }
        addAct(actReplacement, cd);
    }
    //Works with just a number, too. Exordium is 1 in this case.
    public static void addAct(int actReplacement, CustomDungeon cd) {
        if(!dungeons.containsKey(cd.id)) {
            if(!actnumbers.containsKey(actReplacement)) {
                actnumbers.put(actReplacement, new ArrayList<>());
                actnumbers.get(actReplacement).add(cd.id);
            }
            dungeons.put(cd.id, cd);
        } else {
            BaseMod.logger.error("Act \"" + cd.id + "\" already present.");
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