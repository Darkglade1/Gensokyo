package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class InnateMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("InnateMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public AbstractCardModifier makeCopy() {
        return new InnateMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.isInnate = true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(CommonKeywordIconsField.useIcons.get(card))) {
            return TEXT[0] + rawDescription;
        } else {
            return rawDescription;
        }
    }
}
