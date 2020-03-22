package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class Swordslinger extends CustomMonster
{
    public static final String ID = "Gensokyo:Swordslinger";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF = 1;
    private static final byte ATTACK = 2;
    private static final int ATTACK_DAMAGE = 8;
    private static final int A2_ATTACK_DAMAGE = 9;
    private static final int DEBUFF_AMOUNT = 3;
    private static final int A17_DEBUFF_AMOUNT = 4;
    private static final int HP_MIN = 80;
    private static final int HP_MAX = 84;
    private static final int A7_HP_MIN = 83;
    private static final int A7_HP_MAX = 87;
    private int attackDamage;
    private int debuffAmount;

    public Swordslinger() {
        this(0.0f, 0.0f);
    }

    public Swordslinger(final float x, final float y) {
        super(Swordslinger.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 290.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Swordslinger/Spriter/SwordslingerAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuffAmount = A17_DEBUFF_AMOUNT;
        } else {
            this.debuffAmount = DEBUFF_AMOUNT;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, debuffAmount)));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(DEBUFF)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(DEBUFF, Intent.DEBUFF);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, ConstrictedPower.POWER_ID));
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Swordslinger");
        NAME = Swordslinger.monsterStrings.NAME;
        MOVES = Swordslinger.monsterStrings.MOVES;
        DIALOG = Swordslinger.monsterStrings.DIALOG;
    }
}