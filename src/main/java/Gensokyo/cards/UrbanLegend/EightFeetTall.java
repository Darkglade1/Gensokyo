package Gensokyo.cards.UrbanLegend;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.DeathMark;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class EightFeetTall extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(EightFeetTall.class.getSimpleName());
    public static final String IMG = makeCardPath("EightFeetTall.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int DEBUFF = 5;

    public EightFeetTall() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF;
        exhaust = true;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!(m.type == AbstractMonster.EnemyType.BOSS)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DeathMark(m, this.magicNumber), this.magicNumber, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
