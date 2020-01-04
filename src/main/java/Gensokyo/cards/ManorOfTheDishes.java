package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.ManorOfTheDishesPower;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class ManorOfTheDishes extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(ManorOfTheDishes.class.getSimpleName());
    public static final String IMG = makeCardPath("ManorOfTheDishes.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int INTANGIBLE = 1;
    public static final int cardThreshold = 10;

    public ManorOfTheDishes() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = INTANGIBLE;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = cardThreshold;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ManorOfTheDishesPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
