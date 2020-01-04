package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.tags.Tags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MonkeysPaw extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(MonkeysPaw.class.getSimpleName());
    public static final String IMG = makeCardPath("MonkeysPaw.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 0;
    private static final int GOLD = 12;
    private static final int UPGRADED_GOLD = 3;

    public MonkeysPaw() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        magicNumber = baseMagicNumber = GOLD;
        exhaust = true;
        tags.add(Tags.URBAN_LEGEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       int half = p.currentHealth / 2;
       if (half > 0) {
           int goldGained = magicNumber * half;
           AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(p, half, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON));
           AbstractDungeon.player.gainGold(goldGained);
       }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_GOLD);
            initializeDescription();
        }
    }
}
