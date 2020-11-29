package Gensokyo.cards.Item;

import Gensokyo.CardMods.ItemMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class RPG extends AbstractItemCard {
    public static final String ID = GensokyoMod.makeID(RPG.class.getSimpleName());
    public static final String IMG = makeCardPath("RPG.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    private static final int COST = 2;
    private static final int DAMAGE = 25;
    private static final int ITEM = 3;

    public RPG() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new ItemMod(ITEM));
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        rarity = ItemRarity.ITEM_COMMON;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }
}