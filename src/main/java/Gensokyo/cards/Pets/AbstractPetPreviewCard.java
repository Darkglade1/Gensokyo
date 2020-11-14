package Gensokyo.cards.Pets;

import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractPetPreviewCard extends AbstractDefaultCard {

        protected static final CardRarity RARITY = CardRarity.SPECIAL;
        protected static final CardTarget TARGET = CardTarget.NONE;
        protected static final CardType TYPE = CardType.SKILL;
        public static final CardColor COLOR = CardColor.COLORLESS;
        protected static final int COST = -2;

        public AbstractPetPreviewCard(String ID, String IMG, int COST, CardType TYPE, CardColor COLOR, CardRarity RARITY, CardTarget TARGET) {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
        }

        @Override
        public void upgrade() {
        }
    }