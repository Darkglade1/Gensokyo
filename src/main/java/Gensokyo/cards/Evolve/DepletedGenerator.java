package Gensokyo.cards.Evolve;

import Gensokyo.CardMods.EvolveMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeCardPath;

public class DepletedGenerator extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(DepletedGenerator.class.getSimpleName());
    public static final String IMG = makeCardPath("DepletedGenerator.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 2;
    private static final int EVOLVE = 2;

    public DepletedGenerator() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new EvolveMod(EVOLVE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public float getTitleFontSize()
    {
        return 16;
    }

    @Override
    public void triggerEvolve() {
        RewardItem item = new RewardItem();
        ArrayList<AbstractCard> group = new ArrayList<>();
        for(int i = 0; i < item.cards.size(); ++i) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
            boolean containsDupe = true;

            while(true) {
                while(containsDupe) {
                    containsDupe = false;
                    Iterator var6 = group.iterator();

                    while(var6.hasNext()) {
                        AbstractCard c = (AbstractCard)var6.next();
                        if (c.cardID.equals(card.cardID)) {
                            containsDupe = true;
                            card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
                            break;
                        }
                    }
                }

                if (group.contains(card)) {
                    --i;
                } else {
                    group.add(card);
                }
                break;
            }
        }
        item.cards = group;
        AbstractDungeon.getCurrRoom().addCardReward(item);
    }
}