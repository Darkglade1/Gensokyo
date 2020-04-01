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
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ExtraExtra extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("ExtraExtra");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Aya.png");

    private static final int NUM_UNCOMMONS = 10;
    private static final int NUM_COMMONS = 20;

    private int screenNum = 0;
    private boolean pickCard = false;

    private int minUncommonSize;
    private int minCommonSize;

    public ExtraExtra() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        minUncommonSize = Math.min(NUM_UNCOMMONS, AbstractDungeon.uncommonCardPool.group.size());
        minCommonSize = Math.min(NUM_COMMONS, AbstractDungeon.commonCardPool.group.size());
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[1] + minUncommonSize + OPTIONS[2]);
                imageEventText.setDialogOption(OPTIONS[1] + minCommonSize + OPTIONS[3]);
                imageEventText.setDialogOption(OPTIONS[4]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        this.pickCard = true;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                        for(int i = 0; i < minUncommonSize; ++i) {
                            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).makeCopy();
                            boolean containsDupe = true;

                            while(true) {
                                while(containsDupe) {
                                    containsDupe = false;

                                    for (AbstractCard c : group.group) {
                                        if (c.cardID.equals(card.cardID)) {
                                            containsDupe = true;
                                            card = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).makeCopy();
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
                        for (AbstractCard card : group.group) {
                            UnlockTracker.markCardAsSeen(card.cardID);
                        }
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[6], false);

                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        this.pickCard = true;
                        group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for(int i = 0; i < minCommonSize; ++i) {
                            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
                            boolean containsDupe = true;

                            while(true) {
                                while(containsDupe) {
                                    containsDupe = false;

                                    for (AbstractCard c : group.group) {
                                        if (c.cardID.equals(card.cardID)) {
                                            containsDupe = true;
                                            card = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
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
                        for (AbstractCard card : group.group) {
                            UnlockTracker.markCardAsSeen(card.cardID);
                        }
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[6], false);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
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

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

}
