package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.Evolve.AbstractEvolveCard;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class EvolveMod extends VanishingMod {

    public static final String ID = GensokyoMod.makeID("EvolveMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public EvolveMod(int uses) {
        super(uses);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[0] + uses + TEXT[1];
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        if (uses == 0) {
            if (card instanceof AbstractEvolveCard && masterCardRemoved) {
                AbstractEvolveCard evolveCard = (AbstractEvolveCard)card;
                evolveCard.triggerEvolve();
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EvolveMod(uses);
    }
}