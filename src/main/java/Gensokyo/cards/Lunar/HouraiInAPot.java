package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class HouraiInAPot extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(HouraiInAPot.class.getSimpleName());
    public static final String IMG = makeCardPath("HouraiInAPot.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 0;
    private static final int DRAW = 1;

    public HouraiInAPot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!AbstractDungeon.player.hand.isEmpty()) {
            addToBot(new GamblingChipAction(AbstractDungeon.player, true));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        if (upgraded) {
            addToBot(new DrawCardAction(defaultSecondMagicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
