package Gensokyo.cards.Evolve;

import Gensokyo.CardMods.EvolveMod;
import Gensokyo.GensokyoMod;
import Gensokyo.minions.PetUtils;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MysteriousEgg extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(MysteriousEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("MysteriousEgg.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final int COST = 1;
    private static final int EVOLVE = 4;

    public MysteriousEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        CardModifierManager.addModifier(this, new EvolveMod(EVOLVE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerEvolve() {
        AbstractDungeon.actionManager.addToBottom(new AddCardToDeckAction(PetUtils.getRandomPetCard()));
    }
}