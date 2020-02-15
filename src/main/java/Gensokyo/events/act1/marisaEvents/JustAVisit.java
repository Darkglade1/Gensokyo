package Gensokyo.events.act1.marisaEvents;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.BlessingOfConstitution;
import Gensokyo.cards.BlessingOfFortitude;
import Gensokyo.cards.BlessingOfVigor;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeEventPath;

public class JustAVisit extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MarisaJustAVisit");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("ReimuMarisa.png");

    private int screenNum = 0;

    private boolean hasEnoughMoney;
    public static final int COST = 50;
    private AbstractCard blessing;

    public JustAVisit() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.player.gold >= COST) {
            hasEnoughMoney = true;
        }

        ArrayList<AbstractCard> blessings = new ArrayList<>();
        blessings.add(new BlessingOfVigor());
        blessings.add(new BlessingOfConstitution());
        blessings.add(new BlessingOfFortitude());
        blessing = blessings.get(AbstractDungeon.cardRandomRng.random(0, blessings.size() - 1));
        UnlockTracker.markCardAsSeen(blessing.cardID);
        if (this.hasEnoughMoney) {
            this.imageEventText.setDialogOption(OPTIONS[1] + COST + OPTIONS[2] + blessing.name + ".", blessing);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[3] + COST + OPTIONS[4], true);
        }

        //Leave
        this.imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Donate
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(COST);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(blessing, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
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
