package Gensokyo.events.act3;

import Gensokyo.CardMods.EtherealMod;
import Gensokyo.CardMods.ExhaustMod;
import Gensokyo.CardMods.RetainMod;
import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class TheDreamer extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("TheDreamer");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Dream.png");

    private static final int NUM_RETAIN = 1;
    private static final int NUM_ETHEREAL = 2;
    private static final int NUM_EXHAUST = 3;

    private int screenNum = 0;
    private boolean hasCards;
    private boolean hasNonEtherealCards;
    private AbstractCardModifier cardMod;

    public TheDreamer() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();

        hasCards = AbstractDungeon.player.masterDeck.size() != 0;
        hasNonEtherealCards = HasNonEtherealCards();

        if (hasCards) {
            if (hasNonEtherealCards) {
                this.imageEventText.setDialogOption(OPTIONS[1] + OPTIONS[3] + NUM_RETAIN + OPTIONS[4] + OPTIONS[6]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[15], true);
            }
            this.imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[3] + NUM_ETHEREAL + OPTIONS[5] + OPTIONS[7]);
            this.imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[3] + NUM_EXHAUST + OPTIONS[5] + OPTIONS[8]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[16], true);
            this.imageEventText.setDialogOption(OPTIONS[17]);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1] + AbstractDungeon.player.name + DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[17]);
                        screenNum = 1;
                        cardMod = new RetainMod();
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if (!card.isEthereal) {
                                group.addToBottom(card);
                            }
                        }
                        AbstractDungeon.gridSelectScreen.open(group, NUM_RETAIN, OPTIONS[9] + NUM_RETAIN + OPTIONS[10] + OPTIONS[12], false);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[17]);
                        screenNum = 1;
                        if (hasCards) {
                            cardMod = new EtherealMod();
                            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, NUM_ETHEREAL, OPTIONS[9] + NUM_ETHEREAL + OPTIONS[11] + OPTIONS[13], false);
                        }
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[17]);
                        screenNum = 1;
                        cardMod = new ExhaustMod();
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, NUM_EXHAUST, OPTIONS[9] + NUM_EXHAUST + OPTIONS[11] + OPTIONS[14], false);
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
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                CardModifierManager.addModifier(c, cardMod.makeCopy());
                AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
            }
        }
    }

    private boolean HasNonEtherealCards() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (!card.isEthereal) {
                return true;
            }
        }
        return false;
    }

}
