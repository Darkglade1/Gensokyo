package Gensokyo.monsters.act1.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.Grudge;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.WeakPower;

public class VengefulSpirit extends CustomMonster
{
    public static final String ID = "Gensokyo:VengefulSpirit";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte BLOCK_ATTACK = 2;
    private static final byte CURSE = 3;
    private static final int DEBUFF_ATTACK_DAMAGE = 6;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 7;
    private static final int BLOCK_ATTACK_DAMAGE = 5;
    private static final int A2_BLOCK_ATTACK_DAMAGE = 6;
    private static final int BLOCK = 5;
    private static final int A7_BLOCK = 6;
    private static final int DEBUFF = 1;
    private static final int MAJOR_CURSE_AMT = 1;
    private static final int MINOR_CURSE_AMT = 1;
    private static final int A17_MINOR_CURSE_AMT = 2;
    private static final int HP_MIN = 63;
    private static final int HP_MAX = 67;
    private static final int A7_HP_MIN = 65;
    private static final int A7_HP_MAX = 69;
    private int debuffDamage;
    private int blockAttackDamage;
    private int block;
    private int debuff;
    private int majorCurses;
    private int minorCurses;

    public VengefulSpirit() {
        this(0.0f, 0.0f);
    }

    public VengefulSpirit(final float x, final float y) {
        super(VengefulSpirit.NAME, ID, HP_MAX, -5.0F, 0, 200.0f, 205.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/VengefulSpirit/Spriter/VengefulSpiritAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        this.majorCurses = MAJOR_CURSE_AMT;
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.minorCurses = A17_MINOR_CURSE_AMT;
        } else {
            this.minorCurses = MINOR_CURSE_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.block = A7_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.debuffDamage = A2_DEBUFF_ATTACK_DAMAGE;
            this.blockAttackDamage = A2_BLOCK_ATTACK_DAMAGE;
        } else {
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.blockAttackDamage = BLOCK_ATTACK_DAMAGE;
        }

        this.debuff = DEBUFF;

        this.damage.add(new DamageInfo(this, this.debuffDamage));
        this.damage.add(new DamageInfo(this, this.blockAttackDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Grudge(this)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff, true)));
                break;
            }
            case BLOCK_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, block));

                break;
            }
            case CURSE: {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Regret(), majorCurses));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Decay(), minorCurses));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(DEBUFF_ATTACK)) {
            this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(1).base);
        } else if (this.lastMove(CURSE)) {
            this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(CURSE, Intent.STRONG_DEBUFF);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:VengefulSpirit");
        NAME = VengefulSpirit.monsterStrings.NAME;
        MOVES = VengefulSpirit.monsterStrings.MOVES;
        DIALOG = VengefulSpirit.monsterStrings.DIALOG;
    }
}