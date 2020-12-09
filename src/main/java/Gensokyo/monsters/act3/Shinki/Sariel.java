package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.ExposedBack;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.HashMap;
import java.util.Map;

public class Sariel extends AbstractShinkiDelusion
{
    public static final String ID = GensokyoMod.makeID("Sariel");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GensokyoMod.makeID("DelusionIntents"));
    private static final String[] TEXT = uiStrings.TEXT;

    private boolean firstMove = true;
    private static final byte DEBUFF_ATTACK = 0;
    private static final byte ATTACK = 1;

    private static final int ATTACK_DMG = 30;
    private static final int A4_ATTACK_DMG = 33;
    private int attackDmg;

    private static final int DEBUFF_ATTACK_DMG = 10;
    private static final int A4_DEBUFF_ATTACK_DMG = 11;
    private int debuffAttackDmg;

    private static final int DEBUFF_AMT = 2;
    private static final int A19_DEBUFF_AMT = 3;
    private int debuffAmt;

    private static final int POWER_AMT = 50;
    private static final int A19_POWER_AMT = 25;
    private int powerAmt;

    private static final int HP = 250;
    private static final int A9_HP = 270;

    public AbstractCreature target;

    private Map<Byte, EnemyMoveInfo> moves;

    public Sariel(final float x, final float y, Shinki shinki) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sariel/Spriter/SarielAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.shinki = shinki;
        this.target = shinki;
        this.event1 = new SarielEvent1(shinki);
        this.event2 = new SarielEvent2(shinki);
        this.event3 = new SarielEvent3(shinki);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmt = A19_DEBUFF_AMT;
            this.powerAmt = A19_POWER_AMT;
        } else {
            this.debuffAmt = DEBUFF_AMT;
            this.powerAmt = POWER_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(A9_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            attackDmg = A4_ATTACK_DMG;
            debuffAttackDmg = A4_DEBUFF_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
            debuffAttackDmg = DEBUFF_ATTACK_DMG;
        }
        this.animation.setFlip(true, false);
        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, debuffAttackDmg, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExposedBack(this, powerAmt)));
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, target);
        }
        shinki.halfDead = false;
        switch (this.nextMove) {
            case ATTACK: {
                if (target == AbstractDungeon.player) {
                    useFastAttackAnimation();
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case DEBUFF_ATTACK: {
                if (target == AbstractDungeon.player) {
                    useFastAttackAnimation();
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new VulnerablePower(target, debuffAmt, true), debuffAmt));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (!lastMove(DEBUFF_ATTACK) && !lastMoveBefore(DEBUFF_ATTACK)) {
            setMoveShortcut(DEBUFF_ATTACK);
        } else {
            setMoveShortcut(ATTACK);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
    }

    @Override
    public void applyPowers() {
        if (this.nextMove == -1) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            super.applyPowers();
            return;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
        if (target == shinki) {
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                PowerTip intentTip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[0] + info.output + TEXT[1];
            }
        } else {
            super.applyPowers();
        }
    }

    @Override
    public String eventDialog(int eventNum) {
        if (eventNum == 1 && target == AbstractDungeon.player) {
            return DIALOG[4];
        } else {
            return DIALOG[eventNum];
        }
    }
}