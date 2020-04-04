package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.FourOfAKind;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AndThenThereWereNone extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("AndThenThereWereNone");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Flandre.png");

    private int screenNum = 0;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.30F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.35F;
    private static final int NUM_CARDS = 4;

    private int maxHealthLoss;

    public AndThenThereWereNone() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            maxHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                AbstractCard card = new FourOfAKind();
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1] + NUM_CARDS + " " + card.name + "." + OPTIONS[2] + maxHealthLoss + OPTIONS[3], card); // Accept
                this.imageEventText.setDialogOption(OPTIONS[4]); // Refuse
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: //Accept
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("ATTACK_POISON");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        for(int i = 0; i < NUM_CARDS; ++i) {
                            AbstractCard c = new FourOfAKind();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        }
                        break;
                    case 1: // Refuse
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
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
}
