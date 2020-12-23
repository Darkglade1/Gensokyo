package Gensokyo.cards.UrbanLegend;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.LunacyPower;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class Kunekune extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(Kunekune.class.getSimpleName());
    public static final String IMG = makeCardPath("Kunekune.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 3;
    private static final int DEBUFF = 2;

    public Kunekune() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF;
        exhaust = true;
        isEthereal = true;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new LunacyPower(m, p, this.magicNumber), this.magicNumber, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            initializeDescription();
        }
    }
}
