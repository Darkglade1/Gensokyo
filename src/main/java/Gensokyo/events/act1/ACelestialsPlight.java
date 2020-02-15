package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act1.CelestialsFlawlessClothing;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ACelestialsPlight extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ACelestialsPlight");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Celestial.png");

    private int screenNum = 0;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.25F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.30F;
    private int GOLD = 100;

    private int healthdamage;

    public ACelestialsPlight() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }

        this.imageEventText.setDialogOption(OPTIONS[1] + healthdamage + OPTIONS[2]); // Intervene
        this.imageEventText.setDialogOption(OPTIONS[3] + GOLD + OPTIONS[4]); // Let events transpire
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Intervene
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("ATTACK_HEAVY");  // Play a hit sound
                        AbstractDungeon.player.damage(new DamageInfo(null, healthdamage));
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(CelestialsFlawlessClothing.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(CelestialsFlawlessClothing.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Let events transpire
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 2;
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
