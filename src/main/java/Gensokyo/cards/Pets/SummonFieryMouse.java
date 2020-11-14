package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.PetUtils;
import Gensokyo.minions.FieryMouse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonFieryMouse extends AbstractSummonPetCard {
    public static final String ID = GensokyoMod.makeID(SummonFieryMouse.class.getSimpleName());
    public static final String IMG = makeCardPath("FieryMouse.png");

    public SummonFieryMouse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        misc = max_hp = MEDIUM_HP;
        this.cardsToPreview = new SummonFieryMousePreview();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!PetUtils.playerHasPet()) {
            super.use(p, m);
            FieryMouse fieryMouse =  new FieryMouse(max_hp, magicNumber, PET_X_POSITION, PET_Y_POSITION);
            fieryMouse.setAssociatedCard(this);
            BasePlayerMinionHelper.addMinion(p, fieryMouse);
        }
    }
}