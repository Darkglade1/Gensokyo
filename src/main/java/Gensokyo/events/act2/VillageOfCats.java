package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act1.Yukari;
import Gensokyo.relics.act1.PortableGap;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Gensokyo.GensokyoMod.makeEventPath;

public class VillageOfCats extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("VillageOfCats");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Cats.png");

    private static final int MAX_HP = 5;
    private static final int NUM_RARE_RELICS = 2;
    private static final int GOLD = 100;

    private AbstractMonster enemy;
    private boolean hasGap;

    private int screenNum = 0;

    public VillageOfCats() {
        super(NAME, DESCRIPTIONS[0], IMG);
        if (AbstractDungeon.player.hasRelic(PortableGap.ID)) {
            hasGap = true;
        } else {
            hasGap = false;
        }
        if (hasGap) {
            imageEventText.setDialogOption(OPTIONS[2]);
        } else {
            imageEventText.setDialogOption(OPTIONS[0] + MAX_HP + OPTIONS[1]);
        }
        enemy = new Yukari();
        imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(enemy.name, "r") + OPTIONS[4] + NUM_RARE_RELICS + OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        if (hasGap) {
                            PetFreeRelic();
                        } else {
                            PetMaxHP();
                        }
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.loadImage(makeEventPath("Yukari.png"));
                        break;
                }
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(enemy);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        for (int i = 0; i < NUM_RARE_RELICS; i++) {
                            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                        }
                        AbstractDungeon.getCurrRoom().addGoldToRewards(GOLD);
                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        AbstractDungeon.scene.nextRoom(AbstractDungeon.getCurrRoom()); //switches bg
                        this.enterCombatFromImage();
                        break;
                }
                break;
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void PetMaxHP() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
        this.imageEventText.clearRemainingOptions();
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
    }

    private void PetFreeRelic() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
        this.imageEventText.clearRemainingOptions();
        this.imageEventText.loadImage(makeEventPath("Chen.png"));
        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
    }
}
