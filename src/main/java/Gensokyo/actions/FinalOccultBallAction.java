package Gensokyo.actions;

import Gensokyo.cards.Apocalypse;
import Gensokyo.cards.EightFeetTall;
import Gensokyo.cards.GapWoman;
import Gensokyo.cards.HAARP;
import Gensokyo.cards.Kunekune;
import Gensokyo.cards.LittleGreenMen;
import Gensokyo.cards.LochNessMonster;
import Gensokyo.cards.ManorOfTheDishes;
import Gensokyo.cards.MenInBlack;
import Gensokyo.cards.MissMary;
import Gensokyo.cards.MonkeysPaw;
import Gensokyo.cards.RedCapeBlueCape;
import Gensokyo.cards.SevenSchoolMysteries;
import Gensokyo.cards.SlitMouthedWoman;
import Gensokyo.cards.SpontaneousHumanCombustion;
import Gensokyo.cards.TekeTeke;
import Gensokyo.cards.TurboGranny;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FinalOccultBallAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;

    public FinalOccultBallAction(int numberOfCards, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }

    public FinalOccultBallAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.numberOfCards > 0) {
                CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                temp.addToTop(new MissMary());
                temp.addToTop(new SpontaneousHumanCombustion());
                temp.addToTop(new RedCapeBlueCape());
                temp.addToTop(new TekeTeke());
                temp.addToTop(new Kunekune());
                temp.addToTop(new HAARP());
                temp.addToTop(new MenInBlack());
                temp.addToTop(new LittleGreenMen());
                temp.addToTop(new TurboGranny());
                temp.addToTop(new MonkeysPaw());
                temp.addToTop(new ManorOfTheDishes());
                temp.addToTop(new LochNessMonster());
                temp.addToTop(new GapWoman());
                temp.addToTop(new EightFeetTall());
                temp.addToTop(new SevenSchoolMysteries());
                temp.addToTop(new Apocalypse());
                temp.addToTop(new SlitMouthedWoman());
                for (AbstractCard card : temp.group) {
                    card.upgrade();
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                if (this.numberOfCards == 1) {
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false);
                    }
                } else if (this.optional) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
                } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
                }

                this.tickDuration();
                
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(card, false));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Gensokyo:OccultBallAction").TEXT;
    }
}
