package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.patches.ReversalEventPatches;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class OneWingedWhiteHeron extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("OneWingedWhiteHeron");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sagume.png");

    private int screenNum = 0;

    private ReversalEventPatches.ReversalRewardItem eliteReward;
    private ReversalEventPatches.ReversalRewardItem normalReward;
    private AbstractCard card;
    private AbstractRelic relic;


    public OneWingedWhiteHeron() {
        super(NAME, DESCRIPTIONS[0], IMG);
        setRewards();
        this.imageEventText.setDialogOption(OPTIONS[9]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                screenNum = 1;

                if (eliteReward == null) {
                    this.imageEventText.setDialogOption(OPTIONS[7], true);
                } else {
                    if (relic != null) {
                        this.imageEventText.setDialogOption(OPTIONS[0] + eliteReward.monsterName + OPTIONS[1] + eliteReward.heal + OPTIONS[2] + OPTIONS[3] + FontHelper.colorString(relic.name, "r") + OPTIONS[4]);
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[0] + eliteReward.monsterName + OPTIONS[1] + eliteReward.heal + OPTIONS[2]);
                    }
                }
                if (normalReward == null) {
                    this.imageEventText.setDialogOption(OPTIONS[8], true);
                } else {
                    if (card != null) {
                        this.imageEventText.setDialogOption(OPTIONS[0] + normalReward.monsterName + OPTIONS[1] + normalReward.heal + OPTIONS[2] + OPTIONS[3] + FontHelper.colorString("" + normalReward.gold, "r") + OPTIONS[5] + OPTIONS[3] + FontHelper.colorString(card.name, "r") + OPTIONS[4], card.makeStatEquivalentCopy());
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[0] + normalReward.monsterName + OPTIONS[1] + normalReward.heal + OPTIONS[2] + OPTIONS[3] + FontHelper.colorString("" + normalReward.gold, "r") + OPTIONS[5]);
                    }
                }
                this.imageEventText.setDialogOption(OPTIONS[10]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.heal(eliteReward.heal);
                        if (relic != null) {
                            AbstractDungeon.player.loseRelic(relic.relicId);
                        }
                        ReversalEventPatches.eliteEncounters.remove(eliteReward);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.heal(normalReward.heal);
                        AbstractDungeon.player.loseGold(normalReward.gold);
                        if (card != null) {
                            AbstractDungeon.effectList.add(new PurgeCardEffect(card));
                            AbstractDungeon.player.masterDeck.removeCard(card);
                        }
                        ReversalEventPatches.normalEncounters.remove(normalReward);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
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


    private void setRewards() {
        int biggestHeal = -1;
        for (ReversalEventPatches.ReversalRewardItem reward : ReversalEventPatches.eliteEncounters) {
            if (reward.heal > biggestHeal) {
                eliteReward = reward;
                biggestHeal = reward.heal;
            }
        }
        biggestHeal = -1;
        for (ReversalEventPatches.ReversalRewardItem reward : ReversalEventPatches.normalEncounters) {
            if (reward.heal > biggestHeal) {
                normalReward = reward;
                biggestHeal = reward.heal;
            }
        }
        if (normalReward != null) {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card.cardID.equals(normalReward.cardID)) {
                    this.card = card;
                }
            }
        }
        if (eliteReward != null) {
            if (AbstractDungeon.player.hasRelic(eliteReward.relicID)) {
                relic = AbstractDungeon.player.getRelic(eliteReward.relicID);
            }
        }
    }

    public static boolean canSpawn() {
        if ((AbstractDungeon.player.currentHealth < ((int)(AbstractDungeon.player.maxHealth * 0.60F))) && ReversalEventPatches.eliteEncounters.size() >= 1 && ReversalEventPatches.normalEncounters.size() >= 2) {
            return true;
        }
        return false;
    }
}
