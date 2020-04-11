package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.monsters.AbstractSpriterMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TanukiDog extends AbstractSpriterMonster
{
    public static final String ID = "Gensokyo:TanukiDog";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte MULTI_ATTACK = 0;
    private static final byte BLOCK_ATTACK = 1;
    private static final byte BUFF = 2;
    private static final int MULTI_ATTACK_DAMAGE = 3;
    private static final int A2_MULTI_ATTACK_DAMAGE = 3;
    private static final int HITS = 3;
    private static final int BLOCK_ATTACK_DAMAGE = 6;
    private static final int A2_BLOCK_ATTACK_DAMAGE = 7;
    private static final int STR = 3;
    private static final int A17_STR = 4;
    private static final int BLOCK = 8;
    private static final int A7_BLOCK = 9;
    private static final int SELF_DEBUFF = 2;
    private static final float BERSERK_THRESHOLD = 0.65F;
    private static final int HP_MIN = 40;
    private static final int HP_MAX = 44;
    private static final int A7_HP_MIN = 42;
    private static final int A7_HP_MAX = 46;
    private int multiAttackDamage;
    private int blockAttackDamage;
    private int strength;
    private int block;
    private boolean firstTurn = true;
    private boolean usedBerserk = false;

    private Map<Byte, EnemyMoveInfo> moves;

    public TanukiDog() {
        this(0.0f, 0.0f);
    }

    public TanukiDog(final float x, final float y) {
        super(TanukiDog.NAME, ID, HP_MAX, -5.0F, 0, 200.0f, 160.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/TanukiDog/Spriter/TanukiDogAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.strength = A17_STR;
        } else {
            this.strength = STR;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.block = A7_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.multiAttackDamage = A2_MULTI_ATTACK_DAMAGE;
            this.blockAttackDamage = A2_BLOCK_ATTACK_DAMAGE;
        } else {
            this.multiAttackDamage = MULTI_ATTACK_DAMAGE;
            this.blockAttackDamage = BLOCK_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, Intent.ATTACK, multiAttackDamage, HITS, true));
        this.moves.put(BLOCK_ATTACK, new EnemyMoveInfo(BLOCK_ATTACK, Intent.ATTACK_DEFEND, blockAttackDamage, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));

    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        if (this.firstTurn) {
            firstTurn = false;
        }
        switch (this.nextMove) {
            case MULTI_ATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case BLOCK_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
                break;
            }
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, SELF_DEBUFF, true), SELF_DEBUFF));
                usedBerserk = true;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (usedBerserk) {
            this.setMoveShortcut(MULTI_ATTACK);
        } else if (currentHealth <= (int)(maxHealth * BERSERK_THRESHOLD)) {
            this.setMoveShortcut(BUFF);
        } else if (firstTurn) {
            this.setMoveShortcut(BLOCK_ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(MULTI_ATTACK) || !this.lastMoveBefore(MULTI_ATTACK)) {
                possibilities.add(MULTI_ATTACK);
            }
            if (!this.lastMove(BLOCK_ATTACK) || !this.lastMoveBefore(BLOCK_ATTACK)) {
                possibilities.add(BLOCK_ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:TanukiDog");
        NAME = TanukiDog.monsterStrings.NAME;
        MOVES = TanukiDog.monsterStrings.MOVES;
        DIALOG = TanukiDog.monsterStrings.DIALOG;
    }
}