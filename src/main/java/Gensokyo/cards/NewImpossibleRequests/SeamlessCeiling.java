package Gensokyo.cards.NewImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SeamlessCeiling extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(SeamlessCeiling.class.getSimpleName());
    public static final String IMG = makeCardPath("SeamlessCeiling.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CARDS_GOAL = 7;
    private static final int UPGRADE_GOAL = 1;

    public SeamlessCeiling() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = CARDS_GOAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public float getTitleFontSize()
    {
        return 14;
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
