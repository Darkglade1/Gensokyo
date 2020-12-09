package Gensokyo.dungeon;

import Gensokyo.monsters.act2.Koishi;
import Gensokyo.monsters.act2.NormalEnemies.AngelMirror;
import Gensokyo.monsters.act2.NormalEnemies.Chomper;
import Gensokyo.monsters.act2.NormalEnemies.CosmicMonolith;
import Gensokyo.monsters.act2.NormalEnemies.Swordslinger;
import Gensokyo.monsters.act2.NormalEnemies.Wraith;
import Gensokyo.monsters.act2.Reisen;
import Gensokyo.monsters.act2.Tenshi;
import Gensokyo.scenes.GensokyoScene;
import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

public class Gensokyoer extends CustomDungeon {

    public static String ID = "Gensokyo:Gensokyoer";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Gensokyoer() {
        super(NAME, ID, "images/ui/event/panel.png", false, 2, 12, 10);
        this.setMainMusic("audio/music/Gensokyo/KidFestival.ogg");
        this.addTempMusic("FateOfSixtyYears", "audio/music/Gensokyo/FateOfSixtyYears.ogg");
        this.addTempMusic("LunaticPrincess", "audio/music/Gensokyo/LunaticPrincess.ogg");
        this.addTempMusic("TrueAdmin", "audio/music/Gensokyo/TrueAdmin.ogg");
        this.addTempMusic("CosmicMind", "audio/music/Gensokyo/CosmicMind.ogg");
        this.addTempMusic("FullMoon", "audio/music/Gensokyo/FullMoon.ogg");
        this.addTempMusic("Hartmann", "audio/music/Gensokyo/Hartmann.ogg");
        this.addTempMusic("Bhavagra", "audio/music/Gensokyo/Bhavagra.ogg");
    }

    public Gensokyoer(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Gensokyoer(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
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
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.125F;
        } else {
            cardUpgradedChance = 0.25F;
        }
    }

    @Override
    public String getBodyText() {
        if (CardCrawlGame.dungeon instanceof Gensokyo) {
            return TEXT[2];
        } else {
            String[] oldStrings = CardCrawlGame.languagePack.getUIString(Gensokyo.ID).TEXT;
            return oldStrings[2];
        }

    }

    @Override
    public String getOptionText() {
        if (CardCrawlGame.dungeon instanceof Gensokyo) {
            return TEXT[3];
        } else {
            String[] oldStrings = CardCrawlGame.languagePack.getUIString(Gensokyo.ID).TEXT;
            return oldStrings[3];
        }
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
        monsters.add(new MonsterInfo(AngelMirror.ID, 2.0F));
        monsters.add(new MonsterInfo(Chomper.ID, 2.0F));
        monsters.add(new MonsterInfo(CosmicMonolith.ID, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.DOGS_3, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.GLOOPS_2, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        //monsters.add(new MonsterInfo("Chosen and Byrds", 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.MIRROR_AND_DOG, 2.0F));
        monsters.add(new MonsterInfo(Wraith.ID, 6.0F));
        monsters.add(new MonsterInfo(Swordslinger.ID, 4.0F));
        monsters.add(new MonsterInfo(EncounterIDs.SLIME_GROUP, 6.0F));
        monsters.add(new MonsterInfo(EncounterIDs.CHOMPER_AND_GLOOP, 3.0F));
        monsters.add(new MonsterInfo(EncounterIDs.GLOOPS_3, 3.0F));
        monsters.add(new MonsterInfo(EncounterIDs.MONOLITH_AND_DOG, 3.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }
    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(Reisen.ID, 1.0F));
        monsters.add(new MonsterInfo(Koishi.ID, 1.0F));
        monsters.add(new MonsterInfo(Tenshi.ID, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }
    @Override
    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList();
        switch (monsterList.get(monsterList.size() - 1))
        {
            case EncounterIDs.GLOOPS_2:
                retVal.add(EncounterIDs.GLOOPS_3);
                retVal.add(EncounterIDs.CHOMPER_AND_GLOOP);
                break;
            case EncounterIDs.DOGS_3:
                retVal.add(EncounterIDs.MONOLITH_AND_DOG);
                retVal.add(EncounterIDs.MIRROR_AND_DOG);
                break;
            case CosmicMonolith.ID:
                retVal.add(EncounterIDs.MONOLITH_AND_DOG);
                break;
            case Chomper.ID:
                retVal.add(EncounterIDs.CHOMPER_AND_GLOOP);
                break;
            case AngelMirror.ID:
                retVal.add(EncounterIDs.MIRROR_AND_DOG);
                break;
        }

        return retVal;
    }

    @Override
    protected void initializeShrineList() {
    }

    @Override
    protected void initializeEventList() {
        // Events are added via BaseMod in GensokyoMod.receivePostInitialize()
    }
}