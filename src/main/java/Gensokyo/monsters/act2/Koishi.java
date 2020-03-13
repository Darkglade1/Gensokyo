package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.cards.EmbersOfLove;
import Gensokyo.powers.act2.GrowingPain;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.MayhemPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Koishi extends CustomMonster
{
    public static final String ID = "Gensokyo:Koishi";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private boolean secondMove = true;
    private static final byte DEBUFF = 0;
    private static final byte ATTACK = 1;
    private static final byte STATUS = 2;

    private static final int NORMAL_ATTACK_DAMAGE = 4;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 5;
    private static final int HITS = 4;

    private static final int STATUS_AMT = 3;
    private static final int A18_STATUS_AMT = 4;
    private static final int MAYHAM = 1;

    private static final int BUFF = 2;
    private static final int A18_BUFF = 2;

    private static final int HP_MIN = 180;
    private static final int HP_MAX = 182;
    private static final int A8_HP_MIN = 188;
    private static final int A8_HP_MAX = 190;
    private int normalDamage;
    private int buff;
    private int status;

    private Map<Byte, EnemyMoveInfo> moves;

    public Koishi() {
        this(0.0f, 0.0f);
    }

    public Koishi(final float x, final float y) {
        super(Koishi.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Koishi/Spriter/KoishiAnimation.scml");
        this.animation.setFlip(true, false);
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.buff = A18_BUFF;
            this.status = A18_STATUS_AMT;
        } else {
            this.buff = BUFF;
            this.status = STATUS_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.UNKNOWN, -1, 0, false));
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, HITS, true));
        this.moves.put(STATUS, new EnemyMoveInfo(STATUS, Intent.DEBUFF, -1, 0, false));

        Player.PlayerListener listener = new DoremyListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("Hartmann");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GrowingPain(this, buff), buff));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case DEBUFF: {
                runAnim("magicAttackForward");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new MayhemPower(AbstractDungeon.player, MAYHAM), MAYHAM));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new EmbersOfLove(), status));
                firstMove = false;
                break;
            }
            case ATTACK: {
                runAnim("occultAttack");
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                secondMove = false;
                break;
            }
            case STATUS: {
                runAnim("spellA");
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new EmbersOfLove(), status));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(DEBUFF);
        } else if (secondMove) {
            this.setMoveShortcut(ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK)) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(STATUS)) {
                possibilities.add(STATUS);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Koishi");
        NAME = Koishi.monsterStrings.NAME;
        MOVES = Koishi.monsterStrings.MOVES;
        DIALOG = Koishi.monsterStrings.DIALOG;
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

    public class DoremyListener implements Player.PlayerListener {

        private Koishi character;

        public DoremyListener(Koishi character) {
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