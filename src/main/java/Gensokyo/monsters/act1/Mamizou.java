package Gensokyo.monsters.act1;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.DisguiseAction;
import Gensokyo.powers.act1.DisguisePower;
import Gensokyo.powers.act1.PolymorphPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class Mamizou extends CustomMonster
{
    public static final String ID = "Gensokyo:Mamizou";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte TRANSFORM = 1;
    private static final byte FORM_MOVE_1 = 2;
    private static final byte FORM_MOVE_2 = 3;
    private static final byte POLYMORPH = 4;
    private static final int SENTRY_FORM = 0;
    private static final int LAGA_FORM = 1;
    private static final int NOB_FORM = 2;
    private static final int SENTRY_DAMAGE = 9;
    private static final int A3_SENTRY_DAMAGE = 10;
    private static final int SENTRY_DAZES = 2;
    private static final int A18_SENTRY_DAZES = 3;
    private static final int LAGA_ATTACK = 18;
    private static final int A3_LAGA_ATTACK = 20;
    private static final int DEBUFF_AMOUNT = 1;
    private static final int A18_DEBUFF_AMOUNT = 2;
    private static final int NOB_DEBUFF_ATTACK = 6;
    private static final int A3_NOB_DEBUFF_ATTACK = 8;
    private static final int VULNERABLE = 2;
    private static final int NOB_BIG_ATTACK = 14;
    private static final int A3_NOB_BIG_ATTACK = 16;
    private static final int NOB_STRENGTH = 2;
    private static final int A18_NOB_STRENGTH = 3;
    private static final int METALLCIZE = 3;
    private static final int A18_METALLICIZE = 4;
    private static final int HP_MIN = 90;
    private static final int HP_MAX = 92;
    private static final int A_8_HP_MIN = 92;
    private static final int A_8_HP_MAX = 94;
    private static final int TEMP_HP = 45;
    private static final int A_8_TEMP_HP = 47;
    private static final int SENTRY_TEMP_HP = 25;
    private static final int A_8_SENTRY_TEMP_HP = 27;
    private int sentryDamage;
    private int sentryDazes;
    private int lagaAttack;
    private int lagaDebuff;
    private int nobDebuff;
    private int nobAttack;
    private int nobStrength;
    private int metallicize;
    private int form;
    private int tempHP;
    private int sentryTempHP;
    private boolean polymorphing = false;
    private boolean firstReveal = true;

    public AbstractMonster currentDisguise = null;
    private Hitbox originalIntentHb;

    public Mamizou() {
        this(0.0f, 0.0f);
    }

    public Mamizou(final float x, final float y) {
        super(Mamizou.NAME, ID, HP_MAX, -5.0F, 0.0F, 260.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Mamizou/Spriter/MamizouAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.nobStrength = A18_NOB_STRENGTH;
            this.sentryDazes = A18_SENTRY_DAZES;
            this.lagaDebuff = A18_DEBUFF_AMOUNT;
            this.metallicize = A18_METALLICIZE;
        } else {
            this.nobStrength = NOB_STRENGTH;
            this.sentryDazes = SENTRY_DAZES;
            this.lagaDebuff = DEBUFF_AMOUNT;
            this.metallicize = METALLCIZE;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_8_HP_MIN, A_8_HP_MAX);
            this.tempHP = A_8_TEMP_HP;
            this.sentryTempHP = A_8_SENTRY_TEMP_HP;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.tempHP = TEMP_HP;
            this.sentryTempHP = SENTRY_TEMP_HP;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.sentryDamage = A3_SENTRY_DAMAGE;
            this.lagaAttack = A3_LAGA_ATTACK;
            this.nobDebuff = A3_NOB_DEBUFF_ATTACK;
            this.nobAttack = A3_NOB_BIG_ATTACK;
        } else {
            this.sentryDamage = SENTRY_DAMAGE;
            this.lagaAttack = LAGA_ATTACK;
            this.nobDebuff = NOB_DEBUFF_ATTACK;
            this.nobAttack = NOB_BIG_ATTACK;
        }
        this.damage.add(new DamageInfo(this, this.sentryDamage));
        this.damage.add(new DamageInfo(this, this.lagaAttack));
        this.damage.add(new DamageInfo(this, this.nobDebuff));
        this.damage.add(new DamageInfo(this, this.nobAttack));

        Player.PlayerListener listener = new MamizouListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("Futatsuiwa from Gensokyo");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DisguisePower(this, this)));
        originalIntentHb = this.intentHb;
        form = SENTRY_FORM;
        switchDisguise();
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, sentryTempHP));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TRANSFORM: {
                //runAnim("Spell");
                if (form == SENTRY_FORM) {
                    form = LAGA_FORM;
                } else if (form == LAGA_FORM) {
                    form = NOB_FORM;
                } else if (form == NOB_FORM) {
                    form = SENTRY_FORM;
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                int maxHPLoss = this.currentHealth - 1;
                int hpLoss = Math.min(maxHPLoss, tempHP);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this, new DamageInfo(this, hpLoss, DamageInfo.DamageType.HP_LOSS)));
                AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, hpLoss));
                AbstractDungeon.actionManager.addToBottom(new DisguiseAction(this));
                break;
            }
            case FORM_MOVE_1: {
                if (form == SENTRY_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
                    if (!Settings.FAST_MODE) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));
                        AbstractDungeon.actionManager.addToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.2F));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1F));
                        AbstractDungeon.actionManager.addToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.15F));
                    }
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.sentryDazes));
                } else if (form == LAGA_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(currentDisguise, "ATTACK"));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                } else if (form == NOB_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(currentDisguise));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, VULNERABLE, true), VULNERABLE));
                }
                break;
            }
            case FORM_MOVE_2: {
                if (form == SENTRY_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(currentDisguise, "ATTACK"));
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                    if (Settings.FAST_MODE) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE, Settings.FAST_MODE));
                    //AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.sentryAttackDazes));
                } else if (form == LAGA_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(currentDisguise, "DEBUFF"));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -this.lagaDebuff), -this.lagaDebuff));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.lagaDebuff), -this.lagaDebuff));
                } else if (form == NOB_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(currentDisguise));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            }
            case POLYMORPH: {
                runAnim("SpellC");
                if (form == SENTRY_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.sentryDazes));
                } else if (form == LAGA_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, metallicize), metallicize));
                } else if (form == NOB_FORM) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, nobStrength), nobStrength));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new PolymorphPower(AbstractDungeon.player, 1, true), 1));
                polymorphing = false;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (polymorphing) {
            this.setMove(MOVES[0], POLYMORPH, Intent.STRONG_DEBUFF);
        } else if(currentDisguise == null) {
            this.setMove(TRANSFORM, Intent.UNKNOWN);
        } else {
            if (form == SENTRY_FORM) {
                if (lastMove(FORM_MOVE_1)) {
                    this.setMove(FORM_MOVE_2, Intent.ATTACK, this.damage.get(0).base);
                } else {
                    this.setMove(FORM_MOVE_1, Intent.DEBUFF);
                }
            } else if (form == LAGA_FORM) {
                if (lastMove(TRANSFORM)) {
                    this.setMove(FORM_MOVE_1, Intent.ATTACK, this.damage.get(1).base);
                } else if (!lastMove(FORM_MOVE_2) && !lastMoveBefore(FORM_MOVE_2)) {
                    this.setMove(FORM_MOVE_2, Intent.STRONG_DEBUFF);
                } else {
                    this.setMove(FORM_MOVE_1, Intent.ATTACK, this.damage.get(1).base);
                }
            } else if (form == NOB_FORM) {
                this.setMove(FORM_MOVE_2, Intent.ATTACK, this.damage.get(3).base);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Mamizou");
        NAME = Mamizou.monsterStrings.NAME;
        MOVES = Mamizou.monsterStrings.MOVES;
        DIALOG = Mamizou.monsterStrings.DIALOG;
    }

    public void switchDisguise() {
        switch(form) {
            case NOB_FORM:
                GremlinNob nob = new GremlinNob(0, 0);
                switching(nob);
                break;
            case LAGA_FORM:
                Lagavulin laga = new Lagavulin(false);
                switching(laga);
                break;
            case SENTRY_FORM:
                Sentry sentry = new Sentry(0, 0);
                switching(sentry);
                break;
        }
    }

    private void switching (AbstractMonster mo) {
        currentDisguise = mo;
        currentDisguise.drawX = this.drawX;
        currentDisguise.drawY = this.drawY;
        if (this.hasPower(DisguisePower.POWER_ID)) {
            this.getPower(DisguisePower.POWER_ID).updateDescription();
        }
        if (mo instanceof GremlinNob) {
            currentDisguise.drawX += 80.0F * Settings.scale; //Centers Nob correctly
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngerPower(this, nobStrength), nobStrength));
        }
        this.intentHb = mo.intentHb; //Moves the intent icon to more appropriate spot
    }

    public void removeDisguise() {
        if (currentDisguise != null) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
            currentDisguise = null;
            this.intentHb = originalIntentHb;
            polymorphing = true;
            this.setMove(MOVES[0], POLYMORPH, Intent.STRONG_DEBUFF);
            this.createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, MOVES[0], POLYMORPH, Intent.STRONG_DEBUFF));
            if (firstReveal) {
                firstReveal = false;
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(currentDisguise != null) {
            this.renderHealth(sb);
            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != Intent.NONE && !Settings.hideCombatElements) {
                this.renderIntentVfxBehind(sb);
                this.renderIntent(sb);
                this.renderIntentVfxAfter(sb);
                this.renderDamageRange(sb);
            }
            currentDisguise.render(sb);
        } else {
            super.render(sb);
        }
    }

    @Override
    public void refreshIntentHbLocation() {
        //Necessary check to allow us to position intent of disguises properly
        if (currentDisguise == null) {
            super.refreshIntentHbLocation();
        }
    }

    @Override
    public void update() {
        if(currentDisguise != null) {
            currentDisguise.update();
        }
        super.update();
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        currentDisguise = null;
        super.die(triggerRelics);
    }

    //Runs a specific animation
    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
    }

    //Resets character back to idle animation
    public void resetAnimation() {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Idle");
    }

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class MamizouListener implements Player.PlayerListener {

        private Mamizou character;

        public MamizouListener(Mamizou character) {
            this.character = character;
        }

        public void animationFinished(Animation animation){
            if (animation.name.equals("Defeat")) {
                character.stopAnimation();
            } else if (!animation.name.equals("Idle")) {
                character.resetAnimation();
            }
        }

        //UNUSED
        public void animationChanged(Animation var1, Animation var2){

        }

        //UNUSED
        public void preProcess(Player var1){

        }

        //UNUSED
        public void postProcess(Player var1){

        }

        //UNUSED
        public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){

        }
    }
}