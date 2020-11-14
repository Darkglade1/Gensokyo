package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.minions.YingYangFox;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SummonYinYangFoxPreview extends AbstractPetPreviewCard {
        public static final String ID = GensokyoMod.makeID("PetPreview");
        public static final String IMG = makeCardPath("YinYangFox.png");

        public SummonYinYangFoxPreview() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            this.rawDescription = YingYangFox.damage_string + " NL " + YingYangFox.block_string + " NL " + YingYangFox.heal_string;
            initializeDescription();
        }
    }