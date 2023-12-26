package Gensokyo.monsters.act1;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.InvertPowersAction;
import Gensokyo.powers.act1.FortitudePower;
import Gensokyo.powers.act1.UnstableBoundariesPower;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.vfx.EmptyEffect;
import Gensokyo.vfx.YukariTrainEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Texture;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;

public class Yukari extends CustomMonster
{
    public static final String ID = "Gensokyo:Yukari";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final Texture TRAIN_INTENT_TEXTURE = ImageMaster.loadImage("GensokyoResources/images/monsters/Yukari/attack_intent_train.png");
    private boolean firstMove = true;
    private static final byte OPENING = 1;
    private static final byte STRENGTH_DRAIN = 2;
    private static final byte MEGA_DEBUFF = 3;
    private static final byte ATTACK = 4;
    private static final byte LAST_WORD = 5;
    private static final byte TRAIN = 6;
    private static final int NORMAL_ATTACK_DAMAGE = 10;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 11;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 14;
    private static final int A4_DEBUFF_ATTACK_DAMAGE = 16;
    private static final int TRAIN_ATTACK_DAMAGE = 7;
    private static final int A4_TRAIN_ATTACK_DAMAGE = 8;
    private static final int TRAIN_ATTACK_HITS = 3;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int STRENGTH_DRAIN_AMOUNT = 2;
    private static final int OPENING_AMOUNT = 1;
    private static final int WOUND_AMOUNT = 1;
    private static final int A19_WOUND_AMOUNT = 2;
    private static final int BLOCK = 9;
    private static final int A9_BLOCK = 10;
    private int normalDamage;
    private int debuffDamage;
    private int trainDamage;
    private int debuffAmount;
    private int strengthDrain;
    private int block;
    private int wound;
    private boolean useTrain = false;
    private boolean useTrainTexture = false;
    private boolean isEnraged = false;
    private static final int HP = 220;
    private static final int A9_HP = 230;

    public Yukari() {
        this(0.0f, 0.0f);
    }

    public Yukari(boolean isEnraged) {
        this();
        this.isEnraged = isEnraged;
    }

    public Yukari(final float x, final float y) {
        super(Yukari.NAME, ID, HP, -5.0F, 0, 280.0f, 285.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yukari/Spriter/YukariAnimations.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.debuffAmount = DEBUFF_AMOUNT;
        this.strengthDrain = STRENGTH_DRAIN_AMOUNT;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.wound = A19_WOUND_AMOUNT;
        } else {
            this.wound = WOUND_AMOUNT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A4_DEBUFF_ATTACK_DAMAGE;
            this.trainDamage = A4_TRAIN_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.trainDamage = TRAIN_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.trainDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));

        Player.PlayerListener listener = new YukariListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("Necrofantasia");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstableBoundariesPower(this)));
        if (this.isEnraged) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngerPower(this, 2)));
        }
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case OPENING: {
                runAnim("Spell");
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Yukari.DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, OPENING_AMOUNT), OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, OPENING_AMOUNT, true), OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FortitudePower(this, OPENING_AMOUNT, true), OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -OPENING_AMOUNT), -OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, OPENING_AMOUNT, true), OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, OPENING_AMOUNT, true), OPENING_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, OPENING_AMOUNT, true), OPENING_AMOUNT));
                break;
            }
            case STRENGTH_DRAIN: {
                runAnim("Fan");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case MEGA_DEBUFF: {
                runAnim("GapHand");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), this.wound));
                break;
            }
            case ATTACK: {
                runAnim("Parasol");
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, this.debuffAmount, true), this.debuffAmount));
                break;
            }
            case LAST_WORD: {
                runAnim("Spell");
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(this, true));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                break;
            }
            case TRAIN: {
                runAnim("Train");
                for (int i = 0; i < TRAIN_ATTACK_HITS; i++) {
                    if (i == 0) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("Gensokyo:Train"));
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new YukariTrainEffect(), 0.6F));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.6F));
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(this, true));
                useTrainTexture = false;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(Yukari.MOVES[0], OPENING, Intent.UNKNOWN);
            this.firstMove = false;
        }  else if (this.currentHealth < this.maxHealth / 2 && !this.useTrain) {
            this.useTrain = true;
            this.setMove(Yukari.MOVES[5], LAST_WORD, Intent.DEFEND_BUFF);
        } else if (this.lastMove(LAST_WORD)) {
            useTrainTexture = true;
            this.setMove(Yukari.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
        } else if (this.lastMove(OPENING)) {
            this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(2)).base);
        } else {
            if (this.useTrain && !this.lastMove(TRAIN) && !this.lastMoveBefore(TRAIN)) { //use train every 3 turns
                useTrainTexture = true;
                this.setMove(Yukari.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
            } else if (num < 35) {
                if (!this.lastMove(STRENGTH_DRAIN)) {
                    this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(2)).base);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    } else {
                        this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    }
                }
            } else if (num < 65) {
                if (!this.lastMove(MEGA_DEBUFF)) {
                    this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    } else {
                        this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    }
                }
            } else {
                if (!this.lastMove(ATTACK)) {
                    this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    } else {
                        this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    }
                }
            }
        }
    }

    @Override
    public Texture getAttackIntent() {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent();
    }
    @Override
    public Texture getAttackIntent(int dmg) {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent(dmg);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yukari");
        NAME = Yukari.monsterStrings.NAME;
        MOVES = Yukari.monsterStrings.MOVES;
        DIALOG = Yukari.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        this.onBossVictoryLogic();
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

    public class YukariListener implements Player.PlayerListener {

        private Yukari character;

        public YukariListener(Yukari character) {
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