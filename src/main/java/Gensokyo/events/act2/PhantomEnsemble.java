package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act2.ChorusOfJoy;
import Gensokyo.relics.act2.DirgeOfMelancholy;
import Gensokyo.relics.act2.SongOfSouls;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import static Gensokyo.GensokyoMod.makeEventPath;

public class PhantomEnsemble extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("PhantomEnsemble");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Ensemble.png");

    private AbstractRelic relic1;
    private AbstractRelic relic2;
    private AbstractRelic relic3;

    private int screenNum = 0;

    public PhantomEnsemble() {
        super(NAME, DESCRIPTIONS[0], IMG);
        relic1 = new SongOfSouls();
        relic2 = new ChorusOfJoy();
        relic3 = new DirgeOfMelancholy();
        imageEventText.setDialogOption(OPTIONS[0], relic1);
        imageEventText.setDialogOption(OPTIONS[1], relic2);
        imageEventText.setDialogOption(OPTIONS[2], relic3);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        if (AbstractDungeon.player.hasRelic(relic1.relicId)) {
                            relic1 = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic1);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        if (AbstractDungeon.player.hasRelic(relic2.relicId)) {
                            relic2 = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic2);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        if (AbstractDungeon.player.hasRelic(relic3.relicId)) {
                            relic3 = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic3);
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
