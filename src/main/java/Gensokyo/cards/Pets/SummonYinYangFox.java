package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.YingYangFox;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        misc = max_hp = LARGE_HP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!playerHasPet()) {
            super.use(p, m);
            YingYangFox fox =  new YingYangFox(max_hp, magicNumber, PET_X_POSITION, PET_Y_POSITION);
            fox.setAssociatedCard(this);
            BasePlayerMinionHelper.addMinion(p, fox);
        }
    }
}