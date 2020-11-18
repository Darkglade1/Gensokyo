package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.PsychicCat;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonPsychicCatPreview extends AbstractPetPreviewCard {
        public static final String ID = GensokyoMod.makeID("PetPreview");
        public static final String IMG = makeCardPath("PsychicCat.png");

        public SummonPsychicCatPreview() {
            super(ID, IMG, COST, TYPE, RARITY, TARGET);
            this.rawDescription = PsychicCat.move1 + " NL " + PsychicCat.move2 + " NL " + PsychicCat.move3;
            initializeDescription();
        }
    }