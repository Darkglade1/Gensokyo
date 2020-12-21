package Gensokyo.cards.Evolve;

import Gensokyo.cards.AbstractShopSpecialCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractEvolveCard extends AbstractShopSpecialCard {

    public AbstractEvolveCard(String ID, String IMG, int COST, CardType TYPE, CardRarity RARITY, CardTarget TARGET) {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }

    public abstract void triggerEvolve();

}