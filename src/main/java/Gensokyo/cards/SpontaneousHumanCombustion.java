package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.SpontaneousHumanCombustionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SpontaneousHumanCombustion extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(SpontaneousHumanCombustion.class.getSimpleName());
    public static final String IMG = makeCardPath("SpontaneousHumanCombustion.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int STRENGTH = 5;
    private static final int UPGRADE_PLUS_STRENGTH = 2;
    public static final int SELF_DAMAGE = 1;

    public SpontaneousHumanCombustion() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STRENGTH;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = SELF_DAMAGE;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpontaneousHumanCombustionPower(p, defaultSecondMagicNumber)));
    }

    @Override
    public float getTitleFontSize()
    {
        return 11;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH);
            initializeDescription();
        }
    }
}
