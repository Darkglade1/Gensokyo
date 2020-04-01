package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act1.OccultBall;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ClashOfLegends extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ClashOfLegends");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Clash.png");

    private int maxHpLoss;
    private static final float MAX_HP_LOSS = 0.10F;
    private static final float A15_MAX_HP_LOSS = 0.12F;

    private int screenNum = 0;

    public ClashOfLegends() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        this.noCardsInRewards = true;
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * A15_MAX_HP_LOSS);
        } else {
            this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * MAX_HP_LOSS);
        }
        this.maxHpLoss = Math.max(this.maxHpLoss, 1); //Ensures the player loses a minimum of 1 Max HP.
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1] + this.maxHpLoss + OPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[3]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Embrace the power
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        RewardItem reward = new RewardItem();
                        reward.cards = returnUrbanLegendCardReward(AbstractDungeon.cardRandomRng);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addCardReward(reward);
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                    case 1: // Refuse
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
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

    public static ArrayList<AbstractCard> returnUrbanLegendCardReward(Random rng) {
        ArrayList<AbstractCard> list = OccultBall.getAllUrbanLegends();
        Collections.shuffle(list, rng.random);

        int numCards = 3;
        AbstractRelic r;
        for(Iterator var2 = AbstractDungeon.player.relics.iterator(); var2.hasNext(); numCards = r.changeNumberOfCardsInReward(numCards)) {
            r = (AbstractRelic)var2.next();
        }
        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }
        for (AbstractCard c : list) {
            if (c.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasRelic(MoltenEgg2.ID)) {
                c.upgrade();
            } else if (c.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasRelic(ToxicEgg2.ID)) {
                c.upgrade();
            } else if (c.type == AbstractCard.CardType.POWER && AbstractDungeon.player.hasRelic(FrozenEgg2.ID)) {
                c.upgrade();
            }
        }
        return new ArrayList<>(list.subList(0, numCards));
    }
}
