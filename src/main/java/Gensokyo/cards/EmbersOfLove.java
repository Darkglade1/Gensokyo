package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class EmbersOfLove extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(EmbersOfLove.class.getSimpleName());
    public static final String IMG = makeCardPath("Embers.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int BUFF = 2;
    private static final int UPGRADE_BUFF = 1;

    public EmbersOfLove() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BUFF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        this.addToBot(new ApplyPowerAction(target, p, new StrengthPower(target, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BUFF);
        }
    }

}
