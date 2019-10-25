package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.OccultBall;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class FieldTripToAnotherWorld extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("FieldTripToAnotherWorld");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Shadow.png");

    private boolean hasCard;
    private AbstractCard card;

    private int screenNum = 0;

    public FieldTripToAnotherWorld() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        card = this.getCard();
        if (card == null) {
            hasCard = false;
        } else {
            hasCard = true;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                if (this.hasCard) {
                    this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(this.card.name, "r") + "." + OPTIONS[2], this.card.makeStatEquivalentCopy());
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[3], true);
                }
                this.imageEventText.setDialogOption(OPTIONS[5]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Accept
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.card));
                        AbstractDungeon.player.masterDeck.removeCard(this.card);
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(OccultBall.ID)) {
                            OccultBall ball = (OccultBall)AbstractDungeon.player.getRelic(OccultBall.ID);
                            if (ball.counter == OccultBall.MAX_STACKS) {
                                relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                            } else {
                                relic = RelicLibrary.getRelic(OccultBall.ID).makeCopy();
                            }
                        } else {
                            relic = RelicLibrary.getRelic(OccultBall.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);

                        break;
                    case 1: // Refuse
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
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

    private AbstractCard getCard() {
        ArrayList<AbstractCard> rares = new ArrayList<>();
        ArrayList<AbstractCard> uncommons = new ArrayList<>();
        ArrayList<AbstractCard> commons = new ArrayList<>();

        for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
            if (c.rarity == AbstractCard.CardRarity.RARE) {
                rares.add(c);
            } else if (c.rarity == AbstractCard.CardRarity.UNCOMMON) {
                uncommons.add(c);
            } else if (c.rarity == AbstractCard.CardRarity.COMMON) {
                commons.add(c);
            }
        }
        ArrayList<AbstractCard> usedList;
        if (!rares.isEmpty()) {
            usedList = rares;
        } else if (!uncommons.isEmpty()) {
            usedList = uncommons;
        } else if (!commons.isEmpty()) {
            usedList = commons;
        } else {
            return null;
        }

        Collections.shuffle(usedList, new Random(AbstractDungeon.miscRng.randomLong()));
        return usedList.get(0);
    }

}
