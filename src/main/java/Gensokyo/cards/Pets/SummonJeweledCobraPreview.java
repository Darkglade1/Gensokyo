package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.JeweledCobra;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonJeweledCobraPreview extends AbstractPetPreviewCard {
        public static final String ID = GensokyoMod.makeID("PetPreview");
        public static final String IMG = makeCardPath("JeweledCobra.png");

        public SummonJeweledCobraPreview() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            this.rawDescription = JeweledCobra.move1 + " NL " + JeweledCobra.move2 + " NL " + JeweledCobra.move3;
            initializeDescription();
        }
    }