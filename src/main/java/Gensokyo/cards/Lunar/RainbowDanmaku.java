package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.CallbackScryAction;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.function.Consumer;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RainbowDanmaku extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(RainbowDanmaku.class.getSimpleName());
    public static final String IMG = makeCardPath("RainbowDanmaku.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 3;
    private static final int SCRY = 5;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DAMAGE = 5;

    public RainbowDanmaku() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SCRY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        Consumer<ArrayList<AbstractCard>> consumer = cards -> {
            for (AbstractCard card : cards) {
                if (card.type == CardType.ATTACK) {
                    addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
