package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.bossRush.Yuyuko;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class Butterfly extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(Butterfly.class.getSimpleName());
    public static final String IMG = makeCardPath("Butterfly.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int BLOCK = 3;
    private static final int HP_LOSS = 3;

    Yuyuko yuyuko;

    public Butterfly(Yuyuko yuyuko) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HP_LOSS;
        baseBlock = BLOCK;
        this.yuyuko = yuyuko;
        exhaust = true;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void triggerOnExhaust() {
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
        if (yuyuko != null) {
            yuyuko.incrementFan(1); //because for some reason onExhaust in powers only works if the power is on the player
        }
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new Butterfly(this.yuyuko);
    }
}
