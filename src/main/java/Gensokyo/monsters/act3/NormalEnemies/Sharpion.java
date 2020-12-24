package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.RuthlessStabs;
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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Map;

public class Sharpion extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("Sharpion");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte MULTI_ATTACK = 2;

    private static final int ATTACK_DMG = 20;
    private static final int A2_ATTACK_DMG = 22;
    private int attackDmg;

    private static final int DEBUFF_ATTACK_DAMAGE = 12;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 13;
    private int debuffAttackDamage;

    private static final int MULTI_ATTACK_DAMAGE = 9;
    private static final int A2_MULTI_ATTACK_DAMAGE = 10;
    private static final int HITS = 2;
    private int multiAttackDamage;

    private static final int DEBUFF_AMT = 1;
    private static final int A17_DEBUFF_AMT = 2;
    private int debuffAmt;

    private static final int HP = 165;
    private static final int A7_HP = 180;

    private Map<Byte, EnemyMoveInfo> moves;

    public Sharpion() {
        this(0.0f, 0.0f);
    }

    public Sharpion(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 260.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sharpion/Spriter/SharpionAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuffAmt = A17_DEBUFF_AMT;
        } else {
            this.debuffAmt = DEBUFF_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
            debuffAttackDamage = A2_DEBUFF_ATTACK_DAMAGE;
            multiAttackDamage = A2_MULTI_ATTACK_DAMAGE;
        } else {
            attackDmg = ATTACK_DMG;
            debuffAttackDamage = DEBUFF_ATTACK_DAMAGE;
            multiAttackDamage = MULTI_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, debuffAttackDamage, 0, false));
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, Intent.ATTACK, multiAttackDamage, HITS, true));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new RuthlessStabs(this)));
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case DEBUFF_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                break;
            }
            case MULTI_ATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
        if (this.lastMove(ATTACK)) {
            setMoveShortcut(MULTI_ATTACK);
        } else if (this.lastMove(MULTI_ATTACK)) {
            setMoveShortcut(DEBUFF_ATTACK);
        } else {
            setMoveShortcut(ATTACK);
        }
    }
}