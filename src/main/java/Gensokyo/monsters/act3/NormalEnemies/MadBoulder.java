package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act2.BetterDrawReductionPower;
import Gensokyo.powers.act3.BetterCurlUp;
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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MadBoulder extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("MadBoulder");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte ATTACK_BUFF = 1;
    private static final byte DEBUFF = 2;
    private static final byte BUFF = 3;

    private static final int ATTACK_DMG = 6;
    private static final int A2_ATTACK_DMG = 7;
    private static final int HITS = 2;
    private int attackDmg;

    private static final int BUFF_ATTACK_DMG = 8;
    private static final int A2_BUFF_ATTACK_DMG = 9;
    private int buffAttackDmg;

    private static final int STRENGTH = 2;
    private static final int A17_STRENGTH = 3;
    private int strength;

    private static final int BLOCK = 12;
    private static final int A7_BLOCK = 13;
    private int block;

    private static final int DEBUFF_AMT = 1;

    private static final int HP_MIN = 46;
    private static final int HP_MAX = 52;
    private static final int A7_HP_MIN = 48;
    private static final int A7_HP_MAX = 55;

    private Map<Byte, EnemyMoveInfo> moves;

    public MadBoulder() {
        this(0.0f, 0.0f);
    }

    public MadBoulder(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/MadBoulder/Spriter/MadBoulderAnimation.scml");
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime((int)(time * Math.random()));
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.strength = A17_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
            this.block = A7_BLOCK;
        } else {
            setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
            buffAttackDmg = A2_BUFF_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
            buffAttackDmg = BUFF_ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, HITS, true));
        this.moves.put(ATTACK_BUFF, new EnemyMoveInfo(ATTACK_BUFF, Intent.ATTACK_BUFF, buffAttackDmg, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new BetterCurlUp(this, block)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case ATTACK_BUFF: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(this, this, new BetterCurlUp(this, block)));
                break;
            }
            case DEBUFF: {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BetterDrawReductionPower(AbstractDungeon.player, DEBUFF_AMT), DEBUFF_AMT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
            case BUFF: {
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(null, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (!this.hasPower(BetterCurlUp.POWER_ID) && !firstMove) {
            setMoveShortcut(ATTACK_BUFF);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK) && ! firstMove) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(DEBUFF) && !this.lastMoveBefore(DEBUFF)) {
                possibilities.add(DEBUFF);
            }
            if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
                possibilities.add(BUFF);
            }
            setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }
}