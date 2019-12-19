package Gensokyo.events.marisaEvents;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.MarisaTwilightSpark;
import ThMod.cards.Marisa.FinalSpark;
import ThMod.cards.Marisa.MasterSpark;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AnOldGhost extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MarisaAnOldGhost");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Mima.png");

    private int screenNum = 0;

    private boolean upgradedSpark = false;
    private boolean masterSparkInstead = true;
    private AbstractCard card;

    public AnOldGhost() {
        super(NAME, DESCRIPTIONS[0], IMG);
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cardID.equals(FinalSpark.ID)) {
                upgradedSpark = true;
                masterSparkInstead = false;
            }
            if (card.cardID.equals(MasterSpark.ID)) {
                masterSparkInstead = false;
                if (card.upgraded) {
                    upgradedSpark = true;
                }
            }
        }

        if (upgradedSpark) {
            card = new MarisaTwilightSpark();
            card.upgrade();
            UnlockTracker.markCardAsSeen(MarisaTwilightSpark.ID);
        } else if (!masterSparkInstead) {
            card = new MarisaTwilightSpark();
            UnlockTracker.markCardAsSeen(MarisaTwilightSpark.ID);
        } else {
            card = new MasterSpark();
        }

        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                if (masterSparkInstead) {
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                } else {
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                }
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1] + card.name + ".", card);
                this.imageEventText.setDialogOption(OPTIONS[2]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: //Accept teaching
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1: // Decline
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
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
