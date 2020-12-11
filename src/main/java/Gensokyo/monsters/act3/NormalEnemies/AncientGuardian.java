package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.GuardianOfRelics;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BarricadePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AncientGuardian extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("AncientGuardian");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte ATTACK_STATUS = 1;
    private static final byte BUFF = 2;

    private static final int ATTACK_DMG = 18;
    private static final int A2_ATTACK_DMG = 20;
    private int attackDmg;

    private static final int ATTACK_STATUS_DMG = 13;
    private static final int A2_ATTACK_STATUS_DMG = 14;
    private int attackStatusDmg;

    private static final int STATUS_AMT = 1;
    private static final int ARTIFACT = 2;

    private static final int HP_MIN = 100;
    private static final int HP_MAX = 106;
    private static final int A7_HP_MIN = 102;
    private static final int A7_HP_MAX = 112;

    private Map<Byte, EnemyMoveInfo> moves;

    public AncientGuardian() {
        this(0.0f, 0.0f);
    }

    public AncientGuardian(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 390.0f, 425.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/AncientGuardian/Spriter/AncientGuardianAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
            attackStatusDmg = A2_ATTACK_STATUS_DMG;
        } else {
            attackDmg = ATTACK_DMG;
            attackStatusDmg = ATTACK_STATUS_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(ATTACK_STATUS, new EnemyMoveInfo(ATTACK_STATUS, Intent.ATTACK_DEBUFF, attackStatusDmg, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, ARTIFACT)));
        addToBot(new ApplyPowerAction(this, this, new GuardianOfRelics(this)));
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
            case ATTACK_STATUS: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    addToBot(new MakeTempCardInDrawPileAction(new Wound(), STATUS_AMT + 1, true, true));
                } else {
                    addToBot(new MakeTempCardInDrawPileAction(new Wound(), STATUS_AMT, true, true));
                }
                addToBot(new MakeTempCardInDiscardAction(new Wound(), STATUS_AMT));
                break;
            }
            case BUFF: {
                addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
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
        if (this.firstMove) {
            setMoveShortcut(BUFF);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!(this.lastMove(ATTACK) && this.lastMoveBefore(ATTACK))) {
                possibilities.add(ATTACK);
            }
            if (!(this.lastMove(ATTACK_STATUS) && this.lastMoveBefore(ATTACK_STATUS))) {
                possibilities.add(ATTACK_STATUS);
            }
            setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }
}