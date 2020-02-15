package Gensokyo.dungeon;

import Gensokyo.monsters.act1.Aya;
import Gensokyo.monsters.act1.Cirno;
import Gensokyo.monsters.act1.Mamizou;
import Gensokyo.monsters.act1.NormalEnemies.CorruptedTreant;
import Gensokyo.monsters.act1.NormalEnemies.Gryphon;
import Gensokyo.monsters.act1.NormalEnemies.Kitsune;
import Gensokyo.monsters.act1.NormalEnemies.LivingMonolith;
import Gensokyo.monsters.act1.NormalEnemies.Python;
import Gensokyo.monsters.act1.NormalEnemies.VengefulSpirit;
import Gensokyo.scenes.GensokyoScene;
import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

public class Gensokyo extends actlikeit.dungeons.CustomDungeon {

    public static String ID = "Gensokyo:Gensokyo";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Gensokyo() {
        super(NAME, ID, "images/ui/event/panel.png", false, 3, 12, 10);
        this.onEnterEvent(NeowEvent.class);
        this.setMainMusic("audio/music/Gensokyo/ThemeOfEasternStory.ogg");
        this.addTempMusic("Necrofantasia", "audio/music/Gensokyo/Necrofantasia.ogg");
        this.addTempMusic("TheLostEmotion", "audio/music/Gensokyo/TheLostEmotion.ogg");
        this.addTempMusic("G Free", "audio/music/Gensokyo/G Free.ogg");
        this.addTempMusic("Wind God Girl", "audio/music/Gensokyo/Wind God Girl.ogg");
        this.addTempMusic("TomboyishGirl", "audio/music/Gensokyo/TomboyishGirl.ogg");
        this.addTempMusic("Futatsuiwa from Gensokyo", "audio/music/Gensokyo/Futatsuiwa from Gensokyo.ogg");
        this.addTempMusic("LockedGirl", "audio/music/Gensokyo/LockedGirl.ogg");
        this.addTempMusic("LastOccult", "audio/music/Gensokyo/LastOccult.ogg");
        this.addTempMusic("BorderOfLife", "audio/music/Gensokyo/Border of Life.ogg");
        this.addTempMusic("FateOfSixtyYears", "audio/music/Gensokyo/FateOfSixtyYears.ogg");
        this.addTempMusic("LunaticPrincess", "audio/music/Gensokyo/LunaticPrincess.ogg");
    }

    public Gensokyo(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Gensokyo(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new GensokyoScene();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.25F;
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

    @Override
    protected void generateMonsters() {
        generateWeakEnemies(weakpreset);
        generateStrongEnemies(strongpreset);
        generateElites(elitepreset);
    }

    @Override
    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(Python.ID, 2.0F));
        monsters.add(new MonsterInfo(Gryphon.ID, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.KODAMA_2, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.FAIRIES_3, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(LivingMonolith.ID, 2.0F));
        //monsters.add(new MonsterInfo("Gremlin Gang", 1.0F));
        monsters.add(new MonsterInfo(Kitsune.ID, 2.0F));
        monsters.add(new MonsterInfo(VengefulSpirit.ID, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.FAIRIES_5, 1.0F));
        monsters.add(new MonsterInfo(EncounterIDs.GRYPHON_AND_RABBIT, 1.5F));
        monsters.add(new MonsterInfo(EncounterIDs.PYTHON_AND_KODAMA, 1.5F));
        monsters.add(new MonsterInfo(CorruptedTreant.ID, 1.0F));
        monsters.add(new MonsterInfo(EncounterIDs.KODAMA_3, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.RABBITS_2, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(Mamizou.ID, 1.0F));
        monsters.add(new MonsterInfo(Aya.ID, 1.0F));
        monsters.add(new MonsterInfo(Cirno.ID, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }
    @Override
    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList();
        switch (monsterList.get(monsterList.size() - 1))
        {
            case EncounterIDs.KODAMA_2:
                retVal.add(EncounterIDs.PYTHON_AND_KODAMA);
                retVal.add(EncounterIDs.KODAMA_3);
                break;
            case EncounterIDs.FAIRIES_3:
                retVal.add(EncounterIDs.FAIRIES_5);
                break;
            case Gryphon.ID:
                retVal.add(EncounterIDs.GRYPHON_AND_RABBIT);
                break;
            case Python.ID:
                retVal.add(EncounterIDs.PYTHON_AND_KODAMA);
                break;
        }

        return retVal;
    }

    @Override
    protected void initializeShrineList() {
        //No shrines
    }

    @Override
    protected void initializeEventList() {
        // Events are added via BaseMod in GensokyoMod.receivePostInitialize()
        specialOneTimeEventList.clear(); //gets rid of these global events just in case
        shrineList.clear(); //gets rid of this shit too just in case
    }
}