package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act2.Spooked;
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
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wraith extends CustomMonster
{
    public static final String ID = "Gensokyo:Wraith";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;
    private static final int ATTACK_DAMAGE = 5;
    private static final int A2_ATTACK_DAMAGE = 6;
    private static final int HITS = 4;
    private static final int DRAIN = 1;
    private static final int POWER_THRESHOLD = 14;
    private static final int A17_POWER_THRESHOLD = 11;
    private static final int HP_MIN = 80;
    private static final int HP_MAX = 84;
    private static final int A7_HP_MIN = 83;
    private static final int A7_HP_MAX = 87;
    private int attackDamage;
    private int powerThreshold;
    private boolean sapStr = true;

    private Map<Byte, EnemyMoveInfo> moves;

    public Wraith() {
        this(0.0f, 0.0f);
    }

    public Wraith(final float x, final float y) {
        super(Wraith.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 230.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Wraith/Spriter/WraithAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.powerThreshold = A17_POWER_THRESHOLD;
        } else {
            this.powerThreshold = POWER_THRESHOLD;
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

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDamage, HITS, true));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Spooked(this, powerThreshold, 1)));
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
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                }
                break;
            }
            case DEBUFF: {
                if (sapStr) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -DRAIN), -DRAIN));
                    sapStr = false;
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -DRAIN), -DRAIN));
                    sapStr = true;
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, DRAIN), DRAIN));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(null, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        ArrayList<Byte> possibilities = new ArrayList<>();
        if (!this.lastMove(ATTACK) || !this.lastMoveBefore(ATTACK)) {
            possibilities.add(ATTACK);
        }
        if (!this.lastMove(DEBUFF)) {
            possibilities.add(DEBUFF);
        }
        this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
    }

    @Override
    public void die(boolean triggerRelics) {
        this.useShakeAnimation(5.0F);
        super.die(triggerRelics);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Wraith");
        NAME = Wraith.monsterStrings.NAME;
        MOVES = Wraith.monsterStrings.MOVES;
        DIALOG = Wraith.monsterStrings.DIALOG;
    }
}