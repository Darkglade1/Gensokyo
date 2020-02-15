package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.RezAction;
import Gensokyo.actions.SetFlipAction;
import Gensokyo.cards.ImpossibleRequests.ImpossibleRequest;
import Gensokyo.powers.act2.BetterDrawReductionPower;
import Gensokyo.powers.act2.HouraiImmortal;
import Gensokyo.powers.act2.LunaticPrincess;
import actlikeit.dungeons.CustomDungeon;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Kaguya extends CustomMonster
{
    public static final String ID = "Gensokyo:Kaguya";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte BRILLIANT_DRAGON_BULLET = 0;
    private static final byte BUDDHIST_DIAMOND_1 = 1;
    private static final byte BUDDHIST_DIAMOND_2 = 2;
    private static final byte BUDDHIST_DIAMOND_3 = 3;
    private static final byte BUDDHIST_DIAMOND_4 = 4;
    private static final byte BUDDHIST_DIAMOND_5 = 5;

    private static final int BRILLIANT_DRAGON_BULLET_DAMAGE = 9;
    private static final int A4_BRILLIANT_DRAGON_BULLET_DAMAGE = 10;
    private static final int BRILLIANT_DRAGON_BULLET_HITS = 2;
    private int brilliantDragonBulletDamage;

    private static final int BUDDHIST_DIAMOND_DAMAGE_1 = 11;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE_1 = 12;
    private int buddhistDiamondDamage1;

    private static final int BUDDHIST_DIAMOND_DAMAGE_2 = 9;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE_2 = 10;
    private int buddhistDiamondDamage2;

    private static final int BUDDHIST_DIAMOND_DAMAGE_3 = 16;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE_3 = 17;
    private int buddhistDiamondDamage3;

    private static final int BUDDHIST_DIAMOND_DAMAGE_4 = 15;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE_4 = 16;
    private int buddhistDiamondDamage4;

    private static final int BUDDHIST_DIAMOND_DAMAGE_5 = 14;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE_5 = 15;
    private int buddhistDiamondDamage5;

    public static final int PLAYER_STRENGTH_GAIN = 1;
    private static final int STRENGTH_GAIN = 3;
    //private static final int A19_STRENGTH_GAIN = 3;
    private int strengthGain;

    private static final int HP = 60;
    private static final int A9_HP = 63;
    private int originalMaxHP;

    private static final int DEATH_THRESHOLD = 6;
    private int deathCounter = DEATH_THRESHOLD;

    private static final int DEBUFF_AMT = 1;

    private Map<Byte, EnemyMoveInfo> moves;
    private ImpossibleRequest request;
    private int damage;

    public Kaguya() {
        this(0.0f, 0.0f);
    }

    public Kaguya(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kaguya/Spriter/KaguyaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        this.strengthGain = STRENGTH_GAIN;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }
        this.originalMaxHP = maxHealth;

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.brilliantDragonBulletDamage = A4_BRILLIANT_DRAGON_BULLET_DAMAGE;
            this.buddhistDiamondDamage1 = A4_BUDDHIST_DIAMOND_DAMAGE_1;
            this.buddhistDiamondDamage2 = A4_BUDDHIST_DIAMOND_DAMAGE_2;
            this.buddhistDiamondDamage3 = A4_BUDDHIST_DIAMOND_DAMAGE_3;
            this.buddhistDiamondDamage4 = A4_BUDDHIST_DIAMOND_DAMAGE_4;
            this.buddhistDiamondDamage5 = A4_BUDDHIST_DIAMOND_DAMAGE_5;
        } else {
            this.brilliantDragonBulletDamage = BRILLIANT_DRAGON_BULLET_DAMAGE;
            this.buddhistDiamondDamage1 = BUDDHIST_DIAMOND_DAMAGE_1;
            this.buddhistDiamondDamage2 = BUDDHIST_DIAMOND_DAMAGE_2;
            this.buddhistDiamondDamage3 = BUDDHIST_DIAMOND_DAMAGE_3;
            this.buddhistDiamondDamage4 = BUDDHIST_DIAMOND_DAMAGE_4;
            this.buddhistDiamondDamage5 = BUDDHIST_DIAMOND_DAMAGE_5;
        }

        this.moves = new HashMap<>();
        this.moves.put(BRILLIANT_DRAGON_BULLET, new EnemyMoveInfo(BRILLIANT_DRAGON_BULLET, Intent.ATTACK, this.brilliantDragonBulletDamage, BRILLIANT_DRAGON_BULLET_HITS, true));
        this.moves.put(BUDDHIST_DIAMOND_1, new EnemyMoveInfo(BUDDHIST_DIAMOND_1, Intent.ATTACK_DEBUFF, this.buddhistDiamondDamage1, 0, false));
        this.moves.put(BUDDHIST_DIAMOND_2, new EnemyMoveInfo(BUDDHIST_DIAMOND_1, Intent.ATTACK_DEBUFF, this.buddhistDiamondDamage2, 0, false));
        this.moves.put(BUDDHIST_DIAMOND_3, new EnemyMoveInfo(BUDDHIST_DIAMOND_1, Intent.ATTACK_DEBUFF, this.buddhistDiamondDamage3, 0, false));
        this.moves.put(BUDDHIST_DIAMOND_4, new EnemyMoveInfo(BUDDHIST_DIAMOND_1, Intent.ATTACK_DEBUFF, this.buddhistDiamondDamage4, 0, false));
        this.moves.put(BUDDHIST_DIAMOND_5, new EnemyMoveInfo(BUDDHIST_DIAMOND_1, Intent.ATTACK_DEBUFF, this.buddhistDiamondDamage5, 0, false));

    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        CustomDungeon.playTempMusicInstantly("LunaticPrincess");
        request = new ImpossibleRequest();
        if (AbstractDungeon.ascensionLevel >= 19) {
            request.upgrade();
        }
        request.transform();
        this.addToBot(new ApplyPowerAction(this, this, new HouraiImmortal(this, DEATH_THRESHOLD)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new LunaticPrincess(AbstractDungeon.player, strengthGain, this, request)));
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(request));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, this.damage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        switch (this.nextMove) {
            case BRILLIANT_DRAGON_BULLET: {
                this.useFastAttackAnimation();
                for (int i = 0; i < BRILLIANT_DRAGON_BULLET_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            }
            case BUDDHIST_DIAMOND_1: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (request.requestCounter == ImpossibleRequest.BUDDHA_BOWL) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                } else if (request.requestCounter == ImpossibleRequest.BULLET_BRANCH) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), DEBUFF_AMT, true, true));
                } else if (request.requestCounter == ImpossibleRequest.FIRE_RAT) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new BetterDrawReductionPower(AbstractDungeon.player, DEBUFF_AMT), DEBUFF_AMT));
                } else if (request.requestCounter == ImpossibleRequest.JEWEL_FROM_DRAGON) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(BRILLIANT_DRAGON_BULLET)) {
            if (request.requestCounter == ImpossibleRequest.BUDDHA_BOWL) {
                this.setMoveShortcut(BUDDHIST_DIAMOND_1);
            } else if (request.requestCounter == ImpossibleRequest.BULLET_BRANCH) {
                this.setMoveShortcut(BUDDHIST_DIAMOND_2);
            } else if (request.requestCounter == ImpossibleRequest.FIRE_RAT) {
                this.setMoveShortcut(BUDDHIST_DIAMOND_3);
            } else if (request.requestCounter == ImpossibleRequest.JEWEL_FROM_DRAGON) {
                this.setMoveShortcut(BUDDHIST_DIAMOND_4);
            } else {
                this.setMoveShortcut(BUDDHIST_DIAMOND_5);
            }
        } else {
            this.setMoveShortcut(BRILLIANT_DRAGON_BULLET);
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.damage = info.baseDamage;
        this.setMove(MOVES[info.nextMove], info.nextMove, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            Iterator var2 = this.powers.iterator();

            while (var2.hasNext()) {
                AbstractPower p = (AbstractPower) var2.next();
                p.onDeath();
            }

            var2 = AbstractDungeon.player.relics.iterator();

            while (var2.hasNext()) {
                AbstractRelic r = (AbstractRelic) var2.next();
                r.onMonsterDeath(this);
            }

            ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
            for (AbstractPower power : this.powers) {
                if (!(power instanceof HouraiImmortal) && !(power instanceof LunaticPrincess) && !(power instanceof StrengthPower) && !(power instanceof GainStrengthPower)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }

            AbstractDungeon.actionManager.addToBottom(new RezAction(this));
            AbstractDungeon.onModifyPower();

            if (request.completed) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, PLAYER_STRENGTH_GAIN), PLAYER_STRENGTH_GAIN));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
            }
            request.completed = false;
            request.requestCounter++;
            if (AbstractDungeon.player.hasPower(LunaticPrincess.POWER_ID)) {
                LunaticPrincess power = (LunaticPrincess)AbstractDungeon.player.getPower(LunaticPrincess.POWER_ID);
                power.counter = 0;
            }
            request.transform();

            deathCounter--;
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, HouraiImmortal.POWER_ID, 1));
            if (this.deathCounter <= 0) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new SetFlipAction(this));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
            }
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
        if (this.maxHealth <= 0) {
            this.maxHealth = originalMaxHP;
            AbstractDungeon.actionManager.addToBottom(new InstantKillAction(this));
        }
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Kaguya");
        NAME = Kaguya.monsterStrings.NAME;
        MOVES = Kaguya.monsterStrings.MOVES;
        DIALOG = Kaguya.monsterStrings.DIALOG;
    }
}