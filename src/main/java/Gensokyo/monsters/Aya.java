package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.Evasive;
import Gensokyo.powers.IllusionaryDominance;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Aya extends CustomMonster
{
    public static final String ID = "Gensokyo:Aya";
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
    private static final int DEBUFF_ATTACK_DAMAGE = 8;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 9;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int BLOCK = 9;
    private static final int A8_BLOCK = 10;
    private static final int STRENGTH = 3;
    private static final int A18_STRENGTH = 4;
    private static final int HP_MIN = 70;
    private static final int HP_MAX = 72;
    private static final int A_2_HP_MIN = 72;
    private static final int A_2_HP_MAX = 74;
    private int normalDamage;
    private int debuffDamage;
    private int block;
    private int strength;
    public boolean debuffTriggered;

    public Aya() {
        this(0.0f, 0.0f);
    }

    public Aya(final float x, final float y) {
        super(Aya.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Aya/Spriter/AyaAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
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

        Player.PlayerListener listener = new AyaListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("Wind God Girl");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Evasive(this, 1)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BUFF: {
                runAnim("Spellcard");
                if (this.firstMove) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                    this.firstMove = false;
                }
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IllusionaryDominance(this, strength - 1, this), strength - 1));
                break;
            }
            case DEBUFF_ATTACK: {
                runAnim("AttackDash");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
            case ATTACK: {
                runAnim("AttackFan");
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
        if (this.firstMove) {
            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
        } else if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
        } else {
            if (this.lastMove(BUFF)) {
                this.setMove(Aya.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            } else if (this.lastMove(ATTACK)){
                if (debuffTriggered) {
                    this.setMove(Aya.MOVES[1], DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                } else {
                    this.setMove(Aya.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                }
            } else {
                this.setMove(Aya.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Aya");
        NAME = Aya.monsterStrings.NAME;
        MOVES = Aya.monsterStrings.MOVES;
        DIALOG = Aya.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
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

    public class AyaListener implements Player.PlayerListener {

        private Aya character;

        public AyaListener(Aya character) {
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