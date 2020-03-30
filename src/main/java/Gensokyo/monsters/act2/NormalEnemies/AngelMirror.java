package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act2.Reflective;
import basemod.abstracts.CustomMonster;
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
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.FrailPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AngelMirror extends CustomMonster
{
    public static final String ID = "Gensokyo:AngelMirror";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte BLOCK = 2;
    private static final int ATTACK_DAMAGE = 14;
    private static final int A2_ATTACK_DAMAGE = 15;
    private static final int DEBUFF_ATTACK_DAMAGE = 10;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 11;
    private static final int BLOCK_AMT = 15;
    private static final int POWER_AMT = 6;
    private static final int A17_POWER_AMT = 8;
    private static final int DEBUFF_AMT = 1;
    private static final int HP_MIN = 40;
    private static final int HP_MAX = 42;
    private static final int A7_HP_MIN = 41;
    private static final int A7_HP_MAX = 43;
    private int attackDamage;
    private int debuffAttackDamage;
    private int powerAmt;
    private boolean firstTurn = true;

    private Map<Byte, EnemyMoveInfo> moves;

    public AngelMirror() {
        this(0.0f, 0.0f);
    }

    public AngelMirror(final float x, final float y) {
        super(AngelMirror.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 220.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/AngelMirror/Spriter/AngelMirrorAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.powerAmt = A17_POWER_AMT;
        } else {
            this.powerAmt = POWER_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
            this.debuffAttackDamage = A2_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
            this.debuffAttackDamage = DEBUFF_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDamage, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, debuffAttackDamage, 0, false));
        this.moves.put(BLOCK, new EnemyMoveInfo(BLOCK, Intent.DEFEND, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, BLOCK_AMT));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Reflective(this, powerAmt), powerAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        if (firstTurn) {
            firstTurn = false;
        }
        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                break;
            }
            case DEBUFF_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
            case BLOCK: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, BLOCK_AMT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(null, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (!firstTurn && this.currentBlock <= 0) {
            this.setMoveShortcut(BLOCK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK) || !this.lastMoveBefore(ATTACK)) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(DEBUFF_ATTACK) || !this.lastMoveBefore(DEBUFF_ATTACK)) {
                possibilities.add(DEBUFF_ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        this.useShakeAnimation(5.0F);
        super.die(triggerRelics);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:AngelMirror");
        NAME = AngelMirror.monsterStrings.NAME;
        MOVES = AngelMirror.monsterStrings.MOVES;
        DIALOG = AngelMirror.monsterStrings.DIALOG;
    }
}