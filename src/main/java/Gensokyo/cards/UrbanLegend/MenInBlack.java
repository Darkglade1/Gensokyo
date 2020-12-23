package Gensokyo.cards.UrbanLegend;

import Gensokyo.GensokyoMod;
import Gensokyo.tags.Tags;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MenInBlack extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(MenInBlack.class.getSimpleName());
    public static final String IMG = makeCardPath("MenInBlack.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int BUFF = 1;

    public MenInBlack() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = BUFF;
        selfRetain = true;
        exhaust = true;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber), magicNumber));
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
