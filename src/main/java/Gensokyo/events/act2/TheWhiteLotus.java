package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class TheWhiteLotus extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("TheWhiteLotus");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Byakuren.png");

    private int screenNum = 0;
    private boolean pickCard = false;

    private boolean hasCard;
    private AbstractCard card;

    private static final int NUM_CARDS = 3;
    public static final int GOLD_COST = 50;
    private static final int MAX_HEALTH_GAIN = 7;

    public TheWhiteLotus() {
        super(NAME, DESCRIPTIONS[0], IMG);
        card = this.getCard();
        if (card == null) {
            hasCard = false;
        } else {
            hasCard = true;
        }
       if (hasCard) {
           imageEventText.setDialogOption(OPTIONS[0] + card.name + OPTIONS[1], card);
       } else {
           imageEventText.setDialogOption(OPTIONS[6], true);
       }
       if (AbstractDungeon.player.gold >= GOLD_COST) {
           imageEventText.setDialogOption(OPTIONS[2] + GOLD_COST + OPTIONS[3] + MAX_HEALTH_GAIN + OPTIONS[4]);
       } else {
           imageEventText.setDialogOption(OPTIONS[7] + GOLD_COST + OPTIONS[8], true);
       }
        imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: //Listen
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.card));
                        AbstractDungeon.player.masterDeck.removeCard(this.card);
                        this.pickCard = true;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                        for(int i = 0; i < NUM_CARDS; ++i) {
                            AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRareOrUncommon(1.0F)).makeCopy();
                            boolean containsDupe = true;

                            while(true) {
                                while(containsDupe) {
                                    containsDupe = false;
                                    Iterator var6 = group.group.iterator();

                                    while(var6.hasNext()) {
                                        AbstractCard c = (AbstractCard)var6.next();
                                        if (c.cardID.equals(card.cardID)) {
                                            containsDupe = true;
                                            card = AbstractDungeon.getCard(AbstractDungeon.rollRareOrUncommon(1.0F)).makeCopy();
                                            break;
                                        }
                                    }
                                }

                                if (group.contains(card)) {
                                    --i;
                                } else {
                                    group.addToBottom(card);
                                }
                                break;
                            }
                        }

                        Iterator var8 = group.group.iterator();

                        while(var8.hasNext()) {
                            AbstractCard c = (AbstractCard)var8.next();
                            UnlockTracker.markCardAsSeen(c.cardID);
                        }

                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[9], false);

                        break;
                    case 1: // Donate
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(GOLD_COST);
                        AbstractDungeon.player.increaseMaxHp(MAX_HEALTH_GAIN, true);
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
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

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private AbstractCard getCard() {
        ArrayList<AbstractCard> validCards = new ArrayList<>();

        for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
            if (c.cost == 0) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            return null;
        }
        Collections.shuffle(validCards, new Random(AbstractDungeon.miscRng.randomLong()));
        return validCards.get(0);
    }

    public static boolean hasZeroCostCard() {
        for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
            if (c.cost == 0) {
                return true;
            }
        }
        return false;
    }

}
