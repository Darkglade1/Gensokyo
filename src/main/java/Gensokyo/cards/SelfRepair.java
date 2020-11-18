package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.rooms.nitori.Nitori;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SelfRepair extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(SelfRepair.class.getSimpleName());
    public static final String IMG = makeCardPath("SelfRepair.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int BLOCK = 20;
    private static final int TEMPHP = 5;
    private static final int TEMPHP_INCREASE = 5;

    public SelfRepair() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.block = baseBlock = BLOCK;
        this.magicNumber = baseMagicNumber = TEMPHP;
        this.defaultSecondMagicNumber = defaultBaseSecondMagicNumber = TEMPHP_INCREASE;
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for(AbstractMonster mo: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Nitori) {
                addToBot(new GainBlockAction(mo, block));
                break;
            }
        }
    }

    @Override
    public void upgrade() {
    }

    public void onRetained() {
        for(AbstractMonster mo: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Nitori) {
                addToBot(new AddTemporaryHPAction(mo, AbstractDungeon.player, magicNumber));
                break;
            }
        }
        upgradeMagicNumber(defaultSecondMagicNumber);
    }
}