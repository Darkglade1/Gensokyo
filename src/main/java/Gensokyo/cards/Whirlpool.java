//package Gensokyo.cards;
//
//import Gensokyo.GensokyoMod;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.DrawReductionPower;
//
//import static Gensokyo.GensokyoMod.makeCardPath;
//
//public class Whirlpool extends AbstractDefaultCard {
//
//    public static final String ID = GensokyoMod.makeID(Whirlpool.class.getSimpleName());
//    public static final String IMG = makeCardPath("Whirlpool.png");
//
//    private static final CardRarity RARITY = CardRarity.COMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.STATUS;
//    public static final CardColor COLOR = CardColor.COLORLESS;
//
//    private static final int COST = 1;
//
//    public Whirlpool() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
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
//    public boolean canPlay(AbstractCard card) {
//        return card.type != CardType.SKILL;
//    }
//}