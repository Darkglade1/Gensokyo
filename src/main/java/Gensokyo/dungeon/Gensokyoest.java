package Gensokyo.dungeon;

import Gensokyo.monsters.act3.Doremy;
import Gensokyo.monsters.act3.Marisa;
import Gensokyo.monsters.act3.NormalEnemies.AncientGuardian;
import Gensokyo.monsters.act3.NormalEnemies.AtlasGolem;
import Gensokyo.monsters.act3.NormalEnemies.Rafflesia;
import Gensokyo.monsters.act3.NormalEnemies.SeedOfUnknown;
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

public class Gensokyoest extends CustomDungeon {

    public static String ID = "Gensokyo:Gensokyoest";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Gensokyoest() {
        super(NAME, ID, "images/ui/event/panel.png", false, 2, 12, 10);
        this.addTempMusic("BorderOfLife", "audio/music/Gensokyo/Border of Life.ogg");
    }

    public Gensokyoest(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Gensokyoest(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
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
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    @Override
    public String getBodyText() {
        if (CardCrawlGame.dungeon instanceof Gensokyoer) {
            return TEXT[2];
        } else {
            String[] oldStrings = CardCrawlGame.languagePack.getUIString(Gensokyo.ID).TEXT;
            return oldStrings[2];
        }

    }

    @Override
    public String getOptionText() {
        if (CardCrawlGame.dungeon instanceof Gensokyoer) {
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

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("3 Darklings", 2.0F));
        monsters.add(new MonsterInfo(AncientGuardian.ID, 2.0F));
        monsters.add(new MonsterInfo("3 Shapes", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(SeedOfUnknown.ID, 1.0F));
        monsters.add(new MonsterInfo(AtlasGolem.ID, 1.0F));
        monsters.add(new MonsterInfo("4 Shapes", 1.0F));
        monsters.add(new MonsterInfo(Rafflesia.ID, 1.0F));
        monsters.add(new MonsterInfo("Sphere and 2 Shapes", 1.0F));
        monsters.add(new MonsterInfo("Jaw Worm Horde", 1.0F));
        monsters.add(new MonsterInfo("3 Darklings", 1.0F));
        monsters.add(new MonsterInfo("Writhing Mass", 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(Doremy.ID, 2.0F));
        monsters.add(new MonsterInfo(Marisa.ID, 2.0F));
        monsters.add(new MonsterInfo("Reptomancer", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList();
        String var2 = (String)monsterList.get(monsterList.size() - 1);
        byte var3 = -1;
        switch(var2.hashCode()) {
            case -500373089:
                if (var2.equals("3 Shapes")) {
                    var3 = 2;
                }
                break;
            case 1014856122:
                if (var2.equals("3 Darklings")) {
                    var3 = 0;
                }
                break;
            case 1679632599:
                if (var2.equals("Orb Walker")) {
                    var3 = 1;
                }
        }

        switch(var3) {
            case 0:
                retVal.add("3 Darklings");
                break;
            case 1:
                retVal.add("Orb Walker");
                break;
            case 2:
                retVal.add("4 Shapes");
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