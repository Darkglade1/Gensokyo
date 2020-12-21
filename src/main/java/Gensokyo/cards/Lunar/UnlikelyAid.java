package Gensokyo.cards.Lunar;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.UnlikelyAidDiscoveryAction;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeCardPath;

public class UnlikelyAid extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(UnlikelyAid.class.getSimpleName());
    public static final String IMG = makeCardPath("UnlikelyAid.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = GensokyoMod.Enums.LUNAR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int DISCOVER_AMT = 3;
    private static final int UPGRADE_PLUS_DISCOVER = 2;

    public UnlikelyAid() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DISCOVER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, info, effect));
        CardGroup masterdeck = new CardGroup(AbstractDungeon.player.masterDeck, CardGroup.CardGroupType.UNSPECIFIED);
        int uniqueCount = 0;
        ArrayList<AbstractCard> uniqueCards = new ArrayList<>();
        for (AbstractCard card: masterdeck.group) {
            if (!containsCard(uniqueCards, card)) {
                uniqueCount++;
                uniqueCards.add(card);
            }
        }
        if (uniqueCount <= magicNumber) {
            if (uniqueCount > 0) {
                addToBot(new UnlikelyAidDiscoveryAction(uniqueCards));
            }
        } else {
            ArrayList<AbstractCard> selectedCards = new ArrayList<>();
            while (selectedCards.size() < magicNumber) {
                AbstractCard randomCard = masterdeck.group.get(AbstractDungeon.cardRandomRng.random(masterdeck.size() - 1));
                if (!containsCard(selectedCards, randomCard)) {
                    selectedCards.add(randomCard);
                }
            }
            addToBot(new UnlikelyAidDiscoveryAction(selectedCards));
        }
    }

    public static boolean containsCard(ArrayList<AbstractCard> cards, AbstractCard card) {
        for (AbstractCard c : cards) {
            if (c.cardID.equals(card.cardID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DISCOVER);
            initializeDescription();
        }
    }
}
