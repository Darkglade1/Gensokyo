package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.LunaticRedEyes;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ThoseEarthRabbits extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ThoseEarthRabbits");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Rabbits.png");

    private int screenNum = 0;

    private int GOLD = 80;

    public ThoseEarthRabbits() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy(Injury.ID)); // Help
                this.imageEventText.setDialogOption(OPTIONS[2] + GOLD + OPTIONS[3]); // Accept the bribe
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: //Help
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(LunaticRedEyes.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(LunaticRedEyes.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        AbstractCard curse = new Injury();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        break;
                    case 1: // Accept the bribe
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        break;
                }
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                // Shake the screen
                CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
