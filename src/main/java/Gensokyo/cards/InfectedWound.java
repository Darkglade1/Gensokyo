package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class InfectedWound extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(InfectedWound.class.getSimpleName());
    public static final String IMG = makeCardPath("InfectedWound.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 2;

    public InfectedWound() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        isEthereal = true;
        exhaust = true;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (card.type == CardType.ATTACK) {
            card.cantUseMessage = languagePack.getCardStrings(cardID).EXTENDED_DESCRIPTION[0];
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }
}
