package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class WeakMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("WeakMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int weakAmt;

    public WeakMod(int weakAmt) {
        this.weakAmt = weakAmt;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new WeakMod(weakAmt);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, weakAmt, false), weakAmt));
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.target = AbstractCard.CardTarget.ENEMY;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[2] + weakAmt + TEXT[3];
    }
}
