package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.HAARPPower;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class HAARP extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(HAARP.class.getSimpleName());
    public static final String IMG = makeCardPath("HAARP.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 2;
    private static final int CHANNEL = 1;
    private static final int UPGRADE_CHANNEL = 1;

    public HAARP() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = CHANNEL;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HAARPPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CHANNEL);
            initializeDescription();
        }
    }
}
