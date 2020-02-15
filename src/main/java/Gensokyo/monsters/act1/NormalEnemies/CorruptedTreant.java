package Gensokyo.monsters.act1.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.RottingWood;
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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class CorruptedTreant extends CustomMonster
{
    public static final String ID = "Gensokyo:CorruptedTreant";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF = 1;
    private static final byte ATTACK = 2;
    private static final int ATTACK_DAMAGE = 9;
    private static final int A2_ATTACK_DAMAGE = 10;
    private static final int DEBUFF_AMOUNT = 1;
    private static final int POWER_THRESHOLD = 15;
    private static final int POWER_DAMAGE = 4;
    private static final int A17_POWER_DAMAGE = 6;
    private static final int HP_MIN = 48;
    private static final int HP_MAX = 52;
    private static final int A7_HP_MIN = 50;
    private static final int A7_HP_MAX = 54;
    private int attackDamage;
    private int debuff;
    private int powerDamage;

    public CorruptedTreant() {
        this(0.0f, 0.0f);
    }

    public CorruptedTreant(final float x, final float y) {
        super(CorruptedTreant.NAME, ID, HP_MAX, -5.0F, 0, 220.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/CorruptedTreant/Spriter/CorruptedTreantAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.powerDamage = A17_POWER_DAMAGE;
        } else {
            this.powerDamage = POWER_DAMAGE;
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

        this.debuff = DEBUFF_AMOUNT;

        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RottingWood(this, POWER_THRESHOLD, powerDamage)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff, true)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuff, true)));
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
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:CorruptedTreant");
        NAME = CorruptedTreant.monsterStrings.NAME;
        MOVES = CorruptedTreant.monsterStrings.MOVES;
        DIALOG = CorruptedTreant.monsterStrings.DIALOG;
    }
}