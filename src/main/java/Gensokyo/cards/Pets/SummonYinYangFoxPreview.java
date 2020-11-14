package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import Gensokyo.minions.YingYangFox;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonYinYangFoxPreview extends AbstractDefaultCard {
        public static final String ID = GensokyoMod.makeID(SummonYinYangFoxPreview.class.getSimpleName());
        public static final String IMG = makeCardPath("YinYangFox.png");

        private static final CardRarity RARITY = CardRarity.SPECIAL;
        private static final CardTarget TARGET = CardTarget.NONE;
        private static final CardType TYPE = CardType.SKILL;
        public static final CardColor COLOR = CardColor.COLORLESS;
        private static final int COST = -2;

        public SummonYinYangFoxPreview() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            this.rawDescription = YingYangFox.damage_string + " NL " + YingYangFox.block_string + " NL " + YingYangFox.heal_string;
            initializeDescription();
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {

        }
        
        public void upgrade() {
            
        }
    }