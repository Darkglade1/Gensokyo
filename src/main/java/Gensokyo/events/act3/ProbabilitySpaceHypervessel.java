package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ProbabilitySpaceHypervessel extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("ProbabilitySpaceHypervessel");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Yumemi.png");

    private int screenNum = 0;

    private static final int NUM_UPGRADES = 1;
    private static final int CARDS_TO_TRANSFORM = 1;
    private static final int NUM_POTIONS = 2;
    private boolean cardsSelected = false;

    public ProbabilitySpaceHypervessel() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0] + NUM_UPGRADES + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3] + NUM_POTIONS + OPTIONS[4]);
        this.noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Upgrade card
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        upgradeCards();
                        break;
                    case 1: // Transform card
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        this.transform();
                        break;
                    case 2: // Obtain Potions
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        for (int i = 0; i < NUM_POTIONS; i++) {
                            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionHelper.getRandomPotion()));
                        }
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }

    private void upgradeCards() {
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            for (int i = 0; i < NUM_UPGRADES; i++) {
                upgradableCards.get(i).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(i).makeStatEquivalentCopy()));
            }
        }
    }

    public void update() {
        super.update();
        if (!this.cardsSelected) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == CARDS_TO_TRANSFORM) {
                this.cardsSelected = true;
                float displayCount = 0.0F;
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    card.untip();
                    card.unhover();
                    AbstractDungeon.player.masterDeck.removeCard(card);
                    AbstractCard c = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
                    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && c != null) {
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(c.makeCopy(), (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F, false));
                        displayCount += (float) Settings.WIDTH / 6.0F;
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }
        }
    }

    private void transform() {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), CARDS_TO_TRANSFORM, OPTIONS[5], false, false, false, false);
    }
}
