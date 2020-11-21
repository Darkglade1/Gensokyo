package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.AnimatedMoveEffect;
import Gensokyo.monsters.act3.Remilia;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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


public class SistersPlayerPosition extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("SistersPlayerPosition");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SpellCard84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SpellCard32.png"));

    private static float movement = 180.0F * Settings.scale;
    private float position1;
    private float position2;
    private float position3;
    private boolean firstTime = true;

    public ArrayList<Integer> unsafeLanes = new ArrayList<>();

    public SistersPlayerPosition(AbstractCreature owner, int amount) {
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
            if (mo.hasPower(SistersPosition.POWER_ID)) {
                if (power1 == null) {
                    power1 = mo.getPower(SistersPosition.POWER_ID);
                } else if (power2 == null) {
                    power2 = mo.getPower(SistersPosition.POWER_ID);
                }
            }
        }
        if (power1 != null && power2 != null) {
            SistersPosition sister1 = (SistersPosition)power1;
            SistersPosition sister2 = (SistersPosition)power2;

            SistersPosition ally;
            SistersPosition enemy;

            if (sister1.ally) {
                ally = sister1;
                enemy = sister2;
            } else {
                ally = sister2;
                enemy = sister1;
            }

            if (!firstTime) {
                ally.amount = newAllyPosition();
                enemy.amount = newEnemyPosition(ally.amount);
            } else {
                firstTime = false;
            }

            updateSisterPositions(ally);
            updateSisterPositions(enemy);
            ally.updateDescription();
            enemy.updateDescription();

            int difference = Math.abs(ally.amount - enemy.amount);
            if (difference == 0) {
                if (ally.amount == 3) {
                    unsafeLanes.add(1);
                }
                if (ally.amount == 1) {
                    unsafeLanes.add(3);
                }
            }
            if (difference == 1) {
                if (ally.amount == 3 && enemy.amount == 2) {
                    unsafeLanes.add(1);
                    unsafeLanes.add(2);
                }
                if (ally.amount == 2 && enemy.amount == 3) {
                    unsafeLanes.add(3);
                }
                if (ally.amount == 2 && enemy.amount == 1) {
                    unsafeLanes.add(1);
                }
                if (ally.amount == 1 && enemy.amount == 2) {
                    unsafeLanes.add(2);
                    unsafeLanes.add(3);
                }
            }
            if (difference == 2) {
                unsafeLanes.add(1);
                unsafeLanes.add(2);
                unsafeLanes.add(3);
            }
        }
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        //HARDCODED JANK REEEEEEEEEEEEEEEE
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo.id.equals(Remilia.ID)) {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        mo.halfDead = true;
                        mo.healthBarUpdatedEvent();
                        this.isDone = true;
                    }
                });
                break;
            }
        }
    }

    private void fixOrbPositioning() {
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            (AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
        }
    }

    private int newAllyPosition() {
        ArrayList<Integer> possiblePositions = new ArrayList<>();
        possiblePositions.add(1);
        possiblePositions.add(2);
        possiblePositions.add(3);
        Collections.shuffle(possiblePositions, AbstractDungeon.monsterRng.random);
        return possiblePositions.get(0);
    }

    private int newEnemyPosition(int allyPosition) {
        ArrayList<Integer> possiblePositions = new ArrayList<>();
        if (allyPosition != 3) {
            possiblePositions.add(1);
        }
        if (allyPosition != 2) {
            possiblePositions.add(2);
        }
        if (allyPosition != 1) {
            possiblePositions.add(3);
        }
        Collections.shuffle(possiblePositions, AbstractDungeon.monsterRng.random);
        return possiblePositions.get(0);
    }

    private void updateSisterPositions(AbstractPower power) {
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
//        if (moveUp) {
//            if (power.owner instanceof Byakuren) {
//                ((Byakuren)power.owner).runAnim("MoveUp");
//            }
//            if (power.owner instanceof Miko) {
//                ((Miko)power.owner).runAnim("MoveUp");
//            }
//        }
//        if (moveDown) {
//            if (power.owner instanceof Byakuren) {
//                ((Byakuren)power.owner).runAnim("MoveDown");
//            }
//            if (power.owner instanceof Miko) {
//                ((Miko)power.owner).runAnim("MoveDown");
//            }
//        }
    }

    @Override
    public void onRemove() {
        this.owner.drawY = position1;
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
