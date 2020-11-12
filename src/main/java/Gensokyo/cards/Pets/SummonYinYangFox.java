package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.YingYangFox;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonYinYangFox extends AbstractSummonPetCard {
    public static final String ID = GensokyoMod.makeID(SummonYinYangFox.class.getSimpleName());
    public static final String IMG = makeCardPath("YinYangFox.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final int COST = 0;

    public SummonYinYangFox() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = misc = LARGE_HP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(p);
        int minionCount = playerMinions.monsters.size();
        if (BasePlayerMinionHelper.getMaxMinions(p) <= minionCount) {
            BasePlayerMinionHelper.changeMaxMinionAmount(p, minionCount + 1);
        }
        BasePlayerMinionHelper.addMinion(p, new YingYangFox(LARGE_HP, magicNumber, PET_X_POSITION, PET_Y_POSITION));
    }

}