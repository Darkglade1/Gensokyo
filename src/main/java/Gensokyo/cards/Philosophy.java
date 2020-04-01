package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act2.PhilosophyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class Philosophy extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(Philosophy.class.getSimpleName());
    public static final String IMG = makeCardPath("Philosophy.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5;

    public Philosophy() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PhilosophyPower(p, magicNumber), magicNumber));
    }

    @Override
    public float getTitleFontSize()
    {
        return 12;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE);
        }
    }

}
