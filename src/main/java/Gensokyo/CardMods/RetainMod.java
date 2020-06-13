package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class RetainMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("RetainMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private boolean alreadyRetain = false;

    @Override
    public AbstractCardModifier makeCopy() {
        return new RetainMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.selfRetain) {
            card.selfRetain = true;
            alreadyRetain = false;
        } else {
            alreadyRetain = true;
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!alreadyRetain) {
            return TEXT[0] + rawDescription;
        }
        return rawDescription;
    }
}
