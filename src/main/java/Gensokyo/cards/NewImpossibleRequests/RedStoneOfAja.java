package Gensokyo.cards.NewImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RedStoneOfAja extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(RedStoneOfAja.class.getSimpleName());
    public static final String IMG = makeCardPath("RedStoneOfAja.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CARDS_PLAYED_GOAL = 10;
    private static final int UPGRADE_GOAL = 2;

    public RedStoneOfAja() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = CARDS_PLAYED_GOAL;
    }

    @Override
    public float getTitleFontSize()
    {
        return 18;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_GOAL);
            initializeDescription();
        }
    }
}
