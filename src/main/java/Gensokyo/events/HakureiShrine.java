package Gensokyo.events;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class HakureiShrine extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("HakureiShrine");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("HakureiShrine.png");

    private int screenNum = 0;

    private boolean hasEnoughMoney;
    private boolean hasMoney;

    public static int upgradeCost = 70;
    private static int upgradeAmount = 2;
    private static int healthCost;
    private int healthGain;

    public HakureiShrine() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractPlayer p = AbstractDungeon.player;
        int missingHP = p.maxHealth - p.currentHealth;
        int goldCost = missingHP * 2;
        if (goldCost > p.gold) {
            healthCost = p.gold;
            healthGain = p.gold / 2;
        } else {
            healthCost = goldCost;
            healthGain = missingHP;
        }

        if (p.gold >= upgradeCost) {
            hasEnoughMoney = true;
        }
        if (p.gold > 1) {
            hasMoney = true;
        }

        //Lose gold and upgrade cards
        if (AbstractDungeon.player.masterDeck.hasUpgradableCards()) {
            if (this.hasEnoughMoney) {
                this.imageEventText.setDialogOption(OPTIONS[1] + upgradeCost + OPTIONS[2] + upgradeAmount + OPTIONS[3]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[4] + upgradeCost + OPTIONS[5], true);
            }
        } else {
            this.imageEventText.setDialogOption(OPTIONS[10], true);
        }

        //Lose gold and gain HP
        if (missingHP == 0) {
            this.imageEventText.setDialogOption(OPTIONS[12], true);
        }
        else if (this.hasMoney) {
            this.imageEventText.setDialogOption(OPTIONS[6] + healthCost + OPTIONS[7] + healthGain + OPTIONS[8]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[9], true);
        }

        //Leave
        this.imageEventText.setDialogOption(OPTIONS[11]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Upgrade cards
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(this.upgradeCost);
                        this.upgradeCards();

                        break;
                    case 1: // Gain HP
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(this.healthCost);
                        AbstractDungeon.player.heal(this.healthGain);
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                this.imageEventText.clearRemainingOptions();
                screenNum = 2;
                break;
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void upgradeCards() {
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        ArrayList<AbstractCard> upgradableCards = new ArrayList();
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        List<String> cardMetrics = new ArrayList();
        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            if (upgradableCards.size() == 1) {
                upgradableCards.get(0).upgrade();
                cardMetrics.add(upgradableCards.get(0).cardID);
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));
            } else {
                upgradableCards.get(0).upgrade();
                upgradableCards.get(1).upgrade();
                cardMetrics.add(upgradableCards.get(0).cardID);
                cardMetrics.add(upgradableCards.get(1).cardID);
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - 190.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(1).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + 190.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
            }
        }
    }
}
