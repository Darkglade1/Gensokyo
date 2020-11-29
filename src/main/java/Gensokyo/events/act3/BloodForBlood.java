package Gensokyo.events.act3;

import Gensokyo.CardMods.BloodcastMod;
import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import java.util.ArrayList;
import java.util.function.Predicate;

import static Gensokyo.GensokyoMod.makeEventPath;

public class BloodForBlood extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("BloodForBlood");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Graveyard.png");

    private static final float HP_COST = 0.15f;
    private static final float A15_HP_COST = 0.20f;
    private static final int NUM_BLOODCAST = 1;
    private Predicate<AbstractCard> validCardTest = card -> card.cost >= 0 && !CardModifierManager.hasModifier(card, BloodcastMod.ID);

    private int screenNum = 0;
    private int hpCost;
    private AbstractCardModifier cardMod = new BloodcastMod();
    private ArrayList<AbstractCard> validCards;

    public BloodForBlood() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();

        if (AbstractDungeon.ascensionLevel < 15) {
            hpCost = (int) ((float) AbstractDungeon.player.maxHealth * HP_COST);
        } else {
            hpCost = (int) ((float) AbstractDungeon.player.maxHealth * A15_HP_COST);
        }
        validCards = getValidCards();
        if (validCards.size() >= NUM_BLOODCAST) {
            if (NUM_BLOODCAST == 1) {
                this.imageEventText.setDialogOption(OPTIONS[0] + hpCost + OPTIONS[1] + OPTIONS[2] + NUM_BLOODCAST + OPTIONS[3] + OPTIONS[5]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[0] + hpCost + OPTIONS[1] + OPTIONS[2] + NUM_BLOODCAST + OPTIONS[4] + OPTIONS[5]);
            }
            this.imageEventText.optionList.get(0).
        } else {
            this.imageEventText.setDialogOption(OPTIONS[10], true);
        }
        this.imageEventText.setDialogOption(OPTIONS[11]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.loadImage(makeEventPath("DarkMima.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[11]);
                        screenNum = 1;
                        AbstractDungeon.player.damage(new DamageInfo(null, hpCost));
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : validCards) {
                            group.addToBottom(card);
                        }
                        if (NUM_BLOODCAST == 1) {
                            AbstractDungeon.gridSelectScreen.open(group, NUM_BLOODCAST, OPTIONS[6] + NUM_BLOODCAST + OPTIONS[7] + OPTIONS[9], false);
                        } else {
                            AbstractDungeon.gridSelectScreen.open(group, NUM_BLOODCAST, OPTIONS[6] + NUM_BLOODCAST + OPTIONS[8] + OPTIONS[9], false);
                        }
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[11]);
                        screenNum = 1;
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

    private ArrayList<AbstractCard> getValidCards() {
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (validCardTest.test(card)) {
                validCards.add(card);
            }
        }
        return validCards;
    }

}
