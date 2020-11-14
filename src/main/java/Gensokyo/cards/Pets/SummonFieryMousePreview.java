package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.FieryMouse;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonFieryMousePreview extends AbstractPetPreviewCard {
        public static final String ID = GensokyoMod.makeID("PetPreview");
        public static final String IMG = makeCardPath("FieryMouse.png");

        public SummonFieryMousePreview() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            this.rawDescription = FieryMouse.move1 + " NL " + FieryMouse.move2 + " NL " + FieryMouse.move3;
            initializeDescription();
        }
    }