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

public class ExoticEgg extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(ExoticEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("ExoticEgg.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 2;
    private static final int EVOLVE = 2;

    public ExoticEgg() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
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