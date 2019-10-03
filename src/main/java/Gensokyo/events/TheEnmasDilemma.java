package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.Justice;
import Gensokyo.relics.Mercy;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeEventPath;

public class TheEnmasDilemma extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("TheEnmasDilemma");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Enma.png");

    private boolean attack;
    private boolean skill;
    private AbstractCard attackCard;
    private AbstractCard skillCard;

    private int screenNum = 0;

    public TheEnmasDilemma() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        this.setCards();
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                if (this.attack) {
                    this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(this.attackCard.name, "r") + "." + OPTIONS[5], this.attackCard.makeStatEquivalentCopy());
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[2], true);
                }
                if (this.skill) {
                    this.imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(this.skillCard.name, "r") + "." + OPTIONS[5], this.skillCard.makeStatEquivalentCopy());
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[4], true);
                }
                this.imageEventText.setDialogOption(OPTIONS[6]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: //Mercy
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.attackCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.attackCard);
                        AbstractRelic relic;
                        if (AbstractDungeon.player.hasRelic(Mercy.ID)) {
                            relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic = RelicLibrary.getRelic(Mercy.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);

                        break;
                    case 1: // Justice
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.skillCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.skillCard);
                        AbstractRelic relic2;
                        if (AbstractDungeon.player.hasRelic(Justice.ID)) {
                            relic2 = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        } else {
                            relic2 = RelicLibrary.getRelic(Justice.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic2);

                        break;
                    case 2: // It is not my place to say
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();

                        break;
                }
                break;
            case 2:
                switch (buttonPressed) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    private boolean hasCardWithTypeAndWithoutTag(AbstractCard.CardType type, AbstractCard.CardTags tag) {
        Iterator var1 = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            c = (AbstractCard)var1.next();
        } while(c.type != type || c.tags.contains(tag));

        return true;
    }

    private AbstractCard returnCardOfTypeWithoutTag(AbstractCard.CardType type, Random rng, AbstractCard.CardTags tag) {
        ArrayList<AbstractCard> cards = new ArrayList();
        Iterator var3 = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            if (c.type == type && !c.tags.contains(tag)) {
                cards.add(c);
            }
        }

        return cards.remove(rng.random(cards.size() - 1));
    }

    private void setCards() {
        this.attack = this.hasCardWithTypeAndWithoutTag(AbstractCard.CardType.ATTACK, BaseModCardTags.BASIC_STRIKE);
        this.skill = this.hasCardWithTypeAndWithoutTag(AbstractCard.CardType.SKILL, BaseModCardTags.BASIC_DEFEND);
        if (this.attack) {
            this.attackCard = this.returnCardOfTypeWithoutTag(AbstractCard.CardType.ATTACK, AbstractDungeon.miscRng, BaseModCardTags.BASIC_STRIKE);
        }

        if (this.skill) {
            this.skillCard = this.returnCardOfTypeWithoutTag(AbstractCard.CardType.SKILL, AbstractDungeon.miscRng, BaseModCardTags.BASIC_DEFEND);
        }
    }

}
