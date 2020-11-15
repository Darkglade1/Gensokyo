package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.JeweledCobra;
import Gensokyo.minions.PetUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonJeweledCobra extends AbstractSummonPetCard {
    public static final String ID = GensokyoMod.makeID(SummonJeweledCobra.class.getSimpleName());
    public static final String IMG = makeCardPath("JeweledCobra.png");

    public SummonJeweledCobra() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        misc = max_hp = MEDIUM_HP;
        this.cardsToPreview = new SummonJeweledCobraPreview();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!PetUtils.playerHasPet()) {
            super.use(p, m);
            JeweledCobra cobra =  new JeweledCobra(max_hp, magicNumber, PET_X_POSITION, PET_Y_POSITION);
            cobra.setAssociatedCard(this);
            BasePlayerMinionHelper.addMinion(p, cobra);
        }
    }
}