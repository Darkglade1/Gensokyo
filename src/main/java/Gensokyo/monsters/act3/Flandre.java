package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.AnimatedMoveActualAction;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.AndThenThereWereNone;
import Gensokyo.powers.act3.Doom;
import Gensokyo.powers.act3.EyesOfDeath;
import Gensokyo.powers.act3.SistersPlayerPosition;
import Gensokyo.powers.act3.SistersPosition;
import actlikeit.dungeons.CustomDungeon;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Flandre extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("Flandre");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GensokyoMod.makeID("SistersIntents"));
    private static final String[] TEXT = uiStrings.TEXT;

    private boolean firstMove = true;

    private static final byte ATTACK = 0;
    private static final byte LIFE_STEAL_ATTACK = 1;
    private static final byte MULTIATTACK = 2;
    private static final byte BUFF = 3;

    private static final int ATTACK_DAMAGE = 36;
    private static final int A4_ATTACK_DAMAGE = 40;
    private int attackDamage;

    private static final int MULTI_ATTACK_DAMAGE = 16;
    private static final int A4_MULTI_ATTACK_DAMAGE = 17;
    private static final int MULTI_ATTACK_HITS = 2;
    private int multiAttackDamage;

    private static final int LIFESTEAL_ATTACK_DAMAGE = 30;
    private static final int A4_LIFESTEAL_ATTACK_DAMAGE = 33;
    private int lifestealDamage;

    private static final int DOOM = 1;
    private static final int A19_DOOM = 2;
    private int doom;

    private static final float BUFF_HP_THRESHOLD = 0.50f;
    private boolean buffed = false;

    private static final int HEAL = 250;
    private static final int A19_HEAL = 350;
    private int heal;

    private static final int HP = 666;
    private static final int A9_HP = 700;

    public float originalX;
    public float originalY;

    private Remilia sister;

    private Map<Byte, EnemyMoveInfo> moves;

    public Flandre() {
        this(0.0f, 0.0f);
    }

    public Flandre(final float x, final float y) {
        super(NAME, ID, HP, 0.0F, 0.0F, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Flandre/Spriter/FlandreAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.doom = A19_DOOM;
            this.heal = A19_HEAL;
        } else {
            this.doom = DOOM;
            this.heal = HEAL;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.multiAttackDamage = A4_MULTI_ATTACK_DAMAGE;
            this.lifestealDamage = A4_LIFESTEAL_ATTACK_DAMAGE;
            this.attackDamage = A4_ATTACK_DAMAGE;
        } else {
            this.multiAttackDamage = MULTI_ATTACK_DAMAGE;
            this.lifestealDamage = LIFESTEAL_ATTACK_DAMAGE;
            this.attackDamage = ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.attackDamage, 0, false));
        this.moves.put(LIFE_STEAL_ATTACK, new EnemyMoveInfo(LIFE_STEAL_ATTACK, Intent.ATTACK_BUFF, this.lifestealDamage, 0,false));
        this.moves.put(MULTIATTACK, new EnemyMoveInfo(MULTIATTACK, Intent.ATTACK, this.multiAttackDamage, MULTI_ATTACK_HITS, true));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("UNOwen");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EyesOfDeath(this, doom)));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Remilia) {
                sister = (Remilia) mo;
            }
        }
        this.originalX = this.drawX;
        this.originalY = this.drawY;
        if (sister != null) {
            sister.originalX = this.drawX;
            sister.originalY = this.drawY;
        }
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        AbstractCreature target;
        if (!sister.isDeadOrEscaped()) {
            target = sister;
            if (AbstractDungeon.player.hasPower(SistersPlayerPosition.POWER_ID)) {
                if (((SistersPlayerPosition)AbstractDungeon.player.getPower(SistersPlayerPosition.POWER_ID)).isInUnsafeLane()) {
                    target = AbstractDungeon.player;
                }
            }
        } else {
            target = AbstractDungeon.player;
        }

        if(info.base > -1) {
            info.applyPowers(this, target);
        }
        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case MULTIATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < MULTI_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new GoldenSlashEffect(target.hb.cX - 60.0F * Settings.scale, target.hb.cY, true), 0.0F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                }
               break;
            }
            case LIFE_STEAL_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(target.hb.cX, target.hb.cY), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case BUFF: {
                AbstractPower power = getPower(EyesOfDeath.POWER_ID);
                if (power != null) {
                    EyesOfDeath eyesOfDeath = (EyesOfDeath)power;
                    eyesOfDeath.wearOff = false;
                }
                for (AbstractMonster mo: AbstractDungeon.getMonsters().monsters) {
                    AbstractPower moPower = mo.getPower(Doom.POWER_ID);
                    if (moPower != null) {
                        Doom doom = (Doom)moPower;
                        doom.wearOff = false;
                        doom.updateDescription();
                    }
                }
                AbstractPower playerPower = AbstractDungeon.player.getPower(Doom.POWER_ID);
                if (playerPower != null) {
                    Doom doom = (Doom)playerPower;
                    doom.wearOff = false;
                    doom.updateDescription();
                }
                addToBot(new ApplyPowerAction(this, this, new AndThenThereWereNone(this)));
                buffed = true;
                addToBot(new RemoveDebuffsAction(this));
                addToBot(new HealAction(this, this, heal));
                break;
            }

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void sisterDefeated() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, SistersPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SistersPlayerPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.0F, 2.0F));
        AbstractDungeon.actionManager.addToBottom(new AnimatedMoveActualAction(this, this.drawX, this.drawY, originalX, originalY));
        AbstractDungeon.onModifyPower();
        buffed = true;
    }

    @Override
    protected void getMove(final int num) {
        if (!buffed) {
            if (currentHealth < maxHealth * BUFF_HP_THRESHOLD) {
                setMoveShortcut(BUFF);
            } else {
                ArrayList<Byte> possibilities = new ArrayList<>();
                if (!this.lastMove(ATTACK) && !this.lastMoveBefore(ATTACK)) {
                    possibilities.add(ATTACK);
                }
                if (!this.lastMove(LIFE_STEAL_ATTACK) && !this.lastMoveBefore(LIFE_STEAL_ATTACK)) {
                    possibilities.add(LIFE_STEAL_ATTACK);
                }
                if (!this.lastMove(MULTIATTACK) && !this.lastMoveBefore(MULTIATTACK)) {
                    possibilities.add(MULTIATTACK);
                }
                setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
            }
        } else {
            setMoveShortcut(ATTACK);
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void applyPowers() {
        if (this.nextMove == -1 || sister.isDead) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            super.applyPowers();
            return;
        }

        AbstractCreature target = sister;
        if (AbstractDungeon.player.hasPower(SistersPlayerPosition.POWER_ID)) {
            if (((SistersPlayerPosition) AbstractDungeon.player.getPower(SistersPlayerPosition.POWER_ID)).isInUnsafeLane()) {
                target = AbstractDungeon.player;
            }
        }

        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if (target == sister) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                if (nextMove == MULTIATTACK) {
                    intentTip.body = TEXT[7] + info.output + TEXT[8] + moves.get(this.nextMove).multiplier + TEXT[9];
                } else {
                    intentTip.body = TEXT[2] + info.output + TEXT[3];
                }
            }
        } else {
            super.applyPowers();
            if(info.base > -1) {
                info.applyPowers(this, target);
                Color color = new Color(0.5F, 0.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                if (nextMove == MULTIATTACK) {
                    intentTip.body = TEXT[4] + info.output + TEXT[5]+ moves.get(this.nextMove).multiplier + TEXT[6];
                } else {
                    intentTip.body = TEXT[0] + info.output + TEXT[1];
                }
            } else {
                Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            }
        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
        applyPowers();
    }

    @Override
    public void die(boolean triggerRelics) {
        ((BetterSpriterAnimation)this.animation).startDying();
        if (sister != null) {
            sister.sisterDefeated();
        }
        this.onBossVictoryLogic();
        super.die(triggerRelics);
    }
}