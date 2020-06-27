package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makePowerPath;

public class TimeDilation extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("TimeDilation");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int MAX = 3;
    public static final int MIN = 1;
    public static final int INCREASE = 1;

    public boolean isActive = true;

    private ArrayList<CardInfo> delayedBy1 = new ArrayList<>();
    private ArrayList<CardInfo> delayedBy2 = new ArrayList<>();
    private ArrayList<CardInfo> delayedBy3 = new ArrayList<>();

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Philosophy84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Philosophy32.png"));

    public TimeDilation(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = MIN;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("time");

        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }



    public void increment(AbstractCard c, AbstractMonster monster) {
        this.flash();
        CardInfo cardInfo = new CardInfo(c.makeSameInstanceOf(), monster, c.energyOnUse);
        if (amount == 1) {
            delayedBy1.add(cardInfo);
        } else if (amount == 2) {
            delayedBy2.add(cardInfo);
        } else if (amount == 3) {
            delayedBy3.add(cardInfo);
        }
        amount += INCREASE;
        if (amount > MAX) {
            amount = MIN;
        }
        System.out.println(delayedBy1);
        System.out.println(delayedBy2);
        System.out.println(delayedBy3);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + MAX + DESCRIPTIONS[4] + MIN + DESCRIPTIONS[5] + INCREASE + DESCRIPTIONS[6];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + MAX + DESCRIPTIONS[4] + MIN + DESCRIPTIONS[5] + INCREASE + DESCRIPTIONS[6];
        }
    }

    private static class CardInfo {
        public AbstractCard card;
        public AbstractMonster target;
        public int energyOnUse;

        public CardInfo(AbstractCard card, AbstractMonster target, int energyOnUse) {
            this.card = card;
            this.target = target;
            this.energyOnUse = energyOnUse;
        }

        @Override
        public String toString() {
            return card.toString();
        }
    }
}
