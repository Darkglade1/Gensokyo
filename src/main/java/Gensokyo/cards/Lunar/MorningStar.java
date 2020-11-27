package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class MorningStar extends AbstractImpossibleRequestRewardCard {

    public static final String ID = GensokyoMod.makeID(MorningStar.class.getSimpleName());
    public static final String IMG = makeCardPath("MorningStar.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public MorningStar() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        DamageInfo info = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        addToBot(new DamageAction(m, info, effect));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card.type == CardType.STATUS) {
                        this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                    }
                }
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card.type == CardType.STATUS) {
                        this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile));
                    }
                }
                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card.type == CardType.STATUS) {
                        this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
                    }
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
