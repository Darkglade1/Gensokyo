package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act2.Quicksand;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BigMudSlime extends CustomMonster
{
    public static final String ID = "Gensokyo:BigMudSlime";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final int ATTACK_DAMAGE = 22;
    private static final int A2_ATTACK_DAMAGE = 24;
    private static final int DEBUFF_ATTACK_DAMAGE = 18;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 20;
    private static final int STATUS_AMT = 1;
    private static final int DEBUFF_AMT = 2;
    private static final int POWER_AMT = 2;
    private static final int A17_POWER_AMT = 3;
    private static final int HP_MIN = 110;
    private static final int HP_MAX = 115;
    private static final int A7_HP_MIN = 115;
    private static final int A7_HP_MAX = 119;
    private int attackDamage;
    private int debuffAttackDamage;
    private int powerAmt;

    private Map<Byte, EnemyMoveInfo> moves;

    public BigMudSlime() {
        this(0.0f, 0.0f);
    }

    public BigMudSlime(final float x, final float y) {
        super(BigMudSlime.NAME, ID, HP_MAX, -5.0F, 0, 280.0f, 180.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/BigMudSlime/Spriter/BigMudSlimeAnimation.scml");
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
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Quicksand(this, powerAmt), powerAmt));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, new Color(0.1F, 1.0F, 0.1F, 0.0F))));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
            case DEBUFF_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), STATUS_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, DEBUFF_AMT, true), DEBUFF_AMT));
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
        ArrayList<Byte> possibilities = new ArrayList<>();
        if (!this.lastMove(ATTACK) || !this.lastMoveBefore(ATTACK)) {
            possibilities.add(ATTACK);
        }
        if (!this.lastMove(DEBUFF_ATTACK) || !this.lastMoveBefore(DEBUFF_ATTACK)) {
            possibilities.add(DEBUFF_ATTACK);
        }
        this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
    }

    @Override
    public void die(boolean triggerRelics) {
        this.useShakeAnimation(5.0F);
        super.die(triggerRelics);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:BigMudSlime");
        NAME = BigMudSlime.monsterStrings.NAME;
        MOVES = BigMudSlime.monsterStrings.MOVES;
        DIALOG = BigMudSlime.monsterStrings.DIALOG;
    }
}