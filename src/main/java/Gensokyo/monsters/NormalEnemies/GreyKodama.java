package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class GreyKodama extends CustomMonster
{
    public static final String ID = "Gensokyo:GreyKodama";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte BUFF_ATTACK = 2;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A2_NORMAL_ATTACK_DAMAGE = 7;
    private static final int BUFF_ATTACK_DAMAGE = 3;
    private static final int BUFF = 1;
    private static final int A17_BUFF = 2;
    private static final int HP_MIN = 16;
    private static final int HP_MAX = 20;
    private static final int A7_HP_MIN = 17;
    private static final int A7_HP_MAX = 21;
    private int normalDamage;
    private int buffAttackDamage;
    private int buff;

    public GreyKodama() {
        this(0.0f, 0.0f);
    }

    public GreyKodama(final float x, final float y) {
        super(GreyKodama.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 145.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kodamas/GreySpriter/KodamaAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        this.buffAttackDamage = BUFF_ATTACK_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.normalDamage = A2_NORMAL_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
           this.buff = A17_BUFF;
        } else {
           this.buff = BUFF;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.buffAttackDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
            case BUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, buff)));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(BUFF_ATTACK)) {
            this.setMove(BUFF_ATTACK, Intent.ATTACK_BUFF, this.damage.get(1).base);
        } else if (!this.lastTwoMoves(ATTACK)){
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(BUFF_ATTACK, Intent.ATTACK_BUFF, this.damage.get(1).base);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:GreyKodama");
        NAME = GreyKodama.monsterStrings.NAME;
        MOVES = GreyKodama.monsterStrings.MOVES;
        DIALOG = GreyKodama.monsterStrings.DIALOG;
    }
}