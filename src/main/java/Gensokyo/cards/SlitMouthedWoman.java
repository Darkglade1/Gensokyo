package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.SlitMouthedWomanPower;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class SlitMouthedWoman extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(SlitMouthedWoman.class.getSimpleName());
    public static final String IMG = makeCardPath("SlitMouthedWoman.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int DEBUFF = 3;
    private static final int UPGRADED_DEBUFF = 1;

    public SlitMouthedWoman() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF;
        this.exhaust = true;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SlitMouthedWomanPower(p, magicNumber)));
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
            upgradeMagicNumber(UPGRADED_DEBUFF);
            initializeDescription();
        }
    }
}
