package Gensokyo.events;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ScarletDevilMansion extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ScarletDevilMansion");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("SDM.png");

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;
    private boolean pickCard = false;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.20F; // 20%
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.25F; // 25%

    private int healthdamage;

    public ScarletDevilMansion() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }

        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[0]);

    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.loadImage(makeEventPath("Sakuya.png"));
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1]); //Go to library
                this.imageEventText.setDialogOption(OPTIONS[2] + healthdamage + OPTIONS[3]); // Make donation
                screenNum = 2;
                break;
            case 2:
                switch (buttonPressed) {
                    case 0: //Go to library
                        this.imageEventText.loadImage(makeEventPath("Patchy.png"));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        this.pickCard = true;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                        for(int i = 0; i < 10; ++i) {
                            AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                            boolean containsDupe = true;

                            while(true) {
                                while(containsDupe) {
                                    containsDupe = false;
                                    Iterator var6 = group.group.iterator();

                                    while(var6.hasNext()) {
                                        AbstractCard c = (AbstractCard)var6.next();
                                        if (c.cardID.equals(card.cardID)) {
                                            containsDupe = true;
                                            card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                                            break;
                                        }
                                    }
                                }

                                if (group.contains(card)) {
                                    --i;
                                } else {
                                    card.upgrade();
                                    group.addToBottom(card);
                                }
                                break;
                            }
                        }

                        Iterator var8 = group.group.iterator();

                        while(var8.hasNext()) {
                            AbstractCard c = (AbstractCard)var8.next();
                            UnlockTracker.markCardAsSeen(c.cardID);
                        }

                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[5], false);

                        break;
                    case 1: // Make donation
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        screenNum = 4;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        // Shake the screen
                        CardCrawlGame.sound.play("ATTACK_DAGGER_1");  // Play a hit sound
                        AbstractDungeon.player.damage(new DamageInfo(null, healthdamage));
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);

                        break;
                }
                break;
            case 3:
                this.openMap();
                break;
            case 4:
                this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                break;
            default:
                this.openMap();
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();
            c.upgrade();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

    }

}
