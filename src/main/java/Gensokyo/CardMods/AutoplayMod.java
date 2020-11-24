package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AutoplayMod extends AbstractCardModifier {

    public static final String ID = GensokyoMod.makeID("AutoplayMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public AbstractCardModifier makeCopy() {
        return new AutoplayMod();
    }

    @Override
    public void onDrawn(AbstractCard card) {
        AbstractDungeon.actionManager.addToBottom(new NewQueueCardAction(card, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng)));
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }
}
