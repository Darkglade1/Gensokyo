package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.actions.AnimatedMoveActualAction;
import Gensokyo.monsters.act2.Byakuren;
import Gensokyo.powers.act2.Counter;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.RivalPosition;
import Gensokyo.powers.act2.WishfulSoul;
import Gensokyo.powers.act3.AndThenThereWereNone;
import Gensokyo.powers.act3.Doom;
import Gensokyo.powers.act3.EyesOfDeath;
import Gensokyo.vfx.EmptyEffect;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Flandre extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Flandre");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;

    private static final byte ATTACK = 0;
    private static final byte LIFE_STEAL_ATTACK = 1;
    private static final byte MULTIATTACK = 2;
    private static final byte BUFF = 3;

    private static final int ATTACK_DAMAGE = 30;
    private static final int A4_ATTACK_DAMAGE = 33;
    private int attackDamage;

    private static final int MULTI_ATTACK_DAMAGE = 10;
    private static final int A4_MULTI_ATTACK_DAMAGE = 11;
    private static final int MULTI_ATTACK_HITS = 2;
    private int multiAttackDamage;

    private static final int LIFESTEAL_ATTACK_DAMAGE = 18;
    private static final int A4_LIFESTEAL_ATTACK_DAMAGE = 20;
    private int lifestealDamage;

    private static final int DOOM = 1;
    private static final int A19_DOOM = 2;
    private int doom;

    private static final float BUFF_HP_THRESHOLD = 0.50f;
    private boolean buffed = false;

    private static final int HP = 600;
    private static final int A9_HP = 630;

    public float originalX;
    public float originalY;

    private Remilia rival;

    private Map<Byte, EnemyMoveInfo> moves;

    public Flandre() {
        this(0.0f, 0.0f);
    }

    public Flandre(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, -20.0F, 230.0f, 275.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Miko/Spriter/MikoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.doom = A19_DOOM;
        } else {
            this.doom = DOOM;
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
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EyesOfDeath(this, doom)));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Remilia) {
                rival = (Remilia) mo;
            }
        }
        this.originalX = this.drawX;
        this.originalY = this.drawY;
        if (rival != null) {
            rival.originalX = this.drawX;
            rival.originalY = this.drawY;
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
        if (!rival.isDeadOrEscaped()) {
            target = rival;
            if (AbstractDungeon.player.hasPower(RivalPlayerPosition.POWER_ID)) {
                if (((RivalPlayerPosition)AbstractDungeon.player.getPower(RivalPlayerPosition.POWER_ID)).isInUnsafeLane()) {
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
                break;
            }

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void rivalDefeated() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, RivalPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, RivalPlayerPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.0F, 2.0F));
        AbstractDungeon.actionManager.addToBottom(new AnimatedMoveActualAction(this, this.drawX, this.drawY, originalX, originalY));
        AbstractDungeon.onModifyPower();
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
        rival.halfDead = true;
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void applyPowers() {
        if (this.nextMove == -1 || this.intent == IntentEnums.ATTACK_AREA || rival.isDeadOrEscaped()) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            super.applyPowers();
            return;
        }

        AbstractCreature target = rival;
        if (AbstractDungeon.player.hasPower(RivalPlayerPosition.POWER_ID)) {
            if (((RivalPlayerPosition) AbstractDungeon.player.getPower(RivalPlayerPosition.POWER_ID)).isInUnsafeLane()) {
                target = AbstractDungeon.player;
            }
        }

        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if (target == rival) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[7] + info.output + TEXT[8] + moves.get(this.nextMove).multiplier + TEXT[9];
            } else if (this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF) {
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[11];
            }
        } else {
            super.applyPowers();
            if(info.base > -1) {
                info.applyPowers(this, target);
                Color color = new Color(0.5F, 0.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[4] + info.output + TEXT[5]+ moves.get(this.nextMove).multiplier + TEXT[6];
            } else if ((this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF)) {
                Color color = new Color(0.5F, 0.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[10];
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
        if (rival != null) {
            rival.rivalDefeated();
            if (rival.isDeadOrEscaped() || rival.isDying) {
                this.onBossVictoryLogic();
            }
        }
        super.die(triggerRelics);
    }
}