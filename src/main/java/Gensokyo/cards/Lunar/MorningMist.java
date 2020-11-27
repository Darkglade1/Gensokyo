package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MorningMist extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(MorningMist.class.getSimpleName());
    public static final String IMG = makeCardPath("MorningMist.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 0;
    private static final int SCRY = 2;
    private static final int UPGRADE_PLUS_SCRY = 1;

    public MorningMist() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SCRY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(magicNumber));
        addToBot(new DiscardPileToTopOfDeckAction(p));
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
