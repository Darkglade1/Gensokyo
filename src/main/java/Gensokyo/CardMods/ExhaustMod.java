package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ExhaustMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("ExhaustMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private boolean alreadyExhaust = false;

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExhaustMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.exhaust) {
            card.exhaust = true;
            alreadyExhaust = false;
        } else {
            alreadyExhaust = true;
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!alreadyExhaust && !(CommonKeywordIconsField.useIcons.get(card))) {
            return rawDescription + TEXT[0];
        }
        return rawDescription;
    }
}
