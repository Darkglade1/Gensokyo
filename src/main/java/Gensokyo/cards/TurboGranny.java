package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class TurboGranny extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(TurboGranny.class.getSimpleName());
    public static final String IMG = makeCardPath("TurboGranny.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int DEX = 2;
    private static final int UPGRADE_DEX = 1;
    private static final int PLATED_ARMOR = 1;
    private static final int UPGRADE_PLATED_ARMOR = 1;

    public TurboGranny() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEX;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = PLATED_ARMOR;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DEX);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLATED_ARMOR);
            initializeDescription();
        }
    }
}
