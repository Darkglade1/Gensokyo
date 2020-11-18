package Gensokyo.cards.Evolve;

import Gensokyo.CardMods.EvolveMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RustyChest extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(RustyChest.class.getSimpleName());
    public static final String IMG = makeCardPath("MysteriousEgg.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 3;
    private static final int EVOLVE = 2;

    public static final int GOLD = 250;

    public RustyChest() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new EvolveMod(EVOLVE));
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = GOLD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerEvolve() {
        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
        AbstractDungeon.player.gainGold(GOLD);
    }
}