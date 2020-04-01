package Gensokyo.events.act1.marisaEvents;

import Gensokyo.GensokyoMod;
import ThMod.cards.Marisa.AlicesGift;
import ThMod.patches.CardTagEnum;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Gensokyo.GensokyoMod.makeEventPath;

public class Walpurgisnacht extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MarisaWalpurgisnacht");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Magician.png");

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;

    private int hpLoss;
    private static final float MAX_HP_PERCENT = 0.30F;
    private static final float MAX_HP_PERCENT_HIGH_ASC = 0.35F;
    private static final int CARDS_TO_TRANSFORM = 1;
    private AbstractCard aliceCard;
    private boolean cardsSelected = false;

    public Walpurgisnacht() {
        super(NAME, DESCRIPTIONS[0], IMG);
        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[0]);

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.hpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_PERCENT_HIGH_ASC);
        } else {
            this.hpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_PERCENT);
        }
        aliceCard = new AlicesGift();
        aliceCard.upgrade();
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1] + this.hpLoss + OPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[3], aliceCard);
                this.imageEventText.setDialogOption(OPTIONS[4]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Offer to go first
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("ATTACK_FIRE");  // Play a hit sound
                        AbstractDungeon.player.damage(new DamageInfo(null, hpLoss));
                        upgradeAllSparks();
                        break;
                    case 1: // Let Alice go first
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(aliceCard, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 2: // Let Patchy go first
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        this.transform();
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

    private void upgradeAllSparks() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.tags.contains(CardTagEnum.SPARK) && c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH, MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT));
            }
        }
    }

    public void update() {
        super.update();
        if (!this.cardsSelected) {
            List<String> transformedCards = new ArrayList();
            List<String> obtainedCards = new ArrayList();
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == CARDS_TO_TRANSFORM) {
                this.cardsSelected = true;
                float displayCount = 0.0F;
                Iterator i = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(i.hasNext()) {
                    AbstractCard card = (AbstractCard)i.next();
                    card.untip();
                    card.unhover();
                    transformedCards.add(card.cardID);
                    AbstractDungeon.player.masterDeck.removeCard(card);
                    AbstractCard c =  AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
                    obtainedCards.add(c.cardID);
                    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && c != null) {
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(c.makeCopy(), (float)Settings.WIDTH / 3.0F + displayCount, (float)Settings.HEIGHT / 2.0F, false));
                        displayCount += (float)Settings.WIDTH / 6.0F;
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }
        }
    }

    private void transform() {
        if (!AbstractDungeon.isScreenUp) {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), CARDS_TO_TRANSFORM, OPTIONS[7], false, false, false, false);
        } else {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), CARDS_TO_TRANSFORM, OPTIONS[7], false, false, false, false);
        }

    }
}
