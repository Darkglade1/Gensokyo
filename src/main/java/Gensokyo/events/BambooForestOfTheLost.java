package Gensokyo.events;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeEventPath;

public class BambooForestOfTheLost extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("BambooForestOfTheLost");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("BambooForest.png");

    private static final float HP_HEAL_PERCENT = 0.30F;
    private static final float A_15_HP_HEAL_PERCENT = 0.25F;
    private int healAmt;
    private int numCards = 2;

    private int screenNum = 0;

    public BambooForestOfTheLost() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.healAmt = MathUtils.round((float)AbstractDungeon.player.maxHealth * A_15_HP_HEAL_PERCENT);
        } else {
            this.healAmt = MathUtils.round((float)AbstractDungeon.player.maxHealth * HP_HEAL_PERCENT);
        }
        imageEventText.setDialogOption(OPTIONS[1] + healAmt + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3] + numCards + OPTIONS[4]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Eientei
                        this.imageEventText.loadImage(makeEventPath("Eirin.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.heal(this.healAmt);
                        break;
                    case 1: // Human Village
                        this.imageEventText.loadImage(makeEventPath("Keine.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        for (int i = 0; i < numCards; i++) {
                            RewardItem reward = new RewardItem();
                            AbstractDungeon.getCurrRoom().addCardReward(reward);
                        }
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
