package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act2.GlitterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Map;

public class Gloop extends AbstractSpriterMonster
{
    public static final String ID = "Gensokyo:Gloop";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte STRONG_DEBUFF = 0;
    private static final byte DEBUFF = 1;
    private static final byte ATTACK = 2;
    private static final int ATTACK_DAMAGE = 8;
    private static final int A2_ATTACK_DAMAGE = 9;
    private static final int DEBUFF_AMT = 3;
    private static final int A17_DEBUFF_AMT = 4;
    private static final int STRONG_DEBUFF_AMT = 1;
    private static final int A17_STRONG_DEBUFF_AMT = 1;
    private static final int HP_MIN = 46;
    private static final int HP_MAX = 50;
    private static final int A7_HP_MIN = 48;
    private static final int A7_HP_MAX = 52;
    private int attackDamage;
    private int debuff_amt;
    private int strongDebuff;
    private boolean canVulnerable;
    private boolean firstTurn = true;

    private Map<Byte, EnemyMoveInfo> moves;

    public Gloop() {
        this(0.0f, 0.0f, false);
    }

    public Gloop(final float x, final float y, boolean canVulnerable) {
        super(Gloop.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 220.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Gloop/Spriter/GloopAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.canVulnerable = canVulnerable;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuff_amt = A17_DEBUFF_AMT;
            this.strongDebuff = A17_STRONG_DEBUFF_AMT;
        } else {
            this.debuff_amt = DEBUFF_AMT;
            this.strongDebuff = STRONG_DEBUFF_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(STRONG_DEBUFF, new EnemyMoveInfo(STRONG_DEBUFF, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDamage, 0, false));

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
            case STRONG_DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new GlitterPower(AbstractDungeon.player, strongDebuff), strongDebuff));
                break;
            }
            case DEBUFF: {
                if (!AbstractDungeon.player.hasPower(WeakPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff_amt, true), debuff_amt));
                } else if (!AbstractDungeon.player.hasPower(FrailPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuff_amt, true), debuff_amt));
                } else if (canVulnerable && !AbstractDungeon.player.hasPower(VulnerablePower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, debuff_amt, true), debuff_amt));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff_amt, true), debuff_amt));
                }
                break;
            }
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
        }
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo instanceof Gloop) {
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(mo)); //hacky way to make these monsters roll at end of round
            }
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstTurn) {
            this.setMoveShortcut(STRONG_DEBUFF);
        } else if (!AbstractDungeon.player.hasPower(WeakPower.POWER_ID) || !AbstractDungeon.player.hasPower(FrailPower.POWER_ID) || (canVulnerable && !AbstractDungeon.player.hasPower(VulnerablePower.POWER_ID))) {
            this.setMoveShortcut(DEBUFF);
        } else {
            this.setMoveShortcut(ATTACK);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Gloop");
        NAME = Gloop.monsterStrings.NAME;
        MOVES = Gloop.monsterStrings.MOVES;
        DIALOG = Gloop.monsterStrings.DIALOG;
    }
}