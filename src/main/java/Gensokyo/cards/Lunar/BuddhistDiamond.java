package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class BuddhistDiamond extends AbstractDefaultCard implements OnDiscardedByScry{

    public static final String ID = GensokyoMod.makeID(BuddhistDiamond.class.getSimpleName());
    public static final String IMG = makeCardPath("BuddhistDiamond.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 2;
    private static final int DAMAGE = 24;
    private static final int UPGRADE_PLUS_DMG = 6;
    private static final int DAMAGE_INCREASE = 12;
    private static final int UPGRADE_DAMAGE_INCREASE = 3;

    public BuddhistDiamond() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_INCREASE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        addToBot(new DamageAction(m, info, effect));
    }

    @Override
    public void onDiscardedByScry() {
        addToTop(new ModifyDamageAction(this.uuid, this.magicNumber));
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
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_DAMAGE_INCREASE);
            initializeDescription();
        }
    }
}
