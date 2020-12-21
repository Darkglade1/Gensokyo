package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("DrawMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int drawAmt;

    public DrawMod(int drawAmt) {
        this.drawAmt = drawAmt;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DrawMod(drawAmt);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(drawAmt));
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (drawAmt == 1) {
            return rawDescription + TEXT[2] + drawAmt + TEXT[3];
        } else {
            return rawDescription + TEXT[2] + drawAmt + TEXT[4];
        }
    }
}
