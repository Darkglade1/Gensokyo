package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.Trickster;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class Kitsune extends CustomMonster
{
    public static final String ID = "Gensokyo:Kitsune";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte ATTACK = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 5;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 6;
    private static final int ATTACK_DAMAGE = 12;
    private static final int A2_ATTACK_DAMAGE = 13;
    private static final int STATUS = 1;
    private static final int A17_STATUS = 2;
    private static final int HP_MIN = 42;
    private static final int HP_MAX = 46;
    private static final int A7_HP_MIN = 44;
    private static final int A7_HP_MAX = 48;
    private int debuffDamage;
    private int attackDamage;
    private int status;

    public Kitsune() {
        this(0.0f, 0.0f);
    }

    public Kitsune(final float x, final float y) {
        super(Kitsune.NAME, ID, HP_MAX, -5.0F, 0, 200.0f, 190.0F, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kitsune/Spriter/KitsuneAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.status = A17_STATUS;
        } else {
            this.status = STATUS;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.debuffDamage = A2_DEBUFF_ATTACK_DAMAGE;
            this.attackDamage = A2_ATTACK_DAMAGE;
        } else {
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.attackDamage = ATTACK_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.debuffDamage));
        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Trickster(this)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), status));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(ATTACK)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base);
        } else if (!this.lastTwoMoves(DEBUFF_ATTACK)) {
            this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Kitsune");
        NAME = Kitsune.monsterStrings.NAME;
        MOVES = Kitsune.monsterStrings.MOVES;
        DIALOG = Kitsune.monsterStrings.DIALOG;
    }
}