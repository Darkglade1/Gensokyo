package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class InfectedWound extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(InfectedWound.class.getSimpleName());
    public static final String IMG = makeCardPath("InfectedWound.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 2;
    private static final int DAMAGE = 3;

    public InfectedWound() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE;
        isEthereal = true;
        exhaust = true;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c.type == CardType.ATTACK) {
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON, true));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }
}
