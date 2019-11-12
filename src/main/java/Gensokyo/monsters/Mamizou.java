package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.DisguisePower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Mamizou extends CustomMonster
{
    public static final String ID = "Gensokyo:Mamizou";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte BUFF = 1;
    private static final byte ATTACK = 2;
    private static final byte DEBUFF_ATTACK = 3;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 7;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 10;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 11;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int BLOCK = 7;
    private static final int A8_BLOCK = 9;
    private static final int STRENGTH = 3;
    private static final int A18_STRENGTH = 4;
    private static final int HP_MIN = 61;
    private static final int HP_MAX = 62;
    private static final int A_8_HP_MIN = 63;
    private static final int A_8_HP_MAX = 64;
    private static final int TEMP_HP = 30;
    private static final int A_8_TEMP_HP = 32;
    private static final float HB_X = -5.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 260.0f;
    private static final float HB_H = 255.0f;
    private int normalDamage;
    private int debuffDamage;
    private int block;
    private int strength;
    private int tempHP;

    public Mamizou() {
        this(0.0f, 0.0f);
    }

    public Mamizou(final float x, final float y) {
        super(Mamizou.NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Mamizou/Spriter/MamizouAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_8_HP_MIN, A_8_HP_MAX);
            this.block = A8_BLOCK;
            this.tempHP = A_8_TEMP_HP;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
            this.tempHP = TEMP_HP;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A3_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));

        Player.PlayerListener listener = new MamizouListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Gensokyo/Wind God Girl.mp3");
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new DisguisePower(this, HB_X, HB_Y, HB_W, HB_H)));
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, tempHP));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
//            case BUFF: {
//                //runAnim("Spell");
//                if (this.firstMove) {
//                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
//                    this.firstMove = false;
//                }
//                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IllusionaryDominance(this, STRENGTH - 1, this), STRENGTH - 1));
//                break;
//            }
//            case DEBUFF_ATTACK: {
//                //runAnim("Fan");
//                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
//                break;
//            }
            case ATTACK: {
                //runAnim("Parasol");
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        this.setMove(ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
//        if (this.firstMove) {
//            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
//        } else if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
//            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
//        } else {
//            if (this.lastMove(BUFF)) {
//                this.setMove(Mamizou.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
//            } else if (this.lastMove(ATTACK)){
//                if (debuffTriggered) {
//                    this.setMove(Mamizou.MOVES[1], DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
//                } else {
//                    this.setMove(Mamizou.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
//                }
//            } else {
//                this.setMove(Mamizou.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
//            }
//        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Mamizou");
        NAME = Mamizou.monsterStrings.NAME;
        MOVES = Mamizou.monsterStrings.MOVES;
        DIALOG = Mamizou.monsterStrings.DIALOG;
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.hasPower(DisguisePower.POWER_ID) && DisguisePower.currentDisguise != null) {
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
            DisguisePower.currentDisguise.render(sb);
        } else {
            super.render(sb);
        }
    }

    @Override
    public void update() {
        if(this.hasPower(DisguisePower.POWER_ID) && DisguisePower.currentDisguise != null) {
            DisguisePower.currentDisguise.update();
        }
        super.update();
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
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