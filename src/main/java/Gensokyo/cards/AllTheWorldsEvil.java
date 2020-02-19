package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class AllTheWorldsEvil extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(AllTheWorldsEvil.class.getSimpleName());
    public static final String IMG = makeCardPath("Evil.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 0;
    private static final int DEBUFF = 1;

    public AllTheWorldsEvil() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = DEBUFF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, DEBUFF, false), DEBUFF));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, DEBUFF, false), DEBUFF));
        }
    }

    @Override
    public float getTitleFontSize()
    {
        return 18;
    }

    @Override
    public void upgrade() {

    }
}
