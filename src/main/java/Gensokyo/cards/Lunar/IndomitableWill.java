//package Gensokyo.cards.Lunar;
//
//import Gensokyo.GensokyoMod;
//import Gensokyo.actions.CallbackScryAction;
//import Gensokyo.cards.AbstractDefaultCard;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.DexterityPower;
//import com.megacrit.cardcrawl.powers.StrengthPower;
//
//import java.util.ArrayList;
//import java.util.function.Consumer;
//
//import static Gensokyo.GensokyoMod.makeCardPath;
//
//public class IndomitableWill extends AbstractDefaultCard {
//
//    public static final String ID = GensokyoMod.makeID(IndomitableWill.class.getSimpleName());
//    public static final String IMG = makeCardPath("CrescentMoonSlash.png");
//
//    private static final CardRarity RARITY = CardRarity.UNCOMMON;
//    private static final CardTarget TARGET = CardTarget.SELF;
//    private static final CardType TYPE = CardType.POWER;
//    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;
//
//    private static final int COST = 1;
//    private static final int SCRY = 2;
//    private static final int UPGRADE_PLUS_SCRY = 1;
//    private static final int STR_DEX = 2;
//
//    public IndomitableWill() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        magicNumber = baseMagicNumber = SCRY;
//        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = STR_DEX;
//    }
//
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        Consumer<ArrayList<AbstractCard>> consumer = cards -> {
//            for (AbstractCard card : cards) {
//                if (card.type == CardType.ATTACK) {
//                    addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, defaultSecondMagicNumber), defaultSecondMagicNumber));
//                }
//                if (card.type == CardType.SKILL) {
//                    addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, defaultSecondMagicNumber), defaultSecondMagicNumber));
//                }
//            }
//        };
//        addToBot(new CallbackScryAction(magicNumber, consumer));
//    }
//
//    @Override
//    public float getTitleFontSize()
//    {
//        return 18;
//    }
//
//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            upgradeMagicNumber(UPGRADE_PLUS_SCRY);
//            initializeDescription();
//        }
//    }
//}
