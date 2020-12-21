package Gensokyo.cards.Item;

import Gensokyo.CardMods.ItemMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MedicineKit extends AbstractItemCard {
    public static final String ID = GensokyoMod.makeID(MedicineKit.class.getSimpleName());
    public static final String IMG = makeCardPath("MedicineKit.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 0;
    private static final int HEAL = 2;
    private static final int ITEM = 2;

    public MedicineKit() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new ItemMod(ITEM));
        magicNumber = baseMagicNumber = HEAL;
        selfRetain = true;
        rarity = ItemRarity.ITEM_UNCOMMON;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int debuffCount = 0;
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffCount++;
            }
        }
        addToBot(new RemoveDebuffsAction(p));
        addToBot(new HealAction(p, p, debuffCount * magicNumber));
    }
}