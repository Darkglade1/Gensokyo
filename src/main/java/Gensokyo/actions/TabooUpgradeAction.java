package Gensokyo.actions;

import Gensokyo.cards.FourOfAKind;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TabooUpgradeAction extends AbstractGameAction {
    private AbstractCard card;

    public TabooUpgradeAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    public void update() {
        this.card.magicNumber += this.amount;
        this.card.baseMagicNumber += this.amount;
        this.card.applyPowers();

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof FourOfAKind) {
                c.magicNumber += this.amount;
                c.baseMagicNumber += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof FourOfAKind) {
                c.magicNumber += this.amount;
                c.baseMagicNumber += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof FourOfAKind) {
                c.magicNumber += this.amount;
                c.baseMagicNumber += this.amount;
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}
