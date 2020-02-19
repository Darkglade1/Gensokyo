package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class PurpleSoul extends CustomMonster
{
    public static final String ID = "Gensokyo:PurpleSoul";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final int ATTACK_DAMAGE = 6;
    private static final int HP_MIN = 34;
    private static final int HP_MAX = 36;
    private static final int A9_HP_MIN = 36;
    private static final int A9_HP_MAX = 38;
    private int attackDamage;
    private Yuyuko yuyuko;

    public PurpleSoul() {
        this(0.0f, 0.0f, null);
    }

    public PurpleSoul(final float x, final float y, Yuyuko yuyuko) {
        super(NAME, ID, HP_MAX, 0.0F, 0, 150.0f, 150.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/PurpleSoul/Spriter/PurpleSoulAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.yuyuko = yuyuko;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP_MIN, A9_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        this.attackDamage = ATTACK_DAMAGE;
        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        if (yuyuko != null) {
            yuyuko.minions[1] = null;
            yuyuko.incrementFan(1);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:PurpleSoul");
        NAME = PurpleSoul.monsterStrings.NAME;
        MOVES = PurpleSoul.monsterStrings.MOVES;
        DIALOG = PurpleSoul.monsterStrings.DIALOG;
    }
}