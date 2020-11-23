//package Gensokyo.cards;
//
//import Gensokyo.GensokyoMod;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
//import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.DrawReductionPower;
//import com.megacrit.cardcrawl.powers.WeakPower;
//
//import static Gensokyo.GensokyoMod.makeCardPath;
//
//public class EMP extends AbstractDefaultCard {
//
//    public static final String ID = GensokyoMod.makeID(EMP.class.getSimpleName());
//    public static final String IMG = makeCardPath("EMP.png");
//
//    private static final CardRarity RARITY = CardRarity.COMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.STATUS;
//    public static final CardColor COLOR = CardColor.COLORLESS;
//
//    private static final int COST = -2;
//    private static final int ENERGY_LOSS = 1;
//    private static final int DRAW_REDUCTION = 1;
//
//    public EMP() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        magicNumber = baseMagicNumber = ENERGY_LOSS;
//        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = DRAW_REDUCTION;
//        this.isEthereal = true;
//
//    }
//
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//    }
//
//    @Override
//    public void upgrade() {
//    }
//
//    @Override
//    public void triggerWhenDrawn() {
//        addToBot(new LoseEnergyAction(magicNumber));
//        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawReductionPower(AbstractDungeon.player, defaultSecondMagicNumber), defaultSecondMagicNumber));
//    }
//}