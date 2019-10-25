package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.NagashiBinaDoll;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import static Gensokyo.GensokyoMod.makeEventPath;

public class GoddessOfMisfortune extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("GoddessOfMisfortune");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Hina.png");

    private int screenNum = 0;

    public GoddessOfMisfortune() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1]);
                this.imageEventText.setDialogOption(OPTIONS[2]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Accept
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(NagashiBinaDoll.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(NagashiBinaDoll.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Refuse
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
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
