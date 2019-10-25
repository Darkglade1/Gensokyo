package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.PortableGap;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AHoleInReality extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("AHoleInReality");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("GapEvent.png");

    private int screenNum = 0;

    private float HEALTH_LOSS_PERCENTAGE = 0.30F;
    private float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.40F;

    private int maxHealthLoss;

    public AHoleInReality() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }

        this.imageEventText.setDialogOption(OPTIONS[1] + maxHealthLoss + OPTIONS[2]); // Jump in
        this.imageEventText.setDialogOption(OPTIONS[3]); // Leave
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Jump in
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(PortableGap.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(PortableGap.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 2;
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
