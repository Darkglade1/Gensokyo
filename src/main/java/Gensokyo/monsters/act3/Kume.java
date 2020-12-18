package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kume extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("Kume");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final byte MULTI_ATTACK = 0;
    private static final byte ATTACK_DEBUFF = 1;
    private static final byte BUFF = 2;

    private static final int MULTI_ATTACK_DMG = 7;
    private static final int A2_MULTI_ATTACK_DMG = 8;
    private static final int MULTI_ATTACK_HITS = 2;
    private int multiAttackDmg;

    private static final int ATTACK_DEBUFF_DMG = 11;
    private static final int A2_ATTACK_DEBUFF_DMG = 12;
    private int attackDebuffDmg;

    private static final int DEBUFF_AMT = 1;
    private static final int A17_DEBUFF_AMT = 2;
    private int debuffAmt;

    private static final int STRENGTH = 2;
    private static final int A17_STRENGTH = 3;
    private int strength;

    private static final int HP_MIN = 55;
    private static final int HP_MAX = 60;
    private static final int A7_HP_MIN = 57;
    private static final int A7_HP_MAX = 65;

    private Map<Byte, EnemyMoveInfo> moves;

    public Kume() {
        this(0.0f, 0.0f);
    }

    public Kume(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 50.0f, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kume/Spriter/KumeAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.strength = A17_STRENGTH;
            this.debuffAmt = A17_DEBUFF_AMT;
        } else {
            this.strength = STRENGTH;
            this.debuffAmt = DEBUFF_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            multiAttackDmg = A2_MULTI_ATTACK_DMG;
            attackDebuffDmg = A2_ATTACK_DEBUFF_DMG;
        } else {
            multiAttackDmg = MULTI_ATTACK_DMG;
            attackDebuffDmg = ATTACK_DEBUFF_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, Intent.ATTACK, multiAttackDmg, MULTI_ATTACK_HITS, true));
        this.moves.put(ATTACK_DEBUFF, new EnemyMoveInfo(ATTACK_DEBUFF, Intent.ATTACK_DEBUFF, attackDebuffDmg, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case MULTI_ATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < MULTI_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case ATTACK_DEBUFF: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
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
        ArrayList<Byte> possibilities = new ArrayList<>();
        if (!this.lastMove(MULTI_ATTACK)) {
            possibilities.add(MULTI_ATTACK);
        }
        if (!this.lastMove(ATTACK_DEBUFF) && !this.lastMoveBefore(ATTACK_DEBUFF)) {
            possibilities.add(ATTACK_DEBUFF);
        }
        if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
            possibilities.add(BUFF);
        }
        setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
    }
}