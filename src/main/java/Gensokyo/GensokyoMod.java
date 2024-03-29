package Gensokyo;

import Gensokyo.CustomIntents.AreaAttackIntent;
import Gensokyo.CustomIntents.CurseAttackIntent;
import Gensokyo.CustomIntents.DeathIntent;
import Gensokyo.CustomIntents.PurifyIntent;
import Gensokyo.cards.AllTheWorldsEvil;
import Gensokyo.cards.Butterfly;
import Gensokyo.cards.CustomPotion;
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
import Gensokyo.cards.GreaterSplit;
import Gensokyo.cards.InfectedWound;
import Gensokyo.cards.Item.LifeforceConverter;
import Gensokyo.cards.Item.MedicineKit;
import Gensokyo.cards.Item.OverchargedCore;
import Gensokyo.cards.Item.RPG;
import Gensokyo.cards.Item.ReactiveArmor;
import Gensokyo.cards.Item.Taser;
import Gensokyo.cards.Lunar.BrilliantDragonBullet;
import Gensokyo.cards.Lunar.BuddhistDiamond;
import Gensokyo.cards.Lunar.Dawn;
import Gensokyo.cards.Lunar.DreamlikeParadise;
import Gensokyo.cards.Lunar.EverlastingLife;
import Gensokyo.cards.Lunar.HouraiInAPot;
import Gensokyo.cards.Lunar.LifeSpringInfinity;
import Gensokyo.cards.Lunar.MorningMist;
import Gensokyo.cards.Lunar.MorningStar;
import Gensokyo.cards.Lunar.NewMoon;
import Gensokyo.cards.Lunar.RainbowDanmaku;
import Gensokyo.cards.Lunar.RisingWorld;
import Gensokyo.cards.Lunar.SalamanderShield;
import Gensokyo.cards.Lunar.UnhurriedMind;
import Gensokyo.cards.Lunar.UnlikelyAid;
import Gensokyo.cards.Pets.SummonFieryMouse;
import Gensokyo.cards.Pets.SummonJeweledCobra;
import Gensokyo.cards.Pets.SummonPsychicCat;
import Gensokyo.cards.PrivateSquare;
import Gensokyo.cards.UrbanLegend.Apocalypse;
import Gensokyo.cards.BlessingOfConstitution;
import Gensokyo.cards.BlessingOfFortitude;
import Gensokyo.cards.BlessingOfVigor;
import Gensokyo.cards.CrescentMoonSlash;
import Gensokyo.cards.UrbanLegend.Doppelganger;
import Gensokyo.cards.UrbanLegend.EightFeetTall;
import Gensokyo.cards.EmbersOfLove;
import Gensokyo.cards.Frozen;
import Gensokyo.cards.UrbanLegend.GapWoman;
import Gensokyo.cards.UrbanLegend.HAARP;
import Gensokyo.cards.ImpossibleRequests.ImpossibleRequest;
import Gensokyo.cards.UrbanLegend.Kunekune;
import Gensokyo.cards.UrbanLegend.LittleGreenMen;
import Gensokyo.cards.UrbanLegend.LochNessMonster;
import Gensokyo.cards.UrbanLegend.ManorOfTheDishes;
import Gensokyo.cards.MarisaTwilightSpark;
import Gensokyo.cards.UrbanLegend.MenInBlack;
import Gensokyo.cards.MindShatter;
import Gensokyo.cards.UrbanLegend.MissMary;
import Gensokyo.cards.UrbanLegend.MonkeysPaw;
import Gensokyo.cards.Pets.SummonYinYangFox;
import Gensokyo.cards.Philosophy;
import Gensokyo.cards.UrbanLegend.RedCapeBlueCape;
import Gensokyo.cards.ReflexRadar;
import Gensokyo.cards.UrbanLegend.SevenSchoolMysteries;
import Gensokyo.cards.ShootingStar;
import Gensokyo.cards.UrbanLegend.SlitMouthedWoman;
import Gensokyo.cards.UrbanLegend.SpontaneousHumanCombustion;
import Gensokyo.cards.FourOfAKind;
import Gensokyo.cards.UrbanLegend.TekeTeke;
import Gensokyo.cards.UrbanLegend.TurboGranny;
import Gensokyo.dungeon.EncounterIDs;
import Gensokyo.dungeon.Gensokyo;
import Gensokyo.dungeon.Gensokyoer;
import Gensokyo.dungeon.Gensokyoest;
import Gensokyo.events.act1.ABanquetForGhosts;
import Gensokyo.events.act1.ACelestialsPlight;
import Gensokyo.events.act1.AHoleInReality;
import Gensokyo.events.act1.ALandWhereOnlyIAmMissing;
import Gensokyo.events.act1.AMomentFractured;
import Gensokyo.events.act1.ASwiftSlash;
import Gensokyo.events.act1.BambooForestOfTheLost;
import Gensokyo.events.act2.ClashOfLegends;
import Gensokyo.events.act1.DemonBookSeller;
import Gensokyo.events.act1.FieldTripToAnotherWorld;
import Gensokyo.events.act1.ForestOfMagic;
import Gensokyo.events.act1.GardenOfTheSun;
import Gensokyo.events.act1.GoddessOfMisfortune;
import Gensokyo.events.act1.GoodsFromTheOutsideWorld;
import Gensokyo.events.act1.HakureiShrine;
import Gensokyo.events.act1.ScarletDevilMansion;
import Gensokyo.events.act1.TheEnmasDilemma;
import Gensokyo.events.act1.ThoseEarthRabbits;
import Gensokyo.events.act1.marisaEvents.AHazardousHobby;
import Gensokyo.events.act1.marisaEvents.AnOldGhost;
import Gensokyo.events.act1.marisaEvents.BookThief;
import Gensokyo.events.act1.marisaEvents.JustAVisit;
import Gensokyo.events.act1.marisaEvents.Walpurgisnacht;
import Gensokyo.events.act2.AHistoryOfViolence;
import Gensokyo.events.act2.AndThenThereWereNone;
import Gensokyo.events.act2.Boo;
import Gensokyo.events.act2.ChildOfMiare;
import Gensokyo.events.act2.DetectiveSatori;
import Gensokyo.events.act2.ExtraExtra;
import Gensokyo.events.act2.FiresOfInvention;
import Gensokyo.events.act2.Impoverished;
import Gensokyo.events.act2.LivingGodOfMiracles;
import Gensokyo.events.act2.NightmareOfHeian;
import Gensokyo.events.act2.NohDance;
import Gensokyo.events.act2.OneWingedWhiteHeron;
import Gensokyo.events.act2.PhantomEnsemble;
import Gensokyo.events.act2.TheWhiteLotus;
import Gensokyo.events.act2.TreasureHunter;
import Gensokyo.events.act2.VillageOfCats;
import Gensokyo.events.act3.BloodForBlood;
import Gensokyo.events.act3.IAmNotThere;
import Gensokyo.events.act3.KappaWarehouse;
import Gensokyo.events.act3.MedicineSeller;
import Gensokyo.events.act3.OwlFriend;
import Gensokyo.events.act3.PrincessOfTheMoon;
import Gensokyo.events.act3.ProbabilitySpaceHypervessel;
import Gensokyo.events.act3.ProphetOfDisaster;
import Gensokyo.events.act3.SomeoneElsesStory;
import Gensokyo.events.act3.TheDreamer;
import Gensokyo.events.act3.ThoseWhoFightMonsters;
import Gensokyo.monsters.act1.Aya;
import Gensokyo.monsters.act1.Cirno;
import Gensokyo.monsters.act1.GreaterFairy;
import Gensokyo.monsters.act1.Kokoro;
import Gensokyo.monsters.act1.Mamizou;
import Gensokyo.monsters.act1.NormalEnemies.CorruptedTreant;
import Gensokyo.monsters.act1.NormalEnemies.GreaterFairyNormal;
import Gensokyo.monsters.act1.NormalEnemies.GreyKodama;
import Gensokyo.monsters.act1.NormalEnemies.Gryphon;
import Gensokyo.monsters.act1.NormalEnemies.Kitsune;
import Gensokyo.monsters.act1.NormalEnemies.LivingMonolith;
import Gensokyo.monsters.act1.NormalEnemies.MaidFairyNormal;
import Gensokyo.monsters.act1.NormalEnemies.MoonRabbit;
import Gensokyo.monsters.act1.NormalEnemies.Python;
import Gensokyo.monsters.act1.NormalEnemies.RedKodama;
import Gensokyo.monsters.act1.NormalEnemies.SunflowerFairyNormal;
import Gensokyo.monsters.act1.NormalEnemies.VengefulSpirit;
import Gensokyo.monsters.act1.NormalEnemies.WhiteKodama;
import Gensokyo.monsters.act1.NormalEnemies.YellowKodama;
import Gensokyo.monsters.act1.NormalEnemies.ZombieFairyNormal;
import Gensokyo.monsters.act1.Reimu;
import Gensokyo.monsters.act1.SunflowerFairy;
import Gensokyo.monsters.act1.Yukari;
import Gensokyo.monsters.act1.ZombieFairy;
import Gensokyo.monsters.act2.Byakuren;
import Gensokyo.monsters.act2.Eiki;
import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.monsters.act2.Koishi;
import Gensokyo.monsters.act2.Miko;
import Gensokyo.monsters.act2.NormalEnemies.AngelMirror;
import Gensokyo.monsters.act2.NormalEnemies.BigMudSlime;
import Gensokyo.monsters.act2.NormalEnemies.Chomper;
import Gensokyo.monsters.act2.NormalEnemies.CosmicMonolith;
import Gensokyo.monsters.act2.NormalEnemies.Gloop;
import Gensokyo.monsters.act2.NormalEnemies.SlimeBunny;
import Gensokyo.monsters.act2.NormalEnemies.Swordslinger;
import Gensokyo.monsters.act2.NormalEnemies.TanukiDog;
import Gensokyo.monsters.act2.NormalEnemies.Wraith;
import Gensokyo.monsters.act2.Reisen;
import Gensokyo.monsters.act2.Tenshi;
import Gensokyo.monsters.act3.Kasen;
import Gensokyo.monsters.act3.Marisa;
import Gensokyo.monsters.act3.Mokou;
import Gensokyo.monsters.act3.NormalEnemies.AncientGuardian;
import Gensokyo.monsters.act3.NormalEnemies.AtlasGolem;
import Gensokyo.monsters.act3.NormalEnemies.Dawnsword;
import Gensokyo.monsters.act3.NormalEnemies.Duskaxe;
import Gensokyo.monsters.act3.NormalEnemies.FeralBat;
import Gensokyo.monsters.act3.NormalEnemies.LoudBat;
import Gensokyo.monsters.act3.NormalEnemies.MadBoulder;
import Gensokyo.monsters.act3.NormalEnemies.Rafflesia;
import Gensokyo.monsters.act3.NormalEnemies.SeedOfUnknown;
import Gensokyo.monsters.act3.NormalEnemies.Sharpion;
import Gensokyo.monsters.act3.NormalEnemies.VampireBat;
import Gensokyo.monsters.act3.Shinki.Shinki;
import Gensokyo.monsters.act3.Yuyuko;
import Gensokyo.monsters.act3.Doremy;
import Gensokyo.monsters.act3.Flandre;
import Gensokyo.monsters.act3.Remilia;
import Gensokyo.relics.Companionship;
import Gensokyo.relics.act1.Bombinomicon;
import Gensokyo.relics.act1.BookOfSpecters;
import Gensokyo.relics.act1.CelestialsFlawlessClothing;
import Gensokyo.relics.act1.Justice;
import Gensokyo.relics.act1.LunaticRedEyes;
import Gensokyo.relics.act1.Mercy;
import Gensokyo.relics.act1.NagashiBinaDoll;
import Gensokyo.relics.act1.OccultBall;
import Gensokyo.relics.act1.PerfectCherryBlossom;
import Gensokyo.relics.act1.PortableGap;
import Gensokyo.relics.act1.YoukaiFlower;
import Gensokyo.relics.act1.marisaRelics.ImprobabilityPotion;
import Gensokyo.relics.act1.marisaRelics.IngredientList;
import Gensokyo.relics.act1.marisaRelics.PhilosophersStone;
import Gensokyo.relics.act2.ChorusOfJoy;
import Gensokyo.relics.act2.ConquerorOfFear;
import Gensokyo.relics.act2.DayTheSeaSplit;
import Gensokyo.relics.act2.DemonMask;
import Gensokyo.relics.act2.DirgeOfMelancholy;
import Gensokyo.relics.act2.FoxMask;
import Gensokyo.relics.act2.LionMask;
import Gensokyo.relics.act2.MaskOfHope;
import Gensokyo.relics.act2.MosesMiracle;
import Gensokyo.relics.act2.RedStar;
import Gensokyo.relics.act2.SongOfSouls;
import Gensokyo.relics.act2.SpiderMask;
import Gensokyo.relics.act2.UndefinedDarkness;
import Gensokyo.relics.act3.DualWield;
import Gensokyo.relics.act3.EmptyGrave;
import Gensokyo.relics.act3.TheCrow;
import Gensokyo.rooms.nitori.helpers.gensokyoRelicHelper;
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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import actlikeit.RazIntent.CustomIntent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final String DESCRIPTION = "An alternate Act 1, 2, and 3 mod inspired by Touhou Project.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "GensokyoResources/images/Badge.png";

    public static class Enums {
        @SpireEnum(name = "Urban Legend") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor URBAN_LEGEND;
        @SpireEnum(name = "Urban Legend") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;

        @SpireEnum(name = "Shop Special") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor SHOP_SPECIAL;
        @SpireEnum(name = "Shop Special") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType SHOP_LIBRARY_COLOR;

        @SpireEnum(name = "Lunar") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor LUNAR;
        @SpireEnum(name = "Lunar") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LUNAR_LIBRARY_COLOR;
    }

    public static final Color URBAN_LEGEND = CardHelper.getColor(0, 0, 0);
    public static final Color SHOP_SPECIAL = CardHelper.getColor(0, 255, 0);
    public static final Color LUNAR = CardHelper.getColor(128, 123, 122);

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_BLACK = "GensokyoResources/images/512/attack_blacky.png";
    private static final String SKILL_BLACK = "GensokyoResources/images/512/skill_blacky.png";
    private static final String POWER_BLACK = "GensokyoResources/images/512/power_blacky.png";

    private static final String ENERGY_ORB_BLACK = "GensokyoResources/images/512/card_blacky_orb.png";
    private static final String CARD_ENERGY_ORB_BLACK = "GensokyoResources/images/512/card_small_orb_blacky.png";

    private static final String ATTACK_BLACK_PORTRAIT = "GensokyoResources/images/1024/attack_blacky.png";
    private static final String SKILL_BLACK_PORTRAIT = "GensokyoResources/images/1024/skill_blacky.png";
    private static final String POWER_BLACK_PORTRAIT = "GensokyoResources/images/1024/power_blacky.png";
    private static final String ENERGY_ORB_BLACK_PORTRAIT = "GensokyoResources/images/1024/card_small_orb_blacky.png";

    private static final String ATTACK_GREEN = "GensokyoResources/images/512/attack_green.png";
    private static final String SKILL_GREEN = "GensokyoResources/images/512/skill_green.png";
    private static final String POWER_GREEN = "GensokyoResources/images/512/power_green.png";

    private static final String ENERGY_ORB_GREEN = "GensokyoResources/images/512/card_green_orb.png";
    private static final String CARD_ENERGY_ORB_GREEN = "GensokyoResources/images/512/card_small_orb_green.png";

    private static final String ATTACK_GREEN_PORTRAIT = "GensokyoResources/images/1024/attack_green.png";
    private static final String SKILL_GREEN_PORTRAIT = "GensokyoResources/images/1024/skill_green.png";
    private static final String POWER_GREEN_PORTRAIT = "GensokyoResources/images/1024/power_green.png";
    private static final String ENERGY_ORB_GREEN_PORTRAIT = "GensokyoResources/images/1024/card_small_orb_green.png";

    private static final String ATTACK_LUNAR = "GensokyoResources/images/512/attack_lunar.png";
    private static final String SKILL_LUNAR = "GensokyoResources/images/512/skill_lunar.png";
    private static final String POWER_LUNAR = "GensokyoResources/images/512/power_lunar.png";

    private static final String ENERGY_ORB_LUNAR = "GensokyoResources/images/512/card_lunar_orb.png";
    private static final String CARD_ENERGY_ORB_LUNAR = "GensokyoResources/images/512/card_small_orb_lunar.png";

    private static final String ATTACK_LUNAR_PORTRAIT = "GensokyoResources/images/1024/attack_lunar.png";
    private static final String SKILL_LUNAR_PORTRAIT = "GensokyoResources/images/1024/skill_lunar.png";
    private static final String POWER_LUNAR_PORTRAIT = "GensokyoResources/images/1024/power_lunar.png";
    private static final String ENERGY_ORB_LUNAR_PORTRAIT = "GensokyoResources/images/1024/card_small_orb_lunar.png";
          
    public static boolean hasMarisa;
    public static boolean hasFriendlyMinions;

    static {
        hasMarisa = Loader.isModLoaded("TS05_Marisa");
        hasFriendlyMinions = Loader.isModLoaded("Friendly_Minions_0987678");
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
        BaseMod.addColor(Enums.URBAN_LEGEND, URBAN_LEGEND, URBAN_LEGEND, URBAN_LEGEND,
                URBAN_LEGEND, URBAN_LEGEND, URBAN_LEGEND, URBAN_LEGEND,
                ATTACK_BLACK, SKILL_BLACK, POWER_BLACK, ENERGY_ORB_BLACK,
                ATTACK_BLACK_PORTRAIT, SKILL_BLACK_PORTRAIT, POWER_BLACK_PORTRAIT,
                ENERGY_ORB_BLACK_PORTRAIT, CARD_ENERGY_ORB_BLACK);
        BaseMod.addColor(Enums.SHOP_SPECIAL, SHOP_SPECIAL, SHOP_SPECIAL, SHOP_SPECIAL,
                SHOP_SPECIAL, SHOP_SPECIAL, SHOP_SPECIAL, SHOP_SPECIAL,
                ATTACK_GREEN, SKILL_GREEN, POWER_GREEN, ENERGY_ORB_GREEN,
                ATTACK_GREEN_PORTRAIT, SKILL_GREEN_PORTRAIT, POWER_GREEN_PORTRAIT,
                ENERGY_ORB_GREEN_PORTRAIT, CARD_ENERGY_ORB_GREEN);
        BaseMod.addColor(Enums.LUNAR, LUNAR, LUNAR, LUNAR,
                LUNAR, LUNAR, LUNAR, LUNAR,
                ATTACK_LUNAR, SKILL_LUNAR, POWER_LUNAR, ENERGY_ORB_LUNAR,
                ATTACK_LUNAR_PORTRAIT, SKILL_LUNAR_PORTRAIT, POWER_LUNAR_PORTRAIT,
                ENERGY_ORB_LUNAR_PORTRAIT, CARD_ENERGY_ORB_LUNAR);
        loadConfigData();
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

        for(AbstractRelic r: RelicLibrary.commonList){ gensokyoRelicHelper.addRelic(r); }
        for(AbstractRelic r: RelicLibrary.uncommonList){ gensokyoRelicHelper.addRelic(r); }
        for(AbstractRelic r: RelicLibrary.rareList){ gensokyoRelicHelper.addRelic(r); }

        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        //Act 1
        Gensokyo gensokyo = new Gensokyo();
        Gensokyoer gensokyoer = new Gensokyoer();
        Gensokyoest gensokyoest = new Gensokyoest();


        //Act 1
        BaseMod.addMonster(Aya.ID, (BaseMod.GetMonster)Aya::new);
        BaseMod.addMonster(Cirno.ID, "Cirno", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new GreaterFairy(-450.0F, 0.0F, null),
                        new SunflowerFairy(-250.0F, 0.0F, null),
                        new ZombieFairy(-50.0F, 0.0F, null),
                        new Cirno(150.0F, 0.0F)
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

        gensokyo.addBoss(Reimu.ID, (BaseMod.GetMonster)Reimu::new, "GensokyoResources/images/monsters/Reimu/Reimu.png", "GensokyoResources/images/monsters/Reimu/ReimuOutline.png");
        gensokyo.addBoss(Yukari.ID, (BaseMod.GetMonster)Yukari::new, "GensokyoResources/images/monsters/Yukari/Yukari.png", "GensokyoResources/images/monsters/Yukari/YukariOutline.png");
        gensokyo.addBoss(Kokoro.ID, (BaseMod.GetMonster)Kokoro::new, "GensokyoResources/images/monsters/Kokoro/Kokoro.png", "GensokyoResources/images/monsters/Kokoro/KokoroOutline.png");


        //Act 2
        CustomIntent.add(new AreaAttackIntent());
        CustomIntent.add(new PurifyIntent());

        BaseMod.addMonster(Reisen.ID, (BaseMod.GetMonster) Reisen::new);
        BaseMod.addMonster(Koishi.ID, (BaseMod.GetMonster) Koishi::new);
        BaseMod.addMonster(Tenshi.ID, (BaseMod.GetMonster) Tenshi::new);

        gensokyoer.addBoss(EncounterIDs.ETERNAL_RIVALS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Byakuren(-960.0F, 0.0F),
                        new Miko(),
                }), "GensokyoResources/images/monsters/Byakuren/Rival.png", "GensokyoResources/images/monsters/Byakuren/RivalOutline.png");
        gensokyoer.addBoss(Kaguya.ID, (BaseMod.GetMonster) Kaguya::new, "GensokyoResources/images/monsters/Kaguya/Kaguya.png", "GensokyoResources/images/monsters/Kaguya/KaguyaOutline.png");
        gensokyoer.addBoss(Eiki.ID, (BaseMod.GetMonster) Eiki::new, "GensokyoResources/images/monsters/Eiki/Eiki.png", "GensokyoResources/images/monsters/Eiki/EikiOutline.png");

        BaseMod.addMonster(Swordslinger.ID, (BaseMod.GetMonster)Swordslinger::new);
        BaseMod.addMonster(Wraith.ID, (BaseMod.GetMonster)Wraith::new);
        BaseMod.addMonster(AngelMirror.ID, (BaseMod.GetMonster)AngelMirror::new);
        BaseMod.addMonster(CosmicMonolith.ID, (BaseMod.GetMonster)CosmicMonolith::new);
        BaseMod.addMonster(Chomper.ID, (BaseMod.GetMonster)Chomper::new);

        BaseMod.addMonster(EncounterIDs.SLIME_GROUP, "Slime_Group", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new BigMudSlime(-450.0F, 0.0F),
                        new SlimeBunny(-150.0F, 0.0F),
                }));
        BaseMod.addMonster(EncounterIDs.DOGS_3, "3_Dogs", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new TanukiDog(-450.0F, 0.0F),
                        new TanukiDog(-200.0F, 0.0F),
                        new TanukiDog(50.0F, 0.0F),
                }));
        BaseMod.addMonster(EncounterIDs.GLOOPS_2, "2_Gloops", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Gloop(-450.0F, 0.0F, false),
                        new Gloop(-150.0F, 0.0F, true),
                }));
        BaseMod.addMonster(EncounterIDs.GLOOPS_3, "3_Gloops", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Gloop(-450.0F, 0.0F, false),
                        new Gloop(-200.0F, 0.0F, false),
                        new Gloop(50.0F, 0.0F, true)
                }));
        BaseMod.addMonster(EncounterIDs.CHOMPER_AND_GLOOP, "Chomper_and_Gloop", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Chomper(-450.0F, 0.0F),
                        new Gloop(-150.0F, 0.0F, true),
                }));
        BaseMod.addMonster(EncounterIDs.MONOLITH_AND_DOG, "Monolith_and_Dog", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new CosmicMonolith(-450.0F, 0.0F),
                        new TanukiDog(-150.0F, 0.0F),
                }));
        BaseMod.addMonster(EncounterIDs.MIRROR_AND_DOG, "Mirror_and_Dog", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new AngelMirror(-450.0F, 0.0F),
                        new TanukiDog(-150.0F, 0.0F),
                }));


        //Act 3
        CustomIntent.add(new DeathIntent());
        CustomIntent.add(new CurseAttackIntent());

        gensokyoest.addBoss(Yuyuko.ID, (BaseMod.GetMonster) Yuyuko::new, "GensokyoResources/images/monsters/Yuyuko/Yuyuko.png", "GensokyoResources/images/monsters/Yuyuko/YuyukoOutline.png");
        gensokyoest.addBoss(EncounterIDs.SCARLET_DEVILS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Remilia(-480.0F, 0.0F),
                        new Flandre(),
                }), "GensokyoResources/images/monsters/Flandre/Sisters.png", "GensokyoResources/images/monsters/Flandre/SistersOutline.png");
        gensokyoest.addBoss(Shinki.ID, (BaseMod.GetMonster) Shinki::new, "GensokyoResources/images/monsters/Shinki/Shinki.png", "GensokyoResources/images/monsters/Shinki/ShinkiOutline.png");

        BaseMod.addMonster(Doremy.ID, (BaseMod.GetMonster) Doremy::new);
        BaseMod.addMonster(Marisa.ID, (BaseMod.GetMonster) Marisa::new);
        BaseMod.addMonster(Kasen.ID, (BaseMod.GetMonster) Kasen::new);

        BaseMod.addMonster(Mokou.ID, (BaseMod.GetMonster) Mokou::new);

        BaseMod.addMonster(SeedOfUnknown.ID, (BaseMod.GetMonster) SeedOfUnknown::new);
        BaseMod.addMonster(AncientGuardian.ID, (BaseMod.GetMonster) AncientGuardian::new);
        BaseMod.addMonster(Rafflesia.ID, (BaseMod.GetMonster) Rafflesia::new);
        BaseMod.addMonster(AtlasGolem.ID, (BaseMod.GetMonster) AtlasGolem::new);
        BaseMod.addMonster(EncounterIDs.DUSK_AND_DAWN, "Dusk_and_Dawn", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Dawnsword(-450.0F, 0.0F),
                        new Duskaxe(-150.0F, 0.0F),
                }));
        BaseMod.addMonster(EncounterIDs.BATS_3, "3_Bats", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new LoudBat(-450.0F, 0.0F),
                        new VampireBat(-200.0F, 0.0F),
                        new FeralBat(50.0F, 0.0F)
                }));
        BaseMod.addMonster(EncounterIDs.BATS_4, "4_Bats", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new LoudBat(-450.0F, 0.0F),
                        new VampireBat(-200.0F, 0.0F),
                        new VampireBat(50.0F, 0.0F),
                        new FeralBat(300.0F, 0.0F)
                }));
        BaseMod.addMonster(Sharpion.ID, (BaseMod.GetMonster) Sharpion::new);
        BaseMod.addMonster(EncounterIDs.BOULDERS_3, "3_Boulders", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new MadBoulder(-450.0F, 0.0F),
                        new MadBoulder(-150.0F, 0.0F),
                        new MadBoulder(150.0F, 0.0F)
                }));

        gensokyo.addAct(Exordium.ID);
        gensokyoer.addAct(TheCity.ID);
        gensokyoest.addAct(TheBeyond.ID);


        // =============== EVENTS =================

        //Act 1
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
            BaseMod.addEvent(AHazardousHobby.ID, AHazardousHobby.class, Gensokyo.ID);
            BaseMod.addEvent(Walpurgisnacht.ID, Walpurgisnacht.class, Gensokyo.ID);
        }


        //Act 2
        BaseMod.addEvent(AndThenThereWereNone.ID, AndThenThereWereNone.class, Gensokyoer.ID);
        BaseMod.addEvent(NightmareOfHeian.ID, NightmareOfHeian.class, Gensokyoer.ID);
        BaseMod.addEvent(Impoverished.ID, Impoverished.class, Gensokyoer.ID);
        BaseMod.addEvent(TheWhiteLotus.ID, TheWhiteLotus.class, Gensokyoer.ID);
        BaseMod.addEvent(LivingGodOfMiracles.ID, LivingGodOfMiracles.class, Gensokyoer.ID);
        BaseMod.addEvent(TreasureHunter.ID, TreasureHunter.class, Gensokyoer.ID);
        BaseMod.addEvent(NohDance.ID, NohDance.class, Gensokyoer.ID);
        BaseMod.addEvent(OneWingedWhiteHeron.ID, OneWingedWhiteHeron.class, Gensokyoer.ID);
        BaseMod.addEvent(ChildOfMiare.ID, ChildOfMiare.class, Gensokyoer.ID);
        BaseMod.addEvent(DetectiveSatori.ID, DetectiveSatori.class, Gensokyoer.ID);
        BaseMod.addEvent(AHistoryOfViolence.ID, AHistoryOfViolence.class, Gensokyoer.ID);
        BaseMod.addEvent(FiresOfInvention.ID, FiresOfInvention.class, Gensokyoer.ID);
        BaseMod.addEvent(ExtraExtra.ID, ExtraExtra.class, Gensokyoer.ID);
        BaseMod.addEvent(VillageOfCats.ID, VillageOfCats.class, Gensokyoer.ID);
        BaseMod.addEvent(Boo.ID, Boo.class, Gensokyoer.ID);
        BaseMod.addEvent(PhantomEnsemble.ID, PhantomEnsemble.class, Gensokyoer.ID);
        BaseMod.addEvent(ClashOfLegends.ID, ClashOfLegends.class, Gensokyoer.ID);


        //Act 3
        BaseMod.addEvent(TheDreamer.ID, TheDreamer.class, Gensokyoest.ID);
        BaseMod.addEvent(IAmNotThere.ID, IAmNotThere.class, Gensokyoest.ID);
        BaseMod.addEvent(OwlFriend.ID, OwlFriend.class, Gensokyoest.ID);
        BaseMod.addEvent(PrincessOfTheMoon.ID, PrincessOfTheMoon.class, Gensokyoest.ID);
        BaseMod.addEvent(KappaWarehouse.ID, KappaWarehouse.class, Gensokyoest.ID);
        BaseMod.addEvent(BloodForBlood.ID, BloodForBlood.class, Gensokyoest.ID);
        BaseMod.addEvent(SomeoneElsesStory.ID, SomeoneElsesStory.class, Gensokyoest.ID);
        BaseMod.addEvent(MedicineSeller.ID, MedicineSeller.class, Gensokyoest.ID);
        BaseMod.addEvent(ProphetOfDisaster.ID, ProphetOfDisaster.class, Gensokyoest.ID);
        BaseMod.addEvent(ProbabilitySpaceHypervessel.ID, ProbabilitySpaceHypervessel.class, Gensokyoest.ID);
        BaseMod.addEvent(ThoseWhoFightMonsters.ID, ThoseWhoFightMonsters.class, Gensokyoest.ID);
        // =============== /EVENTS/ =================

        logger.info("Done loading badge Image and mod options");
    }

    private AbstractMonster[] generateKodamaGroup(int groupSize) {
        if (groupSize != 2 && groupSize != 3) {
            groupSize = 3; //default to 3
        }
        float[] groupPositionsSize2 = {-450.0F, -150.0F};
        float[] groupPositionsSize3 = {-450.0F, -200.0F, 50.0F};
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
        float[] groupPositionsSize3 = {-450.0F, -200.0F, 50.0F};
        float[] groupPositionsSize5 = {-450.0F, -300.0F, -150.0F, 0.0F, 150.0F};
        Boolean[] groupWeakMembers3 = {true, false, false};
        Boolean[] groupWeakMembers5 = {true, true, false, false, false};
        ArrayList<Integer> monstersList = new ArrayList<>();
        monstersList.add(1);
        monstersList.add(2);
        monstersList.add(3);
        monstersList.add(4);
        if (groupSize == 5) {
            monstersList.add(AbstractDungeon.monsterRng.random(1, 4)); //adds a randomly chosen duplicate for the 5th fairy
        }
        float[] groupToUse;
        List<Boolean> weakGroup = new ArrayList<>();
        if (groupSize == 5) {
            groupToUse = groupPositionsSize5;
            Collections.addAll(weakGroup, groupWeakMembers5);
        } else {
            groupToUse = groupPositionsSize3;
            Collections.addAll(weakGroup, groupWeakMembers3);
        }
        Collections.shuffle(monstersList, AbstractDungeon.monsterRng.random);
        Collections.shuffle(weakGroup, AbstractDungeon.monsterRng.random);
        AbstractMonster[] monsters = new AbstractMonster[groupSize];
        for (int i = 0; i < groupSize; i++) {
            boolean isWeak = weakGroup.get(i);
            if (monstersList.get(i) == 1) {
                monsters[i] = new GreaterFairyNormal(groupToUse[i], 0.0F, isWeak);
            } else if (monstersList.get(i) == 2) {
                monsters[i] = new SunflowerFairyNormal(groupToUse[i], 0.0F, isWeak);
            } else if (monstersList.get(i) == 3) {
                monsters[i] = new ZombieFairyNormal(groupToUse[i], 0.0F, isWeak);
            } else if (monstersList.get(i) == 4) {
                monsters[i] = new MaidFairyNormal(groupToUse[i], 0.0F, isWeak);
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

    public static void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig("Gensokyo", "GensokyoConfig");
            config.load();
        }
        catch(Exception e) {
            e.printStackTrace();
            saveData();
        }
    }


    public static void saveData() {
        try {
            SpireConfig config = new SpireConfig("Gensokyo", "GensokyoConfig");
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // =============== / POST-INITIALIZE/ =================
    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("Gensokyo:Train", makeEffectPath("TrainSFX.ogg"));
        BaseMod.addAudio("Gensokyo:ghost", makeEffectPath("ghostbreath.ogg"));
        BaseMod.addAudio("Gensokyo:pest", makeEffectPath("pestilence.ogg"));
        BaseMod.addAudio("Gensokyo:magic", makeEffectPath("magic.ogg"));
        BaseMod.addAudio("Gensokyo:earthquake", makeEffectPath("earthquake.ogg"));
    }
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        //Act 1
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
            BaseMod.addRelic(new IngredientList(), RelicType.SHARED);
            BaseMod.addRelic(new ImprobabilityPotion(), RelicType.SHARED);
            BaseMod.addRelic(new PhilosophersStone(), RelicType.SHARED);
        }

        //Act 2
        BaseMod.addRelic(new UndefinedDarkness(), RelicType.SHARED);
        BaseMod.addRelic(new ConquerorOfFear(), RelicType.SHARED);
        BaseMod.addRelic(new DayTheSeaSplit(), RelicType.SHARED);
        BaseMod.addRelic(new MosesMiracle(), RelicType.SHARED);
        BaseMod.addRelic(new FoxMask(), RelicType.SHARED);
        BaseMod.addRelic(new SpiderMask(), RelicType.SHARED);
        BaseMod.addRelic(new MaskOfHope(), RelicType.SHARED);
        BaseMod.addRelic(new DemonMask(), RelicType.SHARED);
        BaseMod.addRelic(new LionMask(), RelicType.SHARED);
        BaseMod.addRelic(new RedStar(), RelicType.SHARED);
        BaseMod.addRelic(new ChorusOfJoy(), RelicType.SHARED);
        BaseMod.addRelic(new DirgeOfMelancholy(), RelicType.SHARED);
        BaseMod.addRelic(new SongOfSouls(), RelicType.SHARED);

        //Act 3
        BaseMod.addRelic(new EmptyGrave(), RelicType.SHARED);
        BaseMod.addRelic(new DualWield(), RelicType.SHARED);
        BaseMod.addRelic(new TheCrow(), RelicType.SHARED);

        //Other relics
        if (hasFriendlyMinions) {
            BaseMod.addRelic(new Companionship(), RelicType.SHARED);
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
        BaseMod.addCard(new ImpossibleRequest());
        BaseMod.addCard(new FourOfAKind());
        BaseMod.addCard(new AllTheWorldsEvil());
        BaseMod.addCard(new EmbersOfLove());
        BaseMod.addCard(new Philosophy());
        BaseMod.addCard(new ReflexRadar());
        BaseMod.addCard(new MindShatter());
        BaseMod.addCard(new ShootingStar());
        BaseMod.addCard(new Butterfly());
        BaseMod.addCard(new CustomPotion());
        BaseMod.addCard(new GreaterSplit());
        BaseMod.addCard(new PrivateSquare());
        BaseMod.addCard(new InfectedWound());

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
        BaseMod.addCard(new Doppelganger());

        //Pets
        if (hasFriendlyMinions) {
            BaseMod.addCard(new SummonYinYangFox());
            BaseMod.addCard(new SummonFieryMouse());
            BaseMod.addCard(new SummonJeweledCobra());
            BaseMod.addCard(new SummonPsychicCat());
        }

        //Evolve cards
        if (hasFriendlyMinions) {
            BaseMod.addCard(new MysteriousEgg());
            BaseMod.addCard(new ExoticEgg());
        }
        BaseMod.addCard(new BlemishedSteel());
        BaseMod.addCard(new DepletedGenerator());
        BaseMod.addCard(new LockedMedkit());
        BaseMod.addCard(new RustyChest());
        BaseMod.addCard(new ScrapIron());
        BaseMod.addCard(new Shovel());
        BaseMod.addCard(new TarnishedGold());
        BaseMod.addCard(new TrainingManual());

        //Item cards
        BaseMod.addCard(new RPG());
        BaseMod.addCard(new Taser());
        BaseMod.addCard(new ReactiveArmor());
        BaseMod.addCard(new OverchargedCore());
        BaseMod.addCard(new MedicineKit());
        BaseMod.addCard(new LifeforceConverter());

        //Lunar cards
        BaseMod.addCard(new BrilliantDragonBullet());
        BaseMod.addCard(new SalamanderShield());
        BaseMod.addCard(new BuddhistDiamond());
        BaseMod.addCard(new NewMoon());
        BaseMod.addCard(new MorningMist());
        BaseMod.addCard(new EverlastingLife());
        BaseMod.addCard(new LifeSpringInfinity());
        BaseMod.addCard(new DreamlikeParadise());
        BaseMod.addCard(new RainbowDanmaku());
        BaseMod.addCard(new UnlikelyAid());
        BaseMod.addCard(new MorningStar());
        BaseMod.addCard(new RisingWorld());
        BaseMod.addCard(new HouraiInAPot());
        BaseMod.addCard(new Dawn());
        BaseMod.addCard(new UnhurriedMind());

        if (hasMarisa) {
            BaseMod.addCard(new MarisaTwilightSpark());
            BaseMod.addCard(new BlessingOfVigor());
            BaseMod.addCard(new BlessingOfFortitude());
            BaseMod.addCard(new BlessingOfConstitution());
        }
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================

    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret += "zhs/";
                break;
            case JPN:
                ret += "jpn/";
                break;
            case KOR:
                ret += "kor/";
                break;
            case RUS:
                ret += "rus/";
                break;
            default:
                ret += "eng/";
                break;
        }
        return getModID() + "Resources/" + (ret + filename + ".json");
    }

    private void loadLocFiles(Settings.GameLanguage language)
    {
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(language, "GensokyoMod-Card-Strings"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(language, "GensokyoMod-Event-Strings"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, makeLocPath(language, "GensokyoMod-Monster-Strings"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(language, "GensokyoMod-Relic-Strings"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(language, "GensokyoMod-Power-Strings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(language, "GensokyoMod-ui"));
    }

    @Override
    public void receiveEditStrings()
    {
        loadLocFiles(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocFiles(Settings.language);
        }
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    private void loadLocKeywords(Settings.GameLanguage language)
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(makeLocPath(language, "GensokyoMod-Keyword-Strings")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        loadLocKeywords(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocKeywords(Settings.language);
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
