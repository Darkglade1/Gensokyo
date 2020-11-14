package Gensokyo.cards.Evolve;

import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractEvolveCard extends AbstractDefaultCard {

    public AbstractEvolveCard(String ID, String IMG, int COST, CardType TYPE, CardColor COLOR, CardRarity RARITY, CardTarget TARGET) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
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