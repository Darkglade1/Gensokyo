package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act2.DayTheSeaSplit;
import Gensokyo.relics.act2.MosesMiracle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class LivingGodOfMiracles extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("LivingGodOfMiracles");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sanae.png");

    private static final int GOLD_COST = 40;

    private int screenNum = 0;
    private boolean pickCard = false;

    public LivingGodOfMiracles() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0] + DayTheSeaSplit.COMBATS + OPTIONS[1], new MosesMiracle()); //Miracle
        if (AbstractDungeon.player.gold < GOLD_COST) {
            this.imageEventText.setDialogOption(OPTIONS[4] + GOLD_COST + OPTIONS[5], true);
        } else if (!AbstractDungeon.player.masterDeck.hasUpgradableCards()) {
            this.imageEventText.setDialogOption(OPTIONS[6], true);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[2] + GOLD_COST + OPTIONS[3]); // Donate
        }
        this.imageEventText.setDialogOption(OPTIONS[8]); //Leave
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Miracle
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 1;
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(DayTheSeaSplit.ID) || AbstractDungeon.player.hasRelic(MosesMiracle.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(DayTheSeaSplit.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Donate
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 1;
                        AbstractDungeon.player.loseGold(GOLD_COST);
                        this.pickCard = true;
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[7], true, false, false, false);
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 1;
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
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickCard = false;
        }
    }

}
