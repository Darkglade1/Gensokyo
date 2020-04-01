package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ForestOfMagic extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ForestOfMagic");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("ForestOfMagic.png");

    private static final float MAX_HP_BOOST = 0.10F;
    private static final int MIN_HP_BOOST = 5;
    private static final int NUM_CURSES = 1;

    private int screenNum = 0;
    private int hpBoost;

    public ForestOfMagic() {
        super(NAME, DESCRIPTIONS[0], IMG);

        int boost = (int)(AbstractDungeon.player.maxHealth * MAX_HP_BOOST);
        hpBoost = Math.max(boost, MIN_HP_BOOST);
        AbstractCard curse = CardLibrary.getCopy(Injury.ID);
        this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(curse.name, "r") + OPTIONS[2], curse); // Marisa
        this.imageEventText.setDialogOption(OPTIONS[3] + hpBoost + OPTIONS[4]); // Alice
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Marisa
                        this.imageEventText.loadImage(makeEventPath("Marisa.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        for (int i = 0; i < NUM_CURSES; i++) {
                            AbstractCard curse = new Injury();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        }
                        break;
                    case 1: // Alice
                        this.imageEventText.loadImage(makeEventPath("Alice.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        screenNum = 5;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.increaseMaxHp(hpBoost, true);
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
                break;
            case 3:
                this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                screenNum = 4;
                this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 4:
                this.openMap();
                break;
            case 5:
                this.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                screenNum = 6;
                break;
            case 6:
                this.imageEventText.updateBodyText(DESCRIPTIONS[7]);
                screenNum = 4;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            default:
                this.openMap();
        }
    }
}
