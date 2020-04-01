package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Predicate;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MindShatter extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(MindShatter.class.getSimpleName());
    public static final String IMG = makeCardPath("Dizzy.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int HP_PER_CARD = 2;

    public MindShatter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HP_PER_CARD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Predicate<AbstractCard> predicate = abstractCard -> {
            if (abstractCard.exhaust) {
                return false;
            } else {
                return true;
            }
        };
        AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(AbstractDungeon.player.drawPile, AbstractDungeon.player.exhaustPile, predicate, AbstractDungeon.player.exhaustPile.size(), cards -> AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, cards.size() * magicNumber))));
    }

    @Override
    public void upgrade() {
    }
}
