package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.CallbackExhaustAction;
import ThMod.ThMod;
import ThMod.patches.AbstractCardEnum;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

import static Gensokyo.GensokyoMod.makeCardPath;
import static ThMod.patches.CardTagEnum.SPARK;

public class MarisaTwilightSpark extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(MarisaTwilightSpark.class.getSimpleName());
    public static final String IMG = makeCardPath("TwilightSpark.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.MARISA_DERIVATIONS;

    private static final int COST = 0;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5;
    private static final int BONUS_DAMAGE = 5;
    private static final int BONUS_DAMAGE_UPGRADE = 2;
    private static final int AMP = 3;

    public MarisaTwilightSpark() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
        this.tags.add(SPARK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (ThMod.Amplified(this, AMP)) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new MindblastEffect(p.dialogX, p.dialogY, false)));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        } else {
            AbstractDungeon.actionManager.addToBottom(new CallbackExhaustAction(BaseMod.MAX_HAND_SIZE, false, true, true, cards -> cards.forEach(card->this.baseDamage += magicNumber)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(BONUS_DAMAGE_UPGRADE);
            initializeDescription();
        }
    }
}
