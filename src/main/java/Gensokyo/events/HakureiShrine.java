package Gensokyo.events;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class HakureiShrine extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("HakureiShrine");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("HakureiShrine.png");

    private int screenNum = 0;

    private boolean pickCard;

    private boolean hasEnoughMoney;
    private boolean hasMoney;

    private int upgradeCost = 90;
    private int upgradeAmount = 2;
    private int healthCost;
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
        if (this.hasEnoughMoney) {
            this.imageEventText.setDialogOption(OPTIONS[1] + upgradeCost + OPTIONS[2] + upgradeAmount + OPTIONS[3]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + upgradeCost + OPTIONS[5], true);
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
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), upgradeAmount, OPTIONS[10], true, false, false, false);
                        }
                        this.pickCard = true;

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
                switch (buttonPressed) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractDungeon.gridSelectScreen.selectedCards.get(i).upgrade();
                AbstractCard upgCard = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                AbstractDungeon.player.bottledCardUpgradeCheck(upgCard);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickCard = false;
        }

    }
}
