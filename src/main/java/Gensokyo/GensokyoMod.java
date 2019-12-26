package Gensokyo;

import Gensokyo.cards.Apocalypse;
import Gensokyo.cards.BlessingOfConstitution;
import Gensokyo.cards.BlessingOfFortitude;
import Gensokyo.cards.BlessingOfVigor;
import Gensokyo.cards.CrescentMoonSlash;
import Gensokyo.cards.EightFeetTall;
import Gensokyo.cards.Frozen;
import Gensokyo.cards.GapWoman;
import Gensokyo.cards.HAARP;
import Gensokyo.cards.Kunekune;
import Gensokyo.cards.LittleGreenMen;
import Gensokyo.cards.LochNessMonster;
import Gensokyo.cards.ManorOfTheDishes;
import Gensokyo.cards.MarisaTwilightSpark;
import Gensokyo.cards.MenInBlack;
import Gensokyo.cards.MissMary;
import Gensokyo.cards.MonkeysPaw;
import Gensokyo.cards.RedCapeBlueCape;
import Gensokyo.cards.SevenSchoolMysteries;
import Gensokyo.cards.SlitMouthedWoman;
import Gensokyo.cards.SpontaneousHumanCombustion;
import Gensokyo.cards.TekeTeke;
import Gensokyo.cards.TurboGranny;
import Gensokyo.dungeon.EncounterIDs;
import Gensokyo.dungeon.Gensokyo;
import Gensokyo.events.ABanquetForGhosts;
import Gensokyo.events.ACelestialsPlight;
import Gensokyo.events.AHoleInReality;
import Gensokyo.events.ALandWhereOnlyIAmMissing;
import Gensokyo.events.AMomentFractured;
import Gensokyo.events.ASwiftSlash;
import Gensokyo.events.BambooForestOfTheLost;
import Gensokyo.events.ClashOfLegends;
import Gensokyo.events.DemonBookSeller;
import Gensokyo.events.FieldTripToAnotherWorld;
import Gensokyo.events.ForestOfMagic;
import Gensokyo.events.GardenOfTheSun;
import Gensokyo.events.GoddessOfMisfortune;
import Gensokyo.events.GoodsFromTheOutsideWorld;
import Gensokyo.events.HakureiShrine;
import Gensokyo.events.ScarletDevilMansion;
import Gensokyo.events.TheEnmasDilemma;
import Gensokyo.events.ThoseEarthRabbits;
import Gensokyo.events.marisaEvents.AnOldGhost;
import Gensokyo.events.marisaEvents.BookThief;
import Gensokyo.events.marisaEvents.JustAVisit;
import Gensokyo.monsters.Aya;
import Gensokyo.monsters.Cirno;
import Gensokyo.monsters.GreaterFairy;
import Gensokyo.monsters.Kokoro;
import Gensokyo.monsters.Mamizou;
import Gensokyo.monsters.NormalEnemies.CorruptedTreant;
import Gensokyo.monsters.NormalEnemies.GreaterFairyNormal;
import Gensokyo.monsters.NormalEnemies.GreyKodama;
import Gensokyo.monsters.NormalEnemies.Gryphon;
import Gensokyo.monsters.NormalEnemies.Kitsune;
import Gensokyo.monsters.NormalEnemies.LivingMonolith;
import Gensokyo.monsters.NormalEnemies.MaidFairyNormal;
import Gensokyo.monsters.NormalEnemies.MoonRabbit;
import Gensokyo.monsters.NormalEnemies.Python;
import Gensokyo.monsters.NormalEnemies.RedKodama;
import Gensokyo.monsters.NormalEnemies.SunflowerFairyNormal;
import Gensokyo.monsters.NormalEnemies.VengefulSpirit;
import Gensokyo.monsters.NormalEnemies.WhiteKodama;
import Gensokyo.monsters.NormalEnemies.YellowKodama;
import Gensokyo.monsters.NormalEnemies.ZombieFairyNormal;
import Gensokyo.monsters.Reimu;
import Gensokyo.monsters.SunflowerFairy;
import Gensokyo.monsters.Yukari;
import Gensokyo.monsters.ZombieFairy;
import Gensokyo.relics.Bombinomicon;
import Gensokyo.relics.BookOfSpecters;
import Gensokyo.relics.CelestialsFlawlessClothing;
import Gensokyo.relics.Justice;
import Gensokyo.relics.LunaticRedEyes;
import Gensokyo.relics.MarisaIngredientList;
import Gensokyo.relics.Mercy;
import Gensokyo.relics.NagashiBinaDoll;
import Gensokyo.relics.OccultBall;
import Gensokyo.relics.PerfectCherryBlossom;
import Gensokyo.relics.PortableGap;
import Gensokyo.relics.YoukaiFlower;
import Gensokyo.util.IDCheckDontTouchPls;
import Gensokyo.util.TextureLoader;
import Gensokyo.variables.DefaultCustomVariable;
import Gensokyo.variables.DefaultSecondMagicNumber;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

