package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class EtherealMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("EtherealMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private boolean alreadyEthereal = false;

    @Override
    public AbstractCardModifier makeCopy() {
        return new EtherealMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.isEthereal) {
            card.isEthereal = true;
            alreadyEthereal = false;
        } else {
            alreadyEthereal = true;
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!alreadyEthereal) {
            return TEXT[0] + rawDescription;
        }
        return rawDescription;
    }
}
