package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ALandWhereOnlyIAmMissing extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ALandWhereOnlyIAmMissing");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Koishi.png");

    private static final float HEALTH_LOSS_PERCENTAGE = 0.20F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.25F;

    private boolean hasCards;
    private boolean has2Cards;
    private boolean has3Cards;
    private int healthdamage;

    private int screenNum = 0;

    public ALandWhereOnlyIAmMissing() {
        super(NAME, DESCRIPTIONS[0], IMG);
        if (AbstractDungeon.ascensionLevel < 15) {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }
        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() >= 3) {
            has3Cards = true;
        }
        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() >= 2) {
            has2Cards = true;
        }
        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() >= 1) {
            hasCards = true;
        }
        if (!hasCards) {
            imageEventText.setDialogOption(OPTIONS[7]);
        } else {
            imageEventText.setDialogOption(OPTIONS[0]);
            if (has2Cards) {
                imageEventText.setDialogOption(OPTIONS[1] + healthdamage + OPTIONS[3]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[5], true);
            }
            if (has3Cards) {
                imageEventText.setDialogOption(OPTIONS[2] + (2 * healthdamage) + OPTIONS[4]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[6], true);
            }
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Play for a little bit
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[11]);
                        screenNum = 1;
                        if (hasCards) {
                            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[8], false);
                        }
                        break;
                    case 1: // Play for some time
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[11]);
                        screenNum = 1;
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.damage(new DamageInfo(null, healthdamage));
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 2, OPTIONS[9], false);
                        break;
                    case 2: // Play for a long time
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[11]);
                        screenNum = 1;
                        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                        AbstractDungeon.player.damage(new DamageInfo(null, 2 * healthdamage));
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 3, OPTIONS[10], false);
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
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
                AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
            }
        }

    }

}
