package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.monsters.AbstractSpriterMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chomper extends AbstractSpriterMonster
{
    public static final String ID = "Gensokyo:Chomper";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 0;
    private static final byte LIFESTEAL_ATTACK = 1;
    private static final byte DEBUFF = 2;
    private static final byte STRONG_DEBUFF = 3;
    private static final int ATTACK_DAMAGE = 7;
    private static final int A2_ATTACK_DAMAGE = 8;
    private static final int HITS = 2;
    private static final int LIFESTEAL_DAMAGE = 9;
    private static final int A2_LIFESTEAL_DAMAGE = 10;
    private static final int DEBUFF_AMT = 2;
    private static final int STR = 2;
    private static final int POWER_AMT = 4;
    private static final int A17_POWER_AMT = 5;
    private static final int HP_MIN = 90;
    private static final int HP_MAX = 94;
    private static final int A7_HP_MIN = 93;
    private static final int A7_HP_MAX = 97;
    private int attackDamage;
    private int lifestealAttackDamage;
    private int powerAmt;
    private boolean firstTurn = true;

    private Map<Byte, EnemyMoveInfo> moves;

    public Chomper() {
        this(0.0f, 0.0f);
    }

    public Chomper(final float x, final float y) {
        super(Chomper.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 220.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Chomper/Spriter/ChomperAnimation.scml");
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
            this.lifestealAttackDamage = A2_LIFESTEAL_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
            this.lifestealAttackDamage = LIFESTEAL_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDamage, HITS, true));
        this.moves.put(LIFESTEAL_ATTACK, new EnemyMoveInfo(LIFESTEAL_ATTACK, Intent.ATTACK_BUFF, lifestealAttackDamage, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));
        this.moves.put(STRONG_DEBUFF, new EnemyMoveInfo(STRONG_DEBUFF, Intent.STRONG_DEBUFF, -1, 0, false));
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
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CHARTREUSE.cpy()), 0.2F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                }
                break;
            }
            case LIFESTEAL_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CHARTREUSE.cpy()), 0.2F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, STR)));
                break;
            }
            case STRONG_DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, powerAmt), powerAmt));
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
        if (firstTurn) {
            this.setMoveShortcut(STRONG_DEBUFF);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK)) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(LIFESTEAL_ATTACK)) {
                possibilities.add(LIFESTEAL_ATTACK);
            }
            if (!this.lastMove(DEBUFF)) {
                possibilities.add(DEBUFF);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Chomper");
        NAME = Chomper.monsterStrings.NAME;
        MOVES = Chomper.monsterStrings.MOVES;
        DIALOG = Chomper.monsterStrings.DIALOG;
    }
}