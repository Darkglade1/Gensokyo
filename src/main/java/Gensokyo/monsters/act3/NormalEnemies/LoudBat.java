package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;

import java.util.HashMap;
import java.util.Map;

public class LoudBat extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("LoudBat");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;
    private static final byte BLOCK = 2;

    private static final int ATTACK_DMG = 8;
    private static final int A2_ATTACK_DMG = 9;
    private int attackDmg;

    private static final int DEBUFF_AMT = 2;
    private static final int A17_DEBUFF_AMT = 3;
    private int debuff;

    private static final int BLOCK_AMT = 8;
    private static final int A7_BLOCK_AMT = 9;
    private int block;

    private static final int HP_MIN = 32;
    private static final int HP_MAX = 36;
    private static final int A7_HP_MIN = 34;
    private static final int A7_HP_MAX = 40;

    private Map<Byte, EnemyMoveInfo> moves;

    public LoudBat() {
        this(0.0f, 0.0f);
    }

    public LoudBat(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 50.0F, 210.0F, 180.0F, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/LoudBat/Spriter/LoudBatAnimation.scml");
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime((int)(time * Math.random()));
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuff = A17_DEBUFF_AMT;
        } else {
            this.debuff = DEBUFF_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
            block = A7_BLOCK_AMT;
        } else {
            setHp(HP_MIN, HP_MAX);
            block = BLOCK_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));
        this.moves.put(BLOCK, new EnemyMoveInfo(BLOCK, Intent.DEFEND, -1, 0, false));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
            case DEBUFF: {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuff, true), debuff));
                break;
            }
            case BLOCK: {
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new GainBlockAction(mo, block));
                }
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
        if (this.lastMove(BLOCK)) {
            setMoveShortcut(DEBUFF);
        } else if (this.lastMove(DEBUFF)) {
            setMoveShortcut(ATTACK);
        } else {
            setMoveShortcut(BLOCK);
        }
    }
}