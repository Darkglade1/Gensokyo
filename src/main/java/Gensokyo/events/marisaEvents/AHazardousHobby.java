package Gensokyo.events.marisaEvents;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.marisaRelics.ImprobabilityPotion;
import Gensokyo.relics.marisaRelics.IngredientList;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AHazardousHobby extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MarisaAHazardousHobby");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Kourindou.png");

    private int screenNum = 0;
    AbstractCard cardOption;
    private static final int GOLD = 150;

    public AHazardousHobby() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        this.cardOption = this.getRandomNonBasicCard();
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 2;
                this.imageEventText.clearAllDialogs();
                if (this.cardOption != null) {
                    this.imageEventText.setDialogOption(OPTIONS[1] + this.cardOption.name + OPTIONS[3] + GOLD + OPTIONS[4], this.cardOption.makeStatEquivalentCopy());
                    this.imageEventText.setDialogOption(OPTIONS[2] + this.cardOption.name + OPTIONS[5], this.cardOption.makeStatEquivalentCopy());
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[7], true);
                }
                this.imageEventText.setDialogOption(OPTIONS[6]);
                break;
            case 2:
                switch (buttonPressed) {
                    case 0: //Money
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.cardOption));
                        AbstractDungeon.player.masterDeck.removeCard(this.cardOption);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        break;
                    case 1: // Recipe
                        if (this.cardOption != null) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                            screenNum = 3;
                            this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.effectList.add(new PurgeCardEffect(this.cardOption));
                            AbstractDungeon.player.masterDeck.removeCard(this.cardOption);
                            AbstractRelic relic;
                            if (AbstractDungeon.player.hasRelic(IngredientList.ID) || AbstractDungeon.player.hasRelic(ImprobabilityPotion.ID)) {
                                relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                            } else {
                                relic = RelicLibrary.getRelic(IngredientList.ID).makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        } else {
                            Leave();
                        }
                        break;
                    case 2:
                        Leave();
                }
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void Leave() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
        screenNum = 3;
        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
        this.imageEventText.clearRemainingOptions();
    }

    private AbstractCard getRandomNonBasicCard() {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.rarity != AbstractCard.CardRarity.BASIC && c.type != AbstractCard.CardType.CURSE) {
                list.add(c);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
            return list.get(0);
        }
    }

    public static boolean hasRandomNonBasicCard() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.rarity != AbstractCard.CardRarity.BASIC && c.type != AbstractCard.CardType.CURSE) {
                return true;
            }
        }
        return false;
    }
}
