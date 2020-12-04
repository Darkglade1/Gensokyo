package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DamageMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("DamageMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int damageAmt;

    public DamageMod(int damageAmt) {
        this.damageAmt = damageAmt;
        this.priority = 1;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DamageMod(damageAmt);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage = damageAmt;
        card.target = AbstractCard.CardTarget.ENEMY;
        card.type = AbstractCard.CardType.ATTACK;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[2];
    }
}
