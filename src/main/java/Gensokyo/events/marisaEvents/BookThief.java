package Gensokyo.events.marisaEvents;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Aya;
import Gensokyo.relics.YoukaiFlower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeEventPath;

public class BookThief extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MarisaBookThief");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Yuuka.png");

    private static final int LIBRARY_SIZE = 10;

    private boolean pickCard = false;
    private AbstractCard pickedCard;

    private int screenNum = 0;

    public BookThief() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[3]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Take book
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);

                        this.pickCard = true;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                        for(int i = 0; i < LIBRARY_SIZE; ++i) {
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

                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[4], false);
                        break;
                    case 1: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Stand your ground
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Aya.ID);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(YoukaiFlower.ID).makeCopy());
                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        this.enterCombatFromImage();
                        break;
                    case 1: // Surrender the book
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.pickedCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.pickedCard);
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

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickedCard = c;
            this.imageEventText.clearRemainingOptions();
            this.imageEventText.setDialogOption(OPTIONS[2] + pickedCard.name + ".", pickedCard.makeCopy());
        }

    }
}
