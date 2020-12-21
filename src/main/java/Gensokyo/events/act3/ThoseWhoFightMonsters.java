package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.GreaterSplit;
import Gensokyo.cards.PrivateSquare;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ThoseWhoFightMonsters extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("ThoseWhoFightMonsters");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("TimeStop.png");

    private int screenNum = 0;
    AbstractCard card1 = new GreaterSplit();
    AbstractCard curse1 = new Doubt();
    AbstractCard card2 = new PrivateSquare();
    AbstractCard curse2 = new Pride();

    public ThoseWhoFightMonsters() {
        super(NAME, DESCRIPTIONS[0], IMG);
        UnlockTracker.markCardAsSeen(GreaterSplit.ID);
        UnlockTracker.markCardAsSeen(PrivateSquare.ID);
        this.imageEventText.setDialogOption(OPTIONS[0] + FontHelper.colorString(card1.name, "g") + OPTIONS[2] + FontHelper.colorString(curse1.name, "r") + OPTIONS[3], card1);
        this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(card2.name, "g") + OPTIONS[2] + FontHelper.colorString(curse2.name, "r") + OPTIONS[3], card2);
        this.imageEventText.setDialogOption(OPTIONS[4]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Observe warrior in red
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card1, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse1, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1: // Observe maid in blue
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card2, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse2, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }
}
