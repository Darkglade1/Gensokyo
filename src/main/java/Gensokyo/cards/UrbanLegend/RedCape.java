package Gensokyo.cards.UrbanLegend;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RedCape extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(RedCape.class.getSimpleName());
    public static final String IMG = makeCardPath("RedCape.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;

    public RedCape() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        baseDamage = DAMAGE;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
