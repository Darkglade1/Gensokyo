package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.SevenSchoolMysteriesPower;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SevenSchoolMysteries extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(SevenSchoolMysteries.class.getSimpleName());
    public static final String IMG = makeCardPath("SevenSchoolMysteries.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 0;
    private static final int BUFF = 3;
    private static final int UPGRADED_BUFF = 1;
    public static final int cardThreshold = 7;

    public SevenSchoolMysteries() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = BUFF;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = cardThreshold;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SevenSchoolMysteriesPower(p, magicNumber)));
    }

    @Override
    public float getTitleFontSize()
    {
        return 18;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_BUFF);
            initializeDescription();
        }
    }
}
