package Gensokyo.cards.Item;

import Gensokyo.CardMods.BloodcastMod;
import Gensokyo.CardMods.ItemMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class LifeforceConverter extends AbstractItemCard {
    public static final String ID = GensokyoMod.makeID(LifeforceConverter.class.getSimpleName());
    public static final String IMG = makeCardPath("LifeforceConverter.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 0;
    private static final int ITEM = 2;

    public LifeforceConverter() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new ItemMod(ITEM));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    CardModifierManager.addModifier(card, new BloodcastMod());
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public float getTitleFontSize()
    {
        return 16;
    }
}