package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.PetUtils;
import Gensokyo.minions.YingYangFox;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonYinYangFox extends AbstractSummonPetCard {
    public static final String ID = GensokyoMod.makeID(SummonYinYangFox.class.getSimpleName());
    public static final String IMG = makeCardPath("YinYangFox.png");

    public SummonYinYangFox() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        misc = max_hp = LARGE_HP;
        this.cardsToPreview = new SummonYinYangFoxPreview();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!PetUtils.playerHasPet()) {
            super.use(p, m);
            YingYangFox fox =  new YingYangFox(max_hp, magicNumber, PET_X_POSITION, PET_Y_POSITION);
            fox.setAssociatedCard(this);
            BasePlayerMinionHelper.addMinion(p, fox);
            applySpellCardRules(fox);
        }
    }
}