package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CostMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("CostMod");
    private int cost;

    public CostMod(int cost) {
        this.cost = cost;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CostMod(cost);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.cost = cost;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
