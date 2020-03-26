package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act2.Reckless;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.HashMap;
import java.util.Map;

public class Swordslinger extends CustomMonster
{
    public static final String ID = "Gensokyo:Swordslinger";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte DOUBLE_ATTACK = 2;
    private static final byte BUFF = 3;
    private static final int ATTACK_DAMAGE = 30;
    private static final int A2_ATTACK_DAMAGE = 33;
    private static final int DOUBLE_ATTACK_DAMAGE = 20;
    private static final int A2_DOUBLE_ATTACK_DAMAGE = 22;
    private static final int HITS = 2;
    private static final int BUFF_AMT = 2;
    private static final int STR = 2;
    private static final int A17_STR = 3;
    private static final int HP_MIN = 100;
    private static final int HP_MAX = 105;
    private static final int A7_HP_MIN = 105;
    private static final int A7_HP_MAX = 109;
    private int attackDamage;
    private int doubleAttackDamage;
    private int strength;

    private Map<Byte, EnemyMoveInfo> moves;

    public Swordslinger() {
        this(0.0f, 0.0f);
    }

    public Swordslinger(final float x, final float y) {
        super(Swordslinger.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 280.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Swordslinger/Spriter/SwordslingerAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.strength = A17_STR;
        } else {
            this.strength = STR;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
            this.doubleAttackDamage = A2_DOUBLE_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
            this.doubleAttackDamage = DOUBLE_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDamage, 0, false));
        this.moves.put(DOUBLE_ATTACK, new EnemyMoveInfo(DOUBLE_ATTACK, Intent.ATTACK, doubleAttackDamage, HITS, true));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));

        Player.PlayerListener listener = new SwordslingerListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Reckless(this)));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case ATTACK: {
                runAnim("Attack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case DOUBLE_ATTACK: {
                runAnim("Attack");
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                break;
            }
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, BUFF_AMT, true), BUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        String moveName = null;
        if (next == BUFF) {
            moveName = MOVES[0];
        }
        this.setMove(moveName, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(DOUBLE_ATTACK)) {
            this.setMoveShortcut(BUFF);
        } else if (this.lastMove(ATTACK)) {
            this.setMoveShortcut(DOUBLE_ATTACK);
        } else {
            this.setMoveShortcut(ATTACK);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        this.useShakeAnimation(5.0F);
        super.die(triggerRelics);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Swordslinger");
        NAME = Swordslinger.monsterStrings.NAME;
        MOVES = Swordslinger.monsterStrings.MOVES;
        DIALOG = Swordslinger.monsterStrings.DIALOG;
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

    public class SwordslingerListener implements Player.PlayerListener {

        private Swordslinger character;

        public SwordslingerListener(Swordslinger character) {
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