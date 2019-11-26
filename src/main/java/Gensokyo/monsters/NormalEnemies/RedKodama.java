package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class RedKodama extends CustomMonster
{
    public static final String ID = "Gensokyo:RedKodama";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte DEBUFF = 2;
    private static final int NORMAL_ATTACK_DAMAGE = 7;
    private static final int A2_NORMAL_ATTACK_DAMAGE = 8;
    private static final int STATUS = 1;
    private static final int A17_STATUS = 2;
    private static final int HP_MIN = 17;
    private static final int HP_MAX = 21;
    private static final int A7_HP_MIN = 18;
    private static final int A7_HP_MAX = 22;
    private int normalDamage;
    private int status;

    public RedKodama() {
        this(0.0f, 0.0f);
    }

    public RedKodama(final float x, final float y) {
        super(RedKodama.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 145.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kodamas/RedSpriter/KodamaAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.normalDamage = A2_NORMAL_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
           this.status = A17_STATUS;
        } else {
           this.status = STATUS;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            }
            case DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), status, true, true));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(DEBUFF)) {
            this.setMove(DEBUFF, Intent.DEBUFF);
        } else if (!this.lastTwoMoves(ATTACK)){
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(DEBUFF, Intent.DEBUFF);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:RedKodama");
        NAME = RedKodama.monsterStrings.NAME;
        MOVES = RedKodama.monsterStrings.MOVES;
        DIALOG = RedKodama.monsterStrings.DIALOG;
    }
}