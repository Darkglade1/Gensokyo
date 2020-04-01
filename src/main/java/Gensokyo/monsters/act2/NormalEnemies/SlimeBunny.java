package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.vfx.FlexibleRainbowCardEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.util.HashMap;
import java.util.Map;

public class SlimeBunny extends AbstractSpriterMonster
{
    public static final String ID = "Gensokyo:SlimeBunny";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte HEAL = 0;
    private static final byte PURIFY = 1;
    private static final byte LEAVE = 2;
    private static final float HEAL_PERCENT = 0.1F;
    private static final int HP_MIN = 20;
    private static final int HP_MAX = 21;
    private static final int A7_HP_MIN = 21;
    private static final int A7_HP_MAX = 22;

    private Map<Byte, EnemyMoveInfo> moves;

    public SlimeBunny() {
        this(0.0f, 0.0f);
    }

    public SlimeBunny(final float x, final float y) {
        super(SlimeBunny.NAME, ID, HP_MAX, -5.0F, 0, 130.0f, 110.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/SlimeBunny/Spriter/SlimeBunnyAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        this.moves = new HashMap<>();
        this.moves.put(HEAL, new EnemyMoveInfo(HEAL, Intent.BUFF, -1, 0, false));
        this.moves.put(PURIFY, new EnemyMoveInfo(PURIFY, IntentEnums.PURIFY, -1, 0, false));
        this.moves.put(LEAVE, new EnemyMoveInfo(LEAVE, Intent.ESCAPE, -1, 0, false));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case HEAL: {
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped() && !mo.isDying) {
                        this.addToBot(new HealAction(mo, this, (int)(mo.maxHealth * HEAL_PERCENT)));
                    }
                }
                break;
            }
            case PURIFY: {
                this.addToBot(new VFXAction(new FlexibleRainbowCardEffect(AbstractDungeon.player)));
                this.addToBot(new RemoveDebuffsAction(AbstractDungeon.player));
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped() && !mo.isDying) {
                        this.addToBot(new VFXAction(new FlexibleRainbowCardEffect(mo)));
                        this.addToBot(new RemoveDebuffsAction(mo));
                    }
                }
                break;
            }
            case LEAVE:
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, LEAVE, Intent.ESCAPE));
                this.animation.setFlip(true, false);
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (onlyMonsterLeft()) {
            this.setMoveShortcut(LEAVE);
        } else {
            if (this.lastMove(PURIFY)) {
                this.setMoveShortcut(HEAL);
            } else {
                this.setMoveShortcut(PURIFY);
            }
        }
    }

    private boolean onlyMonsterLeft() {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped() && !mo.isDying && !mo.id.equals(ID)) {
                return false;
            }
        }
        return true;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:SlimeBunny");
        NAME = SlimeBunny.monsterStrings.NAME;
        MOVES = SlimeBunny.monsterStrings.MOVES;
        DIALOG = SlimeBunny.monsterStrings.DIALOG;
    }
}