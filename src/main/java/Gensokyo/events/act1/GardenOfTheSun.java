package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act1.YoukaiFlower;
import Gensokyo.util.RelicUtils;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.HappyFlower;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class GardenOfTheSun extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("GardenOfTheSun");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Yuuka.png");

    private static final float HEALTH_LOSS_PERCENTAGE = 0.10F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.13F;
    private static final int damageThreshold = 13;

    private int healthdamage;

    private boolean hasStrongCard;
    private AbstractCard strongCard;

    private boolean hasCard;
    private AbstractCard card;

    private int screenNum = 0;

    public GardenOfTheSun() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        if (AbstractDungeon.ascensionLevel < 15) {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }
        setCards();
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                if (this.hasStrongCard) {
                    this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(this.strongCard.name, "r") + "." + OPTIONS[2], this.strongCard.makeStatEquivalentCopy());
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[5] + damageThreshold + OPTIONS[6], true);
                    if (this.hasCard) {
                        this.imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(this.card.name, "r") + "." + OPTIONS[4], this.card.makeStatEquivalentCopy());
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[7], true);
                    }
                }
                this.imageEventText.setDialogOption(OPTIONS[8] + healthdamage + OPTIONS[9]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Demonstrate a powerful attack
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[10]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.strongCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.strongCard);
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(YoukaiFlower.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(YoukaiFlower.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        break;
                    case 1: // Demonstrate your best attack OR Run, depending on scenario
                        if (!this.hasStrongCard) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            screenNum = 2;
                            this.imageEventText.updateDialogOption(0, OPTIONS[10]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.effectList.add(new PurgeCardEffect(this.card));
                            AbstractDungeon.player.masterDeck.removeCard(this.card);
                            AbstractRelic relic2;
                            if (AbstractDungeon.player.hasRelic(HappyFlower.ID)) {
                                relic2 = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                            } else {
                                relic2 = RelicLibrary.getRelic(HappyFlower.ID).makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic2);
                            RelicUtils.removeRelicFromPool(relic2);
                        } else {
                            Run();
                        }
                        break;
                    case 2: // Run
                        Run();
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

    private void Run() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[10]);
        this.imageEventText.clearRemainingOptions();
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
        // Shake the screen
        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
        AbstractDungeon.player.damage(new DamageInfo(null, healthdamage));
    }

    private void setCards() {
        AbstractCard highestDamageCard = null;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (highestDamageCard == null) {
                if (card.baseDamage > 0 && !card.tags.contains(BaseModCardTags.BASIC_STRIKE)) {
                    highestDamageCard = card;
                }
            } else {
                if (card.baseDamage > highestDamageCard.baseDamage && !card.tags.contains(BaseModCardTags.BASIC_STRIKE)) {
                    highestDamageCard = card;
                }
            }
        }
        if (highestDamageCard == null) {
            hasCard = false;
            hasStrongCard = false;
        } else {
            if (highestDamageCard.baseDamage >= damageThreshold) {
                hasStrongCard = true;
                strongCard = highestDamageCard;
            } else {
                hasCard = true;
                card = highestDamageCard;
            }
        }
    }

    public static boolean hasNonStrikeAttack() {
        for (AbstractCard card: AbstractDungeon.player.masterDeck.group) {
            if (card.baseDamage > 0 && !card.tags.contains(BaseModCardTags.BASIC_STRIKE)) {
                return true;
            }
        }
        return false;
    }
}
