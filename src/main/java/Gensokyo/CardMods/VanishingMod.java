package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class VanishingMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("VanishingMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    protected int uses;
    boolean masterCardRemoved = false;

    public VanishingMod(int uses) {
        this.uses = uses;
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
        uses--;
        card.initializeDescription();
        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(masterCard, identifier(null));
            for (AbstractCardModifier mod : mods) {
                if (mod instanceof VanishingMod) {
                    VanishingMod vanishingMod = (VanishingMod)mod;
                    vanishingMod.uses = uses;
                }
            }
            masterCard.initializeDescription();
            if (uses == 0) {
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
                masterCardRemoved = true;
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new VanishingMod(uses);
    }
}