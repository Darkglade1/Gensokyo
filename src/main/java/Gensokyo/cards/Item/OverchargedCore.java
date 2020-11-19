package Gensokyo.cards.Item;

import Gensokyo.CardMods.ItemMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeCardPath;

public class OverchargedCore extends AbstractItemCard {
    public static final String ID = GensokyoMod.makeID(OverchargedCore.class.getSimpleName());
    public static final String IMG = makeCardPath("OverchargedCore.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    private static final int COST = 1;
    private static final int BUFF = 3;
    private static final int ITEM = 1;

    public OverchargedCore() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new ItemMod(ITEM));
        magicNumber = baseMagicNumber = BUFF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, BUFF), BUFF));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, BUFF), BUFF));
        addToBot(new ApplyPowerAction(p, p, new FocusPower(p, BUFF), BUFF));
        addToBot(new LoseHPAction(p, p, BUFF));
    }
}