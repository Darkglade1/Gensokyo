package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act1.FortitudePower;
import Gensokyo.powers.act3.Titan;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.util.HashMap;
import java.util.Map;

public class AtlasGolem extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("AtlasGolem");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;

    private static final byte ATTACK = 0;
    private static final byte UNKNOWN1 = 1;
    private static final byte UNKNOWN2 = 2;

    private static final int MAX_DAMAGE = 50;
    private static final int A2_MAX_DAMAGE = 60;
    private int maxDamage;

    private static final int HP = 200;
    private static final int A7_HP = 220;

    private Map<Byte, EnemyMoveInfo> moves;

    public AtlasGolem() {
        this(0.0f, 0.0f);
    }

    public AtlasGolem(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 390.0f, 325.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/AtlasGolem/Spriter/GolemAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.maxDamage = A2_MAX_DAMAGE;
        } else {
            this.maxDamage = MAX_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.damage.add(new DamageInfo(this, -1));
        this.moves.put(UNKNOWN1, new EnemyMoveInfo(UNKNOWN1, Intent.UNKNOWN, -1, 0, false));
        this.moves.put(UNKNOWN2, new EnemyMoveInfo(UNKNOWN2, Intent.UNKNOWN, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new Titan(this, maxDamage)));
        if (AbstractDungeon.ascensionLevel >= 17) {
            addToBot(new ApplyPowerAction(this, this, new FortitudePower(this, 1, false), 1));
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            firstMove = false;
        }
        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.nextMove == ATTACK) {
            setAttack();
            createIntent();
        }
    }

    private void setAttack() {
        int damage = this.currentHealth;
        damage = Math.min(damage, maxDamage);
        this.damage.get(0).base = damage;
        this.applyPowers();
        this.setMove(MOVES[ATTACK], ATTACK, Intent.ATTACK, this.damage.get(0).base, 0, false);
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(UNKNOWN1)) {
            setMoveShortcut(UNKNOWN2);
            System.out.println("hello2");
        } else if (this.lastMove(UNKNOWN2)) {
            setAttack();
            System.out.println("hello3");
        } else if (this.lastMove(ATTACK)){
            setMoveShortcut(UNKNOWN1);
            System.out.println("hello4");
        } else {
            setMoveShortcut(UNKNOWN1);
            System.out.println("hello1");
        }
    }
}