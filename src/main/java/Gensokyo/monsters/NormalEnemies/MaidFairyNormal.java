package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.BetterGainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class MaidFairyNormal extends AbstractFairy
{
    public static final String ID = "Gensokyo:MaidFairy";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte BLOCK_ATTACK = 2;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A2_NORMAL_ATTACK_DAMAGE = 7;
    private static final int BLOCK_ATTACK_DAMAGE = 5;
    private static final int A2_BLOCK_ATTACK_DAMAGE = 6;
    private static final int BLOCK_GAIN = 5;
    private static final int A7_BLOCK_GAIN = 6;
    private int normalDamage;
    private int blockDamage;
    private int block;

    public MaidFairyNormal() {
        this(0.0f, 0.0f, false);
    }

    public MaidFairyNormal(final float x, final float y, boolean isWeak) {
        super(MaidFairyNormal.NAME, ID, HP_MAX, 0, 0, 130.0f, 165.0f, null, x, y, isWeak);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/MaidFairy/Spriter/MaidFairyAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.normalDamage = A2_NORMAL_ATTACK_DAMAGE;
            this.blockDamage = A2_BLOCK_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.blockDamage = BLOCK_ATTACK_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.blockDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
            case BLOCK_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new BetterGainBlockRandomMonsterAction(this, this.block));
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
            if (num < 50 && !this.lastTwoMoves(BLOCK_ATTACK)) {
                this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(1).base);
            } else if (!this.lastTwoMoves(ATTACK)){
                this.setMove(ATTACK, Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(1).base);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:MaidFairy");
        NAME = MaidFairyNormal.monsterStrings.NAME;
        MOVES = MaidFairyNormal.monsterStrings.MOVES;
        DIALOG = MaidFairyNormal.monsterStrings.DIALOG;
    }
}