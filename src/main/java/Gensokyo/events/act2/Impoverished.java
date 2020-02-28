package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AllTheWorldsEvil;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class Impoverished extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("Impoverished");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Shion.png");

    private int screenNum = 0;

    public static final int UPGRADE_COST = 100;
    private int upgradeAmount;

    public Impoverished() {
        super(NAME, DESCRIPTIONS[0], IMG);
        upgradeAmount = 1 + AbstractDungeon.player.gold / UPGRADE_COST;
        if (upgradeAmount == 1) {
            this.imageEventText.setDialogOption(OPTIONS[0] + AbstractDungeon.player.gold + OPTIONS[1] + upgradeAmount + OPTIONS[2]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[0] + AbstractDungeon.player.gold + OPTIONS[1] + upgradeAmount + OPTIONS[3]);
        }
        this.imageEventText.setDialogOption(OPTIONS[4], new AllTheWorldsEvil());
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Offer gold
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
                        this.upgradeCards();
                        break;
                    case 1: // Ignore her
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractCard curse = new AllTheWorldsEvil();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
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

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            for (int i = 0; i < this.upgradeAmount; i++) {
                if (i < upgradableCards.size()) {
                    upgradableCards.get(i).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(i).makeStatEquivalentCopy()));
                }
            }
        }
    }
}
