package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.TemporaryMaxHPLossAction;
import Gensokyo.powers.act3.BorderOfDeath;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class Butterfly extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(Butterfly.class.getSimpleName());
    public static final String IMG = makeCardPath("Butterfly.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;
    private static final int MAX_HP_LOSS = 2;

    public Butterfly() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAX_HP_LOSS;
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerOnExhaust() {
        AbstractDungeon.actionManager.addToBottom(new TemporaryMaxHPLossAction(magicNumber));
    }

    @Override
    public void upgrade() {
    }

}
