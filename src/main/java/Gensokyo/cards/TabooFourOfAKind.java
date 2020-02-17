package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.TabooUpgradeAction;
import Gensokyo.powers.act2.TabooFourOfAKindPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class TabooFourOfAKind extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(TabooFourOfAKind.class.getSimpleName());
    public static final String IMG = makeCardPath("FourOfAKind.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int REPEATS = 1;
    private static final int UPGRADE = 1;

    public static boolean playedThisTurn = false;

    public TabooFourOfAKind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = REPEATS;
        this.defaultSecondMagicNumber = defaultBaseSecondMagicNumber = UPGRADE;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TabooFourOfAKindPower(p, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new TabooUpgradeAction(this, this.defaultSecondMagicNumber));
        TabooFourOfAKind.playedThisTurn = true;
    }

    @Override
    public boolean canUse(final AbstractPlayer p, final AbstractMonster m) {
        final boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (TabooFourOfAKind.playedThisTurn) {
            this.cantUseMessage = languagePack.getCardStrings(cardID).EXTENDED_DESCRIPTION[0];
            return false;
        }
        return canUse;
    }

    @Override
    public void atTurnStart() {
        TabooFourOfAKind.playedThisTurn = false;
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
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public void initializeDescription() {
        if (this.magicNumber != 1) {
            rawDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
        }
        super.initializeDescription();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.magicNumber = card.baseMagicNumber; //because this doesn't get set for some reason
        return card;
    }
}
