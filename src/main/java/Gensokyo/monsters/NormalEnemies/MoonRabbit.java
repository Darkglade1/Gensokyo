package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.GainStrengthRandomMonsterAction;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class MoonRabbit extends CustomMonster
{
    public static final String ID = "Gensokyo:MoonRabbit";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte BLOCK_ATTACK = 1;
    private static final byte BUFF_ATTACK = 2;
    private static final byte SELF_BLOCK_ATTACK = 3;
    private static final int BLOCK_ATTACK_DAMAGE = 1;
    private static final int MULTI_HIT_COUNT = 2;
    private static final int BUFF_ATTACK_DAMAGE = 5;
    private static final int A2_BUFF_ATTACK_DAMAGE = 6;
    private static final int BUFF = 2;
    private static final int A17_BUFF = 3;
    private static final int BLOCK = 5;
    private static final int A7_BLOCK = 6;
    private static final int HP_MIN = 28;
    private static final int HP_MAX = 31;
    private static final int A7_HP_MIN = 30;
    private static final int A7_HP_MAX = 33;
    public static final int BUFFER = 0;
    public static final int SHIELDER = 1;
    public static final int TANKER = 2;
    private int blockAttackDamage;
    private int buffAttackDamage;
    private int buff;
    private int block;
    private int role;

    public MoonRabbit(final float x, final float y, int role) {
        super(MoonRabbit.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 155.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/MoonRabbit/Spriter/MoonRabbitAnimation.scml");
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

        this.blockAttackDamage = BLOCK_ATTACK_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.buffAttackDamage = A2_BUFF_ATTACK_DAMAGE;
        } else {
            this.buffAttackDamage = BUFF_ATTACK_DAMAGE;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
           this.buff = A17_BUFF;
        } else {
           this.buff = BUFF;
        }
        this.role = role;
        this.damage.add(new DamageInfo(this, this.blockAttackDamage));
        this.damage.add(new DamageInfo(this, this.buffAttackDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BLOCK_ATTACK: {
                for (int i = 0; i < MULTI_HIT_COUNT; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                AbstractDungeon.actionManager.addToBottom(new GainBlockRandomMonsterAction(this, block));
                break;
            }
            case SELF_BLOCK_ATTACK: {
                for (int i = 0; i < MULTI_HIT_COUNT; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, block));
                break;
            }
            case BUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainStrengthRandomMonsterAction(this, buff));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (role == BUFFER) {
            this.setMove(BUFF_ATTACK, Intent.ATTACK_BUFF, this.damage.get(1).base);
        } else if (role == SHIELDER){
            this.setMove(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(0).base, MULTI_HIT_COUNT, true);
        } else {
            this.setMove(SELF_BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.damage.get(0).base, MULTI_HIT_COUNT, true);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:MoonRabbit");
        NAME = MoonRabbit.monsterStrings.NAME;
        MOVES = MoonRabbit.monsterStrings.MOVES;
        DIALOG = MoonRabbit.monsterStrings.DIALOG;
    }
}