@SpireInitializer
public class GensokyoMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber {

    public static final Logger logger = LogManager.getLogger(GensokyoMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Gensokyo";
    private static final String AUTHOR = "Darkglade";
    private static final String DESCRIPTION = "An alternate Act 1 mod inspired by Touhou Project.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "GensokyoResources/images/Badge.png";

    public static boolean hasMarisa;

    static {
        hasMarisa = Loader.isModLoaded("TS05_Marisa");
    }
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeEffectPath(String resourcePath) {
        return getModID() + "Resources/images/effects/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public GensokyoMod() {
        logger.info("Subscribe to BaseMod hooks");
      
        setModID("Gensokyo");
        
        logger.info("Done subscribing");
        
    }
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = GensokyoMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = GensokyoMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = GensokyoMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    @SuppressWarnings("unused")
    public static void initialize() {
        GensokyoMod gensokyoMod = new GensokyoMod();
        BaseMod.subscribe(gensokyoMod);
    }
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        (new Gensokyo()).addAct(Exordium.ID);

        BaseMod.addMonster(Yukari.ID, (BaseMod.GetMonster)Yukari::new);
        BaseMod.addMonster(Kokoro.ID, (BaseMod.GetMonster)Kokoro::new);
        BaseMod.addMonster(Reimu.ID, (BaseMod.GetMonster)Reimu::new);

        BaseMod.addMonster(Aya.ID, (BaseMod.GetMonster)Aya::new);
        BaseMod.addMonster(Cirno.ID, "Cirno", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new GreaterFairy(-600.0F, 0.0F, null),
                        new SunflowerFairy(-400.0F, 0.0F, null),
                        new ZombieFairy(-200.0F, 0.0F, null),
                        new Cirno()
                }));
        BaseMod.addMonster(Mamizou.ID, (BaseMod.GetMonster)Mamizou::new);

        BaseMod.addMonster(EncounterIDs.KODAMA_2, "2_Kodama", () -> new MonsterGroup(generateKodamaGroup(2)));
        BaseMod.addMonster(EncounterIDs.KODAMA_3, "3_Kodama", () -> new MonsterGroup(generateKodamaGroup(3)));
        BaseMod.addMonster(EncounterIDs.FAIRIES_3, "3_Fairies", () -> new MonsterGroup(generateFairyGroup(3)));
        BaseMod.addMonster(EncounterIDs.FAIRIES_5, "5_Fairies", () -> new MonsterGroup(generateFairyGroup(5)));
        BaseMod.addMonster(VengefulSpirit.ID, (BaseMod.GetMonster)VengefulSpirit::new);
        BaseMod.addMonster(LivingMonolith.ID, (BaseMod.GetMonster)LivingMonolith::new);
        BaseMod.addMonster(CorruptedTreant.ID, (BaseMod.GetMonster)CorruptedTreant::new);
        BaseMod.addMonster(Kitsune.ID, (BaseMod.GetMonster)Kitsune::new);
        BaseMod.addMonster(Python.ID, (BaseMod.GetMonster)Python::new);
        BaseMod.addMonster(Gryphon.ID, (BaseMod.GetMonster)Gryphon::new);
        BaseMod.addMonster(EncounterIDs.RABBITS_2, "2_Rabbits", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new MoonRabbit(-450.0F, 0.0F, MoonRabbit.TANKER),
                        new MoonRabbit(-150.0F, 0.0F, MoonRabbit.BUFFER),
                }));
        BaseMod.addMonster(EncounterIDs.GRYPHON_AND_RABBIT, "Gryphon_and_Rabbit", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Gryphon(-450.0F, 0.0F),
                        new MoonRabbit(-150.0F, 0.0F, MoonRabbit.SHIELDER),
                }));
        BaseMod.addMonster(EncounterIDs.PYTHON_AND_KODAMA, "Python_and_Kodama", () -> new MonsterGroup(generatePythonAndKodomaGroup()));

        BaseMod.addBoss(Gensokyo.ID, Yukari.ID, "GensokyoResources/images/monsters/Yukari/Yukari.png", "GensokyoResources/images/monsters/Yukari/YukariOutline.png");
        BaseMod.addBoss(Gensokyo.ID, Kokoro.ID, "GensokyoResources/images/monsters/Kokoro/Kokoro.png", "GensokyoResources/images/monsters/Kokoro/KokoroOutline.png");
        BaseMod.addBoss(Gensokyo.ID, Reimu.ID, "GensokyoResources/images/monsters/Reimu/Reimu.png", "GensokyoResources/images/monsters/Reimu/ReimuOutline.png");



        
        // =============== EVENTS =================

        BaseMod.addEvent(ScarletDevilMansion.ID, ScarletDevilMansion.class, Gensokyo.ID);
        //BaseMod.addEvent(BorderOfDeath.ID, BorderOfDeath.class, Gensokyo.ID);
        BaseMod.addEvent(TheEnmasDilemma.ID, TheEnmasDilemma.class, Gensokyo.ID);
        BaseMod.addEvent(ACelestialsPlight.ID, ACelestialsPlight.class, Gensokyo.ID);
        BaseMod.addEvent(HakureiShrine.ID, HakureiShrine.class, Gensokyo.ID);
        BaseMod.addEvent(ASwiftSlash.ID, ASwiftSlash.class, Gensokyo.ID);
        BaseMod.addEvent(ThoseEarthRabbits.ID, ThoseEarthRabbits.class, Gensokyo.ID);
        BaseMod.addEvent(FieldTripToAnotherWorld.ID, FieldTripToAnotherWorld.class, Gensokyo.ID);
        BaseMod.addEvent(AHoleInReality.ID, AHoleInReality.class, Gensokyo.ID);
        BaseMod.addEvent(GoodsFromTheOutsideWorld.ID, GoodsFromTheOutsideWorld.class, Gensokyo.ID);
        BaseMod.addEvent(ForestOfMagic.ID, ForestOfMagic.class, Gensokyo.ID);
        BaseMod.addEvent(GoddessOfMisfortune.ID, GoddessOfMisfortune.class, Gensokyo.ID);
        BaseMod.addEvent(ClashOfLegends.ID, ClashOfLegends.class, Gensokyo.ID);
        BaseMod.addEvent(ALandWhereOnlyIAmMissing.ID, ALandWhereOnlyIAmMissing.class, Gensokyo.ID);
        BaseMod.addEvent(BambooForestOfTheLost.ID, BambooForestOfTheLost.class, Gensokyo.ID);
        BaseMod.addEvent(GardenOfTheSun.ID, GardenOfTheSun.class, Gensokyo.ID);
        BaseMod.addEvent(DemonBookSeller.ID, DemonBookSeller.class, Gensokyo.ID);
        BaseMod.addEvent(ABanquetForGhosts.ID, ABanquetForGhosts.class, Gensokyo.ID);
        BaseMod.addEvent(AMomentFractured.ID, AMomentFractured.class, Gensokyo.ID);

        if (hasMarisa) {
            BaseMod.addEvent(AnOldGhost.ID, AnOldGhost.class, Gensokyo.ID);
            BaseMod.addEvent(JustAVisit.ID, JustAVisit.class, Gensokyo.ID);
            BaseMod.addEvent(BookThief.ID, BookThief.class, Gensokyo.ID);
        }
        
        // =============== /EVENTS/ =================

        logger.info("Done loading badge Image and mod options");
    }

    private AbstractMonster[] generateKodamaGroup(int groupSize) {
        if (groupSize != 2 && groupSize != 3) {
            groupSize = 3; //default to 3
        }
        float[] groupPositionsSize2 = {-450.0F, -150.0F};
        float[] groupPositionsSize3 = {-550.0F, -300.0F, -50.0F};
        ArrayList<Integer> monstersList = new ArrayList<>();
        monstersList.add(1);
        monstersList.add(2);
        monstersList.add(3);
        monstersList.add(4);
        Collections.shuffle(monstersList, AbstractDungeon.monsterRng.random);
        float[] groupToUse;
        AbstractMonster[] monsters = new AbstractMonster[groupSize];
        if (groupSize == 2) {
            groupToUse = groupPositionsSize2;
        } else {
            groupToUse = groupPositionsSize3;
        }
        for (int i = 0; i < groupSize; i++) {
            if (monstersList.get(i) == 1) {
                monsters[i] = new RedKodama(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 2) {
                monsters[i] = new GreyKodama(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 3) {
                monsters[i] = new YellowKodama(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 4) {
                monsters[i] = new WhiteKodama(groupToUse[i], 0.0F);
            }
        }

        return monsters;
    }

    private AbstractMonster[] generateFairyGroup(int groupSize) {
        if (groupSize != 3 && groupSize != 5) {
            groupSize = 5; //default to 5
        }
        float[] groupPositionsSize3 = {-550.0F, -300.0F, -50.0F};
        float[] groupPositionsSize5 = {-600.0F, -450.0F, -300.0F, -150.0F, 0.0F};
        ArrayList<Integer> monstersList = new ArrayList<>();
        monstersList.add(1);
        monstersList.add(2);
        monstersList.add(3);
        monstersList.add(4);
        if (groupSize == 5) {
            monstersList.add(AbstractDungeon.monsterRng.random(1, 4)); //adds a randomly chosen duplicate for the 5th fairy
        }
        Collections.shuffle(monstersList, AbstractDungeon.monsterRng.random);
        float[] groupToUse;
        AbstractMonster[] monsters = new AbstractMonster[groupSize];
        if (groupSize == 5) {
            groupToUse = groupPositionsSize5;
        } else {
            groupToUse = groupPositionsSize3;
        }
        for (int i = 0; i < groupSize; i++) {
            if (monstersList.get(i) == 1) {
                monsters[i] = new GreaterFairyNormal(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 2) {
                monsters[i] = new SunflowerFairyNormal(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 3) {
                monsters[i] = new ZombieFairyNormal(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 4) {
                monsters[i] = new MaidFairyNormal(groupToUse[i], 0.0F);
            }
        }

        return monsters;
    }

    private AbstractMonster[] generatePythonAndKodomaGroup() {
        int groupSize = 2;
        float[] groupPositionsSize2 = {-450.0F, -150.0F};
        ArrayList<Integer> monstersList = new ArrayList<>();
        monstersList.add(1);
        monstersList.add(2);
        monstersList.add(3);
        Collections.shuffle(monstersList, AbstractDungeon.monsterRng.random);
        float[] groupToUse;
        AbstractMonster[] monsters = new AbstractMonster[groupSize];
        groupToUse = groupPositionsSize2;
        monsters[0] = new Python(groupToUse[0], 0.0F);
        for (int i = 1; i < groupSize; i++) {
            if (monstersList.get(i) == 1) {
                monsters[i] = new RedKodama(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 2) {
                monsters[i] = new GreyKodama(groupToUse[i], 0.0F);
            } else if (monstersList.get(i) == 3) {
                monsters[i] = new YellowKodama(groupToUse[i], 0.0F);
            }
        }

        return monsters;
    }
    
    // =============== / POST-INITIALIZE/ =================
    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("Gensokyo:Train", makeEffectPath("TrainSFX.ogg"));
    }
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        BaseMod.addRelic(new PerfectCherryBlossom(), RelicType.SHARED);
        BaseMod.addRelic(new Mercy(), RelicType.SHARED);
        BaseMod.addRelic(new Justice(), RelicType.SHARED);
        BaseMod.addRelic(new CelestialsFlawlessClothing(), RelicType.SHARED);
        BaseMod.addRelic(new OccultBall(), RelicType.SHARED);
        BaseMod.addRelic(new LunaticRedEyes(), RelicType.SHARED);
        BaseMod.addRelic(new PortableGap(), RelicType.SHARED);
        BaseMod.addRelic(new NagashiBinaDoll(), RelicType.SHARED);
        BaseMod.addRelic(new YoukaiFlower(), RelicType.SHARED);
        BaseMod.addRelic(new Bombinomicon(), RelicType.SHARED);
        BaseMod.addRelic(new BookOfSpecters(), RelicType.SHARED);

        if (hasMarisa) {
            BaseMod.addRelic(new MarisaIngredientList(), RelicType.SHARED);
        }

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        pathCheck();
        logger.info("Added variables");

        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        BaseMod.addCard(new CrescentMoonSlash());
        BaseMod.addCard(new Frozen());

        //Urban Legends
        BaseMod.addCard(new MissMary());
        BaseMod.addCard(new SpontaneousHumanCombustion());
        BaseMod.addCard(new RedCapeBlueCape());
        BaseMod.addCard(new TekeTeke());
        BaseMod.addCard(new Kunekune());
        BaseMod.addCard(new HAARP());
        BaseMod.addCard(new MenInBlack());
        BaseMod.addCard(new LittleGreenMen());
        BaseMod.addCard(new TurboGranny());
        BaseMod.addCard(new MonkeysPaw());
        BaseMod.addCard(new ManorOfTheDishes());
        BaseMod.addCard(new LochNessMonster());
        BaseMod.addCard(new GapWoman());
        BaseMod.addCard(new EightFeetTall());
        BaseMod.addCard(new SevenSchoolMysteries());
        BaseMod.addCard(new Apocalypse());
        BaseMod.addCard(new SlitMouthedWoman());

        if (hasMarisa) {
            BaseMod.addCard(new MarisaTwilightSpark());
            BaseMod.addCard(new BlessingOfVigor());
            BaseMod.addCard(new BlessingOfFortitude());
            BaseMod.addCard(new BlessingOfConstitution());
        }
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Event-Strings.json");

        //Monster Strings
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Monster-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-ui.json");
        
        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/GensokyoMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
