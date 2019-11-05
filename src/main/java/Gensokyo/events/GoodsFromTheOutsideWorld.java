package Gensokyo.events;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Gensokyo.GensokyoMod.makeEventPath;

public class GoodsFromTheOutsideWorld extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("GoodsFromTheOutsideWorld");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Rinnosuke.png");

    private int screenNum = 0;

    private boolean hasEnoughMoneyCommon;
    private boolean hasEnoughMoneyUncommon;
    private boolean hasEnoughMoneyRare;

    private int commonCost = 60;
    private int uncommonCost = 85;
    private int rareCost = 110;

    public GoodsFromTheOutsideWorld() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractPlayer p = AbstractDungeon.player;

        if (p.gold >= commonCost) {
            hasEnoughMoneyCommon = true;
        }
        if (p.gold >= uncommonCost) {
            hasEnoughMoneyUncommon = true;
        }
        if (p.gold >= rareCost) {
            hasEnoughMoneyRare = true;
        }


        if (this.hasEnoughMoneyCommon) {
            this.imageEventText.setDialogOption(OPTIONS[0] + commonCost + OPTIONS[1]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + commonCost + OPTIONS[5], true);
        }

        if (this.hasEnoughMoneyUncommon) {
            this.imageEventText.setDialogOption(OPTIONS[0] + uncommonCost + OPTIONS[2]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + uncommonCost + OPTIONS[5], true);
        }

        if (this.hasEnoughMoneyRare) {
            this.imageEventText.setDialogOption(OPTIONS[0] + rareCost + OPTIONS[3]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + rareCost + OPTIONS[5], true);
        }

        //Leave
        this.imageEventText.setDialogOption(OPTIONS[6]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractRelic relic;
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Buy common relic
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(this.commonCost);
                        relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);

                        break;
                    case 1: // Buy uncommon relic
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(this.uncommonCost);
                        relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 2: // Buy rare relic
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(this.rareCost);
                        relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 3: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
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
}
