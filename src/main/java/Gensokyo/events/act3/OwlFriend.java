package Gensokyo.events.act3;

import Gensokyo.CardMods.AutoplayMod;
import Gensokyo.CardMods.UnplayableMod;
import Gensokyo.GensokyoMod;
import Gensokyo.relics.act3.DualWield;
import Gensokyo.relics.act3.TheCrow;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import static Gensokyo.GensokyoMod.makeEventPath;

public class OwlFriend extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("OwlFriend");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Owl.png");

    private static final int NUM_AUTOPLAY = 2;
    private static final int NUM_UNPLAYABLE = 2;

    private int screenNum = 0;
    private boolean hasPlayableCards;
    private boolean hasPlayableCardsExcludeAutoplay;
    private AbstractCardModifier cardMod;

    private AbstractRelic relic1 = new TheCrow();
    private AbstractRelic relic2 = new DualWield();

    public OwlFriend() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();

        hasPlayableCards = HasPlayableCards();
        hasPlayableCardsExcludeAutoplay = HasPlayableCardsExcludeAutoplay();

        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                this.screenNum = 1;

                if (hasPlayableCardsExcludeAutoplay) {
                    this.imageEventText.setDialogOption(OPTIONS[1] + OPTIONS[3] + NUM_AUTOPLAY + OPTIONS[5] + OPTIONS[6], relic1);
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[13], true);
                }
                if (hasPlayableCards) {
                    this.imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[3] + NUM_UNPLAYABLE + OPTIONS[5] + OPTIONS[7], relic2);
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[13], true);
                }
                this.imageEventText.setDialogOption(OPTIONS[14]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[14]);
                        screenNum = 2;
                        cardMod = new AutoplayMod();
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if (card.cost != -2 && !AutoplayField.autoplay.get(card) && !CardModifierManager.hasModifier(card, AutoplayMod.ID)) {
                                group.addToBottom(card);
                            }
                        }
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(relic1.relicId)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = relic1;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        AbstractDungeon.gridSelectScreen.open(group, NUM_AUTOPLAY, OPTIONS[8] + NUM_AUTOPLAY + OPTIONS[10] + OPTIONS[11], false);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[14]);
                        screenNum = 2;
                        cardMod = new UnplayableMod();
                        group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if (card.cost != -2) {
                                group.addToBottom(card);
                            }
                        }

                        if (AbstractDungeon.player.hasRelic(relic2.relicId)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = relic2;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        AbstractDungeon.gridSelectScreen.open(group, NUM_UNPLAYABLE, OPTIONS[9] + NUM_UNPLAYABLE + OPTIONS[10] + OPTIONS[12], false);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[14]);
                        screenNum = 2;
                        break;
                }
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

    private boolean HasPlayableCards() {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cost != -2) {
                count++;
            }
        }
        return count >= NUM_UNPLAYABLE;
    }

    private boolean HasPlayableCardsExcludeAutoplay() {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cost != -2 && !AutoplayField.autoplay.get(card) && !CardModifierManager.hasModifier(card, AutoplayMod.ID)) {
                count++;
            }
        }
        return count >= NUM_AUTOPLAY;
    }

}
