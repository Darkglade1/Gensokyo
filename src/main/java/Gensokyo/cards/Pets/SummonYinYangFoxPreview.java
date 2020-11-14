package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.YingYangFox;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonYinYangFoxPreview extends AbstractPetPreviewCard {
        public static final String ID = GensokyoMod.makeID("PetPreview");
        public static final String IMG = makeCardPath("YinYangFox.png");

        public SummonYinYangFoxPreview() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            this.rawDescription = YingYangFox.move1 + " NL " + YingYangFox.move2 + " NL " + YingYangFox.move3;
            initializeDescription();
        }
    }