package Gensokyo.monsters.act1.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
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
import com.megacrit.cardcrawl.powers.WeakPower;

public class WhiteKodama extends CustomMonster
{
    public static final String ID = "Gensokyo:WhiteKodama";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte BLOCK_ATTACK = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 3;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 4;
    private static final int BLOCK_ATTACK_DAMAGE = 4;
    private static final int A2_BLOCK_ATTACK_DAMAGE = 5;
    private static final int BLOCK = 3;
    private static final int A7_BLOCK = 4;
    private static final int DEBUFF = 1;
    private static final int HP_MIN = 20;
    private static final int HP_MAX = 24;
    private static final int A7_HP_MIN = 21;
    private static final int A7_HP_MAX = 25;
    private int debuffDamage;
    private int blockAttackDamage;
    private int block;
    private int debuff;

    public WhiteKodama() {
        this(0.0f, 0.0f);
    }

    public WhiteKodama(final float x, final float y) {
        super(WhiteKodama.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 145.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kodamas/WhiteSpriter/KodamaAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.block = A7_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }


        if (AbstractDungeon.ascensionLevel >= 2) {
            this.blockAttackDamage = A2_BLOCK_ATTACK_DAMAGE;
            this.debuffDamage = A2_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.blockAttackDamage = BLOCK_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        }

        this.debuff = DEBUFF;

        this.damage.add(new DamageInfo(this, this.debuffDamage));
        this.damage.add(new DamageInfo(this, this.blockAttackDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff, true)));
                break;
            }
            case BLOCK_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, block));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(BLOCK_ATTACK)) {
            this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(1).base);
        } else if (!this.lastTwoMoves(DEBUFF_ATTACK)){
            this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(1).base);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:WhiteKodama");
        NAME = WhiteKodama.monsterStrings.NAME;
        MOVES = WhiteKodama.monsterStrings.MOVES;
        DIALOG = WhiteKodama.monsterStrings.DIALOG;
    }
}