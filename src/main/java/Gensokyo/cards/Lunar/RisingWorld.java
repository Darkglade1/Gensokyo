package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.CallbackScryAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.function.Consumer;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RisingWorld extends AbstractImpossibleRequestRewardCard {

    public static final String ID = GensokyoMod.makeID(RisingWorld.class.getSimpleName());
    public static final String IMG = makeCardPath("RisingWorld.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 0;
    private static final int SCRY = 3;
    private static final int UPGRADE_PLUS_SCRY = 1;
    private static final int DRAW = 1;

    public RisingWorld() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = SCRY;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Consumer<ArrayList<AbstractCard>> consumer = cards -> {
            for (AbstractCard card : cards) {
                card.upgrade();
            }
        };
        addToBot(new CallbackScryAction(magicNumber, consumer));
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new DrawCardAction(defaultSecondMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SCRY);
            initializeDescription();
        }
    }
}
