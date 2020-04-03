package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.AnimatedMoveEffect;
import Gensokyo.monsters.act2.Byakuren;
import Gensokyo.monsters.act2.Miko;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makePowerPath;


public class RivalPlayerPosition extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("RivalPlayerPosition");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SpellCard84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SpellCard32.png"));
    private static final Texture X = TextureLoader.getTexture("GensokyoResources/images/monsters/Byakuren/X.png");
    private TextureRegion X_REGION;

    private static float movement = 180.0F * Settings.scale;
    private float position1;
    private float position2;
    private float position3;
    private boolean firstTime = true;

    public ArrayList<Integer> unsafeLanes = new ArrayList<>();

    public RivalPlayerPosition(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        position1 = owner.drawY;
        position2 = owner.drawY + movement;
        position3 = owner.drawY + movement * 2;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        this.X_REGION = new TextureRegion(X);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        setUpNewRound();
    }

    public boolean isInUnsafeLane() {
        return unsafeLanes.contains(amount);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount > 1) {
                this.flash();
                amount--;
                updatePosition();
            }
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            if (amount < 3) {
                this.flash();
                amount++;
                updatePosition();
            }
        }
        updateDescription();
    }

    private void updatePosition() {
        if (amount == 1) {
            owner.drawY = position1;
        }
        if (amount == 2) {
            owner.drawY = position2;
        }
        if (amount == 3) {
            owner.drawY = position3;
        }
        owner.dialogY = owner.drawY - (owner.hb_y - 55.0F) * Settings.scale;
        fixOrbPositioning();
        AbstractDungeon.onModifyPower(); //update rivals' intents
    }

    @Override
    public void atEndOfRound() {
        setUpNewRound();
    }

    private void setUpNewRound() {
        AbstractPower power1 = null;
        AbstractPower power2 = null;
        unsafeLanes.clear();
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo.hasPower(RivalPosition.POWER_ID)) {
                if (power1 == null) {
                    power1 = mo.getPower(RivalPosition.POWER_ID);
                } else if (power2 == null) {
                    power2 = mo.getPower(RivalPosition.POWER_ID);
                }
            }
        }
        if (power1 != null && power2 != null) {

            if (!firstTime) {
                power1.amount = newPosition(power1.amount);
                power2.amount = newPosition(power2.amount);
            } else {
                firstTime = false;
            }

            updateRivalPositions(power1);
            updateRivalPositions(power2);
            power1.updateDescription();
            power2.updateDescription();

            int difference = Math.abs(power1.amount - power2.amount);
            if (difference == 0) {
                unsafeLanes.add(power1.amount);
            }
            if (difference == 1) {
                unsafeLanes.add(power1.amount);
                unsafeLanes.add(power2.amount);
            }
            if (difference == 2) {
                unsafeLanes.add(difference);
            }
        }
        updateDescription();
    }

    private void fixOrbPositioning() {
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            (AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
        }
    }

    private int newPosition(int initialPosition) {
        ArrayList<Integer> possiblePositions = new ArrayList<>();
        possiblePositions.add(1);
        possiblePositions.add(2);
        possiblePositions.add(3);
        Collections.shuffle(possiblePositions, AbstractDungeon.monsterRng.random);
        return possiblePositions.get(0);
    }

    private void updateRivalPositions(AbstractPower power) {
        boolean moveUp = false;
        boolean moveDown = false;
        if (power.amount == 1) {
            AbstractDungeon.effectList.add(new AnimatedMoveEffect((AbstractMonster)power.owner, power.owner.drawX, power.owner.drawY, power.owner.drawX, position1));
            if (power.owner.drawY < position1) {
                moveUp = true;
            }
            if (power.owner.drawY > position1) {
                moveDown = true;
            }
        }
        if (power.amount == 2) {
            AbstractDungeon.effectList.add(new AnimatedMoveEffect((AbstractMonster)power.owner, power.owner.drawX, power.owner.drawY, power.owner.drawX, position2));
            if (power.owner.drawY < position2) {
                moveUp = true;
            }
            if (power.owner.drawY > position2) {
                moveDown = true;
            }
        }
        if (power.amount == 3) {
            AbstractDungeon.effectList.add(new AnimatedMoveEffect((AbstractMonster)power.owner, power.owner.drawX, power.owner.drawY, power.owner.drawX, position3));
            if (power.owner.drawY < position3) {
                moveUp = true;
            }
            if (power.owner.drawY > position3) {
                moveDown = true;
            }
        }
        if (moveUp) {
            if (power.owner instanceof Byakuren) {
                ((Byakuren)power.owner).runAnim("MoveUp");
            }
            if (power.owner instanceof Miko) {
                ((Miko)power.owner).runAnim("MoveUp");
            }
        }
        if (moveDown) {
            if (power.owner instanceof Byakuren) {
                ((Byakuren)power.owner).runAnim("MoveDown");
            }
            if (power.owner instanceof Miko) {
                ((Miko)power.owner).runAnim("MoveDown");
            }
        }
    }

    @Override
    public void onRemove() {
        this.owner.drawY = position1;
        AbstractDungeon.player.drawX -= 480.0F * Settings.scale;
        super.onRemove();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        for (Integer lane : unsafeLanes) {
            description  = description + DESCRIPTIONS[2] + lane + DESCRIPTIONS[3];
        }
    }
}
