package Gensokyo.monsters.act1.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class ZombieFairyNormal extends AbstractFairy
{
    public static final String ID = "Gensokyo:ZombieFairy";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte APPLY_DEBUFF = 2;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A2_NORMAL_ATTACK_DAMAGE = 7;
    private static final int DEBUFF = 1;
    private int normalDamage;

    public ZombieFairyNormal() {
        this(0.0f, 0.0f, false);
    }

    public ZombieFairyNormal(final float x, final float y, boolean isWeak) {
        super(ZombieFairyNormal.NAME, ID, HP_MAX, 0, 0, 130.0f, 165.0f, null, x, y, isWeak);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/ZombieFairy/Spriter/ZombieFairyAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.normalDamage = A2_NORMAL_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.normalDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
            case APPLY_DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF, true), DEBUFF));
                break;
            }
            case REVIVE: {
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                this.halfDead = false;
                Iterator var1 = AbstractDungeon.player.relics.iterator();
                while(var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var1.next();
                    r.onSpawnMonster(this);
                }
                break;
            }
            case LEAVE:
                Leave();
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.deathCounter >= MAX_DEATHS) {
            this.setMove(LEAVE, Intent.ESCAPE);
        } else if (this.halfDead) {
            this.setMove(REVIVE, Intent.NONE);
        } else {
            if (num < 50 && !this.lastTwoMoves(APPLY_DEBUFF)) {
                this.setMove(APPLY_DEBUFF, Intent.DEBUFF);
            } else if (!this.lastTwoMoves(ATTACK)){
                this.setMove(ATTACK, Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                this.setMove(APPLY_DEBUFF, Intent.DEBUFF);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:ZombieFairy");
        NAME = ZombieFairyNormal.monsterStrings.NAME;
        MOVES = ZombieFairyNormal.monsterStrings.MOVES;
        DIALOG = ZombieFairyNormal.monsterStrings.DIALOG;
    }
}