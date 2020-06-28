package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.TimeDilationPlayCardAction;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

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

    private ArrayList<CardInfo> delayedBy1 = new ArrayList<>();
    private ArrayList<CardInfo> delayedBy2 = new ArrayList<>();
    private ArrayList<CardInfo> delayedBy3 = new ArrayList<>();

    public ArrayList<CardInfo> playingCards = new ArrayList<>(); //The cards to not delay

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
        int energy = c.energyOnUse;
        if (c.cost == -1) {
            energy = EnergyPanel.totalCount;
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
        CardInfo cardInfo = new CardInfo(c.makeSameInstanceOf(), monster, energy);
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

        playingCards.clear();
        updateDescription();
    }

    public void playCards() {
        for (CardInfo cardInfo : delayedBy1) {
            AbstractCard card = cardInfo.card;
            card.purgeOnUse = true;
            playingCards.add(cardInfo);
            this.addToBot(new TimeDilationPlayCardAction(card, cardInfo.target, false));
        }
        delayedBy1.clear();
        delayedBy1.addAll(delayedBy2);
        delayedBy2.clear();
        delayedBy2.addAll(delayedBy3);
        delayedBy3.clear();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + MAX + DESCRIPTIONS[4] + MIN + DESCRIPTIONS[5] + INCREASE + DESCRIPTIONS[6];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + MAX + DESCRIPTIONS[4] + MIN + DESCRIPTIONS[5] + INCREASE + DESCRIPTIONS[6];
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        float drawScale = 0.30F;
        float initialX = 200.0F * Settings.scale;
        float offsetX = 100.0F * Settings.scale;
        float initialY = 460.0F * Settings.scale;
        float offsetY1 = 130.0F * Settings.scale;
        float offsetY2 = 260.0F * Settings.scale;
        float offsetY3 = 390.0F * Settings.scale;
        for (int i = 0; i < delayedBy1.size(); i++) {
            AbstractCard card = delayedBy1.get(i).card;
            card.drawScale = drawScale;
            card.current_x = initialX + offsetX * i;
            card.current_y = initialY + offsetY1;
            card.render(sb);
        }
        for (int i = 0; i < delayedBy2.size(); i++) {
            AbstractCard card = delayedBy2.get(i).card;
            card.drawScale = drawScale;
            card.current_x = initialX + offsetX * i;
            card.current_y = initialY + offsetY2;
            card.render(sb);
        }
        for (int i = 0; i < delayedBy3.size(); i++) {
            AbstractCard card = delayedBy3.get(i).card;
            card.drawScale = drawScale;
            card.current_x = initialX + offsetX * i;
            card.current_y = initialY + offsetY3;
            card.render(sb);
        }
    }

    public static class CardInfo {
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
            return card.toString() + energyOnUse;
        }
    }
}
