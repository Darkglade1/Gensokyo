package Gensokyo.monsters.act3.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.monsters.AbstractSpriterMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import java.util.HashMap;
import java.util.Map;

public class VampireBat extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("VampireBat");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final byte ATTACK = 0;

    private static final int ATTACK_DMG = 9;
    private static final int A2_ATTACK_DMG = 10;
    private int attackDmg;

    private static final int STRENGTH = 2;

    private static final int HP_MIN = 29;
    private static final int HP_MAX = 35;
    private static final int A7_HP_MIN = 31;
    private static final int A7_HP_MAX = 38;

    private Map<Byte, EnemyMoveInfo> moves;

    public VampireBat() {
        this(0.0f, 0.0f);
    }

    public VampireBat(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 50.0F, 210.0F, 180.0F, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/VampireBat/Spriter/VampireBatAnimation.scml");
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime((int)(time * Math.random()));
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = A2_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK_BUFF, attackDmg, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, STRENGTH), STRENGTH));
        }
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
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(null, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        setMoveShortcut(ATTACK);
    }
}