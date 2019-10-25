package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.PerfectCherryBlossom;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import static Gensokyo.GensokyoMod.makeEventPath;

public class BorderOfDeath extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("BorderOfDeath");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("BorderOfDeath.png");

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;

    private int maxHpLoss;
    private float MAX_HP_PERCENT = 0.12F;
    private float MAX_HP_PERCENT_HIGH_ASC = 0.15F;

    public BorderOfDeath() {
        super(NAME, DESCRIPTIONS[0], IMG);
        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[0]);

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_PERCENT_HIGH_ASC);
        } else {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_PERCENT);
        }

    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                screenNum = 2;
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                screenNum = 3;
                break;
            case 3:
                this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[2] + this.maxHpLoss + OPTIONS[5]); // Prove it
                this.imageEventText.setDialogOption(OPTIONS[1]); //Refuse
                screenNum = 4;
                break;
            case 4:
                switch (buttonPressed) {
                    case 0: // Prove it
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        screenNum = 5;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(PerfectCherryBlossom.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(PerfectCherryBlossom.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Refuse
                        this.imageEventText.updateBodyText(DESCRIPTIONS[7]);
                        screenNum = 7;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 5:
                this.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                screenNum = 6;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 6:
                this.openMap();
                break;
            case 7:
                this.imageEventText.updateBodyText(DESCRIPTIONS[8]);
                screenNum = 6;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                break;
            default:
                this.openMap();
        }
    }
}
