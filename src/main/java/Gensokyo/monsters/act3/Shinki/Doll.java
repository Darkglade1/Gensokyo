package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.DollJudgement;
import Gensokyo.powers.act3.ExplosiveDoll;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Doll extends AbstractSpriterMonster {
    public static final String ID = GensokyoMod.makeID("Doll");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    protected static final byte NONE = 0;
    protected static final byte EXPLODE = 1;
    protected static final int HP = 20;
    protected static final int A9_HP = 22;
    private static final int EXPLODE_DAMAGE = 20;
    private static final int A4_EXPLODE_DAMAGE = 22;
    public static final int DOLLS_EXPLODE_TIMER = 3;
    private int explodeDamage;
    private static final float HB_W = 50.0F;
    private static final float HB_H = 100.0f;
    protected int cooldown = DOLLS_EXPLODE_TIMER;
    protected Alice master;
    public boolean spawnedByPower;

    public Doll(float x, float y, Alice master) {
        this(x, y, master, false);
    }

    public Doll(float x, float y, Alice master, boolean spawnedByPower) {
        super(NAME, ID, HP, -5.0F, 0, HB_W, HB_H, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Doll/Spriter/DollAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.master = master;
        this.spawnedByPower = spawnedByPower;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            explodeDamage = A4_EXPLODE_DAMAGE;
        } else {
            explodeDamage = EXPLODE_DAMAGE;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplosiveDoll(this, cooldown, explodeDamage)));
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    @Override
    public void die(boolean triggerRelics) {
        if (master != null) {
            if (!spawnedByPower) {
                master.dolls.remove(this);
            }
            AbstractPower power = master.getPower(DollJudgement.POWER_ID);
            if (power != null) {
                power.onSpecificTrigger();
            }
        }
        super.die(false);
    }

    @Override
    public void takeTurn() {
        if (cooldown > 0) {
            cooldown--;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (cooldown > 0) {
            this.setMove(NONE, Intent.NONE);
        } else {
            this.setMove(null, EXPLODE, Intent.UNKNOWN, -1, 0, false);
        }
    }
}
