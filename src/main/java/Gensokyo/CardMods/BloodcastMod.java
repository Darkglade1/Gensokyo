package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BloodcastMod extends AbstractCardModifier implements AlternateCardCostModifier {

    public static final String ID = GensokyoMod.makeID("BloodcastMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public AbstractCardModifier makeCopy() {
        return new BloodcastMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }

    @Override
    public int getAlternateResource(AbstractCard card) {
        return AbstractDungeon.player.currentHealth;
    }

    @Override
    public boolean canSplitCost(AbstractCard card) {
        return true;
    }

    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend) {
        int resource = AbstractDungeon.player.currentHealth;
        if (resource > costToSpend) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, costToSpend));
            costToSpend = 0;
        } else if (resource > 0) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, resource));
            costToSpend -= resource;
        }
        return costToSpend;
    }
}
