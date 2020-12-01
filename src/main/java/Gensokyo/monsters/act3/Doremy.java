package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.DreamEater;
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
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Map;

public class Doremy extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Doremy");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private boolean nightmareInflictsFrail = true;
    private boolean isNightmare = false;

    public static final byte DREAM_ATTACK = 0;
    public static final byte DREAM_DEBUFF = 1;
    public static final byte DREAM_BLOCK = 2;

    public static final byte NIGHTMARE_ATTACK = 3;
    public static final byte NIGHTMARE_DEBUFF = 4;
    public static final byte NIGHTMARE_BUFF = 5;

    private static final int DREAM_ATTACK_DAMAGE = 9;
    private static final int A3_DREAM_ATTACK_DAMAGE = 10;

    private static final int DREAM_DEBUFF_AMT = 2;

    private static final int DREAM_BLOCK_AMT = 10;
    private static final int A8_DREAM_BLOCK_AMT = 12;

    private static final int NIGHTMARE_ATTACK_DAMAGE = 6;
    private static final int A3_NIGHTMARE_ATTACK_DAMAGE = 7;
    private static final int HITS = 6;

    private static final int NIGHTMARE_DEBUFF_AMT = 3;

    private static final int NIGHTMARE_BUFF_AMT = 2;

    private static final int DREAM_DURATION = 3;

    private static final int HP = 350;
    private static final int A8_HP = 365;
    private int dreamAttackDamage;
    private int dreamBlock;
    private int nightmareAttackDamage;

    private Map<Byte, EnemyMoveInfo> moves;

    public Doremy() {
        this(0.0f, 0.0f);
    }

    public Doremy(final float x, final float y) {
        super(Doremy.NAME, ID, HP, -5.0F, 0, 240.0f, 260.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Doremy/Spriter/DoremyAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
            dreamBlock = A8_DREAM_BLOCK_AMT;
        } else {
            this.setHp(HP);
            dreamBlock = DREAM_BLOCK_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            dreamAttackDamage = A3_DREAM_ATTACK_DAMAGE;
            nightmareAttackDamage = A3_NIGHTMARE_ATTACK_DAMAGE;
        } else {
            dreamAttackDamage = DREAM_ATTACK_DAMAGE;
            nightmareAttackDamage = NIGHTMARE_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(DREAM_ATTACK, new EnemyMoveInfo(DREAM_ATTACK, Intent.ATTACK, dreamAttackDamage, 0, false));
        this.moves.put(DREAM_BLOCK, new EnemyMoveInfo(DREAM_BLOCK, Intent.DEFEND, -1, 0, false));
        this.moves.put(DREAM_DEBUFF, new EnemyMoveInfo(DREAM_DEBUFF, Intent.DEBUFF, -1, 0, false));

        this.moves.put(NIGHTMARE_ATTACK, new EnemyMoveInfo(NIGHTMARE_ATTACK, Intent.ATTACK, nightmareAttackDamage, HITS, true));
        this.moves.put(NIGHTMARE_DEBUFF, new EnemyMoveInfo(NIGHTMARE_DEBUFF, Intent.DEBUFF, -1, 0, false));
        this.moves.put(NIGHTMARE_BUFF, new EnemyMoveInfo(NIGHTMARE_BUFF, Intent.BUFF, -1, 0, false));

        Player.PlayerListener listener = new DoremyListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Bhavagra");
        boolean stronger = AbstractDungeon.ascensionLevel >= 18;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DreamEater(this, DREAM_DURATION, stronger, this)));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case DREAM_ATTACK: {
                runAnim("AttackForward");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
            case DREAM_BLOCK: {
                runAnim("SpellCall");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, dreamBlock));
                break;
            }
            case DREAM_DEBUFF: {
                runAnim("SpellCall");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DREAM_DEBUFF_AMT, true), DREAM_DEBUFF_AMT));
                break;
            }
            case NIGHTMARE_ATTACK: {
                useFastAttackAnimation();
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            }
            case NIGHTMARE_DEBUFF: {
                if (nightmareInflictsFrail) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, NIGHTMARE_DEBUFF_AMT, true), NIGHTMARE_DEBUFF_AMT));
                    nightmareInflictsFrail = false;
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, NIGHTMARE_DEBUFF_AMT, true), NIGHTMARE_DEBUFF_AMT));
                    nightmareInflictsFrail = true;
                }
                break;
            }
            case NIGHTMARE_BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, NIGHTMARE_BUFF_AMT), NIGHTMARE_BUFF_AMT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!isNightmare) {
            if (lastMove(DREAM_DEBUFF)) {
                setMoveShortcut(DREAM_BLOCK);
            } else if (lastMove(DREAM_BLOCK)) {
                setMoveShortcut(DREAM_ATTACK);
            } else {
                setMoveShortcut(DREAM_DEBUFF);
            }
        } else {
            if (lastMove(NIGHTMARE_ATTACK)) {
                AbstractPower str = getPower(StrengthPower.POWER_ID);
                if (str != null && str.amount < 0) {
                    setMoveShortcut(NIGHTMARE_BUFF);
                } else {
                    setMoveShortcut(NIGHTMARE_DEBUFF);
                }
            } else {
                setMoveShortcut(NIGHTMARE_ATTACK);
            }
        }
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    
    public void transitionToNightmare() {
        isNightmare = true;
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
        this.name = DIALOG[2];
        this.setMoveShortcut(NIGHTMARE_ATTACK);
        runAnim("SpellC");
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
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

    //Resets character back to idle animation
    public void resetNightmareAnimation() {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("NightmareIdle");
    }

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class DoremyListener implements Player.PlayerListener {

        private Doremy character;

        public DoremyListener(Doremy character) {
            this.character = character;
        }

        public void animationFinished(Animation animation){
            if (animation.name.equals("Defeat")) {
                character.stopAnimation();
            } else {
                if (isNightmare) {
                    if (!animation.name.equals("NightmareIdle")) {
                        character.resetNightmareAnimation();
                    }
                } else {
                    if (!animation.name.equals("Idle")) {
                        character.resetAnimation();
                    }
                }

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