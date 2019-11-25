package Gensokyo.dungeon;

import Gensokyo.monsters.Aya;
import Gensokyo.monsters.Cirno;
import Gensokyo.monsters.Mamizou;
import Gensokyo.monsters.NormalEnemies.CorruptedTreant;
import Gensokyo.monsters.NormalEnemies.Gryphon;
import Gensokyo.monsters.NormalEnemies.LivingMonolith;
import Gensokyo.monsters.NormalEnemies.Python;
import Gensokyo.monsters.NormalEnemies.VengefulSpirit;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheBottomScene;

import java.util.ArrayList;

public class Gensokyo extends CustomDungeon {

    public static String ID = "Gensokyo:Gensokyo";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Gensokyo() {
        super(new TheBottomScene(), NAME, ID);
    }

    public Gensokyo(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Gensokyo(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }


    @Override
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
    public void setupMisc(CustomDungeon cd, int actNum) {
        //Copying data from the instance that was used for initialization.
        if (scene != null && scene != cd.savedScene) {
            scene.dispose();
        }
        scene = new TheBottomScene();
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
        monsters.add(new MonsterInfo("Gremlin Gang", 1.0F));
        monsters.add(new MonsterInfo("Looter", 2.0F));
        monsters.add(new MonsterInfo(VengefulSpirit.ID, 2.0F));
        monsters.add(new MonsterInfo(EncounterIDs.FAIRIES_5, 1.0F));
        monsters.add(new MonsterInfo("Exordium Thugs", 1.5F));
        monsters.add(new MonsterInfo("Exordium Wildlife", 1.5F));
        monsters.add(new MonsterInfo(CorruptedTreant.ID, 1.0F));
        monsters.add(new MonsterInfo(EncounterIDs.KODAMA_3, 2.0F));
        monsters.add(new MonsterInfo("2 Fungi Beasts", 2.0F));
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
        String var2 = monsterList.get(monsterList.size() - 1);
        byte var3 = -1;
        switch(var2.hashCode()) {
            case -2013219467:
                if (var2.equals("Looter")) {
                    var3 = 0;
                }
                break;
            case -1879712874:
                if (var2.equals(EncounterIDs.KODAMA_2)) {
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
                retVal.add(EncounterIDs.KODAMA_3);
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
        specialOneTimeEventList.clear(); //gets rid of these global events
    }

    @Override
    protected void initializeShrineList() {
        //No shrines
    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }
        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }
}