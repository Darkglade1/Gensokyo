package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.TekeTekeAction;
import Gensokyo.tags.Tags;
import Gensokyo.vfx.EmptyEffect;
import Gensokyo.vfx.TrainEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class TekeTeke extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(TekeTeke.class.getSimpleName());
    public static final String IMG = makeCardPath("TekeTeke.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 3;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    private static final int BOOST = 7;
    private static final int UPGRADE_PLUS_BOOST = 3;

    private static final int HITS = 3;

    public boolean triggered = false;

    public TekeTeke() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BOOST;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = HITS;
        tags.add(Tags.URBAN_LEGEND);
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.triggered = false;
        AbstractDungeon.actionManager.addToBottom(new SFXAction("Gensokyo:Train"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new TrainEffect(), 0.5F));
        AbstractDungeon.actionManager.addToBottom(new TekeTekeAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY, this));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.6F));
        AbstractDungeon.actionManager.addToBottom(new TekeTekeAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY, this));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.6F));
        AbstractDungeon.actionManager.addToBottom(new TekeTekeAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY, this));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_BOOST);
            initializeDescription();
        }
    }
}
