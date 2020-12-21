package Gensokyo.monsters.act3.NormalEnemies;

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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.HashMap;
import java.util.Map;

public class FeralBat extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("FeralBat");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final byte ATTACK = 0;
    private static final byte BUFF = 1;

    private static final int ATTACK_DMG = 7;
    private static final int A2_ATTACK_DMG = 8;
    private int attackDmg;

    private static final int STRENGTH = 2;
    private static final int A17_STRENGTH = 3;
    private int strength;

    private static final int HP_MIN = 27;
    private static final int HP_MAX = 30;
    private static final int A7_HP_MIN = 29;
    private static final int A7_HP_MAX = 34;

    private Map<Byte, EnemyMoveInfo> moves;

    public FeralBat() {
        this(0.0f, 0.0f);
    }

    public FeralBat(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 50.0F, 210.0F, 180.0F, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/FeralBat/Spriter/FeralBatAnimation.scml");
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
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                break;
            }
            case BUFF: {
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    addToBot(new ApplyPowerAction(mo, this, new StrengthPower(mo, strength), strength));
                }
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
            setMoveShortcut(BUFF);
        } else {
            setMoveShortcut(ATTACK);
        }
    }
}