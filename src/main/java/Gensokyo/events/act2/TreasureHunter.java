package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.GoldenIdol;

import static Gensokyo.GensokyoMod.makeEventPath;

public class TreasureHunter extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("TreasureHunter");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Nazrin.png");

    private int screenNum = 0;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.08F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.1F;
    private static final int MAX_HP_GAIN = 20;

    private int maxHealthLoss;

    public TreasureHunter() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }
        if (AbstractDungeon.player.hasRelic(GoldenIdol.ID)) {
            this.imageEventText.setDialogOption(OPTIONS[0] + MAX_HP_GAIN + OPTIONS[1]); // Offer Golden Idol
        } else {
            this.imageEventText.setDialogOption(OPTIONS[5], true);
        }
        this.imageEventText.setDialogOption(OPTIONS[2] + maxHealthLoss + OPTIONS[3]); // Help dig
        this.imageEventText.setDialogOption(OPTIONS[4]); // Leave
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Offer Idol
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseRelic(GoldenIdol.ID);
                        AbstractDungeon.player.increaseMaxHp(MAX_HP_GAIN, true);
                        break;
                    case 1: //Help dig
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
