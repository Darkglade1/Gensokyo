package Gensokyo.cards.Evolve;

import Gensokyo.CardMods.EvolveMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;

import static Gensokyo.GensokyoMod.makeCardPath;

public class TrainingManual extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(TrainingManual.class.getSimpleName());
    public static final String IMG = makeCardPath("TrainingManual.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 2;
    private static final int EVOLVE = 1;

    public TrainingManual() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new EvolveMod(EVOLVE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerEvolve() {
        RewardItem item = new RewardItem();
        for (AbstractCard card : item.cards) {
            card.upgrade();
        }
        AbstractDungeon.getCurrRoom().addCardReward(item);
    }
}