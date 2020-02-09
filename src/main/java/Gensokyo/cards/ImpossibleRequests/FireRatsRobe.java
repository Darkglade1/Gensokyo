package Gensokyo.cards.ImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class FireRatsRobe extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(FireRatsRobe.class.getSimpleName());
    public static final String IMG = makeCardPath("FireRatRobe.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CARDS_DRAWN_GOAL = 7;

    public FireRatsRobe() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = CARDS_DRAWN_GOAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void upgrade() {

    }
}
