package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act2.GlitterPower;
import Gensokyo.powers.act3.Sinflower;
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
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Map;

public class Rafflesia extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("Rafflesia");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;

    private static final int ATTACK_DMG = 20;
    private static final int A2_ATTACK_DMG = 22;
    private int attackDmg;

    private static final int STATUS_AMT = 3;

    private static final int CONTRICTED_AMT = 5;
    private static final int A17_CONSTRICTED_AMT = 7;
    private int constricted;

    private static final int GLITTER_AMT = 1;
    private static final int A17_GLITTER_AMT = 2;
    private int glitter;

    private static final int HP = 280;
    private static final int A7_HP = 300;

    private Map<Byte, EnemyMoveInfo> moves;

    public Rafflesia() {
        this(0.0f, 0.0f);
    }

    public Rafflesia(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 390.0f, 425.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Rafflesia/Spriter/RafflesiaAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            constricted = A17_CONSTRICTED_AMT;
            glitter = A17_GLITTER_AMT;
        } else {
            constricted = CONTRICTED_AMT;
            glitter = GLITTER_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.STRONG_DEBUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new Sinflower(this)));
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                break;
            }
            case DEBUFF: {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new GlitterPower(AbstractDungeon.player, glitter), glitter));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, constricted), constricted));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, STATUS_AMT, true), STATUS_AMT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, STATUS_AMT, true), STATUS_AMT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, STATUS_AMT, true), STATUS_AMT));
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
        if (!this.lastMove(DEBUFF) && !this.lastMoveBefore(DEBUFF)) {
            setMoveShortcut(DEBUFF);
        } else {
            setMoveShortcut(ATTACK);
        }
    }
}