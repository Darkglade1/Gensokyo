package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.PetUtils;
import Gensokyo.minions.PsychicCat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonPsychicCat extends AbstractSummonPetCard {
    public static final String ID = GensokyoMod.makeID(SummonPsychicCat.class.getSimpleName());
    public static final String IMG = makeCardPath("PsychicCat.png");

    public SummonPsychicCat() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        misc = max_hp = SMALL_HP;
        this.cardsToPreview = new SummonPsychicCatPreview();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!PetUtils.playerHasPet()) {
            super.use(p, m);
            PsychicCat cat =  new PsychicCat(max_hp, magicNumber, PET_X_POSITION, PET_Y_POSITION);
            cat.setAssociatedCard(this);
            BasePlayerMinionHelper.addMinion(p, cat);
            applySpellCardRules(cat);
        }
    }
}