package Gensokyo.monsters.NormalEnemies;

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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class GreaterFairyNormal extends AbstractFairy
{
    public static final String ID = "Gensokyo:GreaterFairy";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte BUFF = 1;
    private static final byte MULTI_ATTACK = 2;
    private static final int STRENGTH = 2;
    private static final int MULTI_ATTACK_DAMAGE = 3;
    private static final int A2_MULTI_ATTACK_DAMAGE = 4;
    private static final int MULTI_HITS = 2;
    private static final int HP_MIN = 6;
    private static final int HP_MAX = 8;
    private static final int A7_HP_MIN = 7;
    private static final int A7_HP_MAX = 9;
    private int strength;
    private int multiHitDamage;

    public GreaterFairyNormal() {
        this(0.0f, 0.0f);
    }

    public GreaterFairyNormal(final float x, final float y) {
        super(GreaterFairyNormal.NAME, ID, HP_MAX, 0, 0, 130.0f, 165.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/GreaterFairy/Spriter/GreaterFairyAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.strength = STRENGTH;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.multiHitDamage = A2_MULTI_ATTACK_DAMAGE;
        } else {
            this.multiHitDamage = MULTI_ATTACK_DAMAGE;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        this.damage.add(new DamageInfo(this, this.multiHitDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                break;
            }
            case MULTI_ATTACK: {
                for (int i = 0; i < MULTI_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
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
            if (num < 50 && !this.lastTwoMoves(MULTI_ATTACK)) {
                this.setMove(MULTI_ATTACK, Intent.ATTACK, this.damage.get(0).base, MULTI_HITS, true);
            } else if (!this.lastTwoMoves(BUFF)){
                this.setMove(BUFF, Intent.BUFF);
            } else {
                this.setMove(MULTI_ATTACK, Intent.ATTACK, this.damage.get(0).base, MULTI_HITS, true);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:GreaterFairy");
        NAME = GreaterFairyNormal.monsterStrings.NAME;
        MOVES = GreaterFairyNormal.monsterStrings.MOVES;
        DIALOG = GreaterFairyNormal.monsterStrings.DIALOG;
    }
}