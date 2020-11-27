package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class HouraiInAPot extends AbstractImpossibleRequestRewardCard {

    public static final String ID = GensokyoMod.makeID(HouraiInAPot.class.getSimpleName());
    public static final String IMG = makeCardPath("HouraiInAPot.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int PLAYS = 2;

    public HouraiInAPot() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = PLAYS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
