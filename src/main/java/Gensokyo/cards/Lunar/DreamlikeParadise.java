package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.CallbackScryAction;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.function.Consumer;

import static Gensokyo.GensokyoMod.makeCardPath;

public class DreamlikeParadise extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(DreamlikeParadise.class.getSimpleName());
    public static final String IMG = makeCardPath("CrescentMoonSlash.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 1;
    private static final int SCRY = 3;
    private static final int UPGRADE_PLUS_SCRY = 1;

    public DreamlikeParadise() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SCRY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Consumer<ArrayList<AbstractCard>> consumer = cards -> {
            for (AbstractCard card : cards) {
                if (card.cost > 0) {
                    card.freeToPlayOnce = true;
                }
            }
        };
        addToBot(new CallbackScryAction(magicNumber, consumer));
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
            upgradeMagicNumber(UPGRADE_PLUS_SCRY);
            initializeDescription();
        }
    }
}
