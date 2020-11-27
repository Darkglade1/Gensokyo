package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.ThirdEyeAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class UnhurriedMind extends AbstractImpossibleRequestRewardCard {

    public static final String ID = GensokyoMod.makeID(UnhurriedMind.class.getSimpleName());
    public static final String IMG = makeCardPath("UnhurriedMind.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 0;

    public UnhurriedMind() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ThirdEyeAction());
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {

    }
}
