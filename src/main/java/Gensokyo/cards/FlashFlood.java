//package Gensokyo.cards;
//
//import Gensokyo.GensokyoMod;
//import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import static Gensokyo.GensokyoMod.makeCardPath;
//
//public class FlashFlood extends AbstractDefaultCard {
//
//    public static final String ID = GensokyoMod.makeID(FlashFlood.class.getSimpleName());
//    public static final String IMG = makeCardPath("FlashFlood.png");
//
//    private static final CardRarity RARITY = CardRarity.COMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.STATUS;
//    public static final CardColor COLOR = CardColor.COLORLESS;
//
//    private static final int COST_REDUCTION = 1;
//    private static final int COST = 5;
//
//    public FlashFlood() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        magicNumber = baseMagicNumber = COST_REDUCTION;
//        exhaust = true;
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
//    public void triggerWhenDrawn() { AbstractDungeon.actionManager.addToBottom(new ReduceCostAction(this.uuid, this.magicNumber)); }
//}