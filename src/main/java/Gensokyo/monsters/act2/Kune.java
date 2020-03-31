package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Kune extends CustomMonster
{
    public static final String ID = "Gensokyo:Kune";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte DEBUFF = 2;
    private static final int ATTACK_DAMAGE = 4;
    private static final int HITS = 2;
    private static final int STATUS_AMT = 1;
    private static final int BUFF_AMT = 2;
    private static final int A18_BUFF_AMT = 3;
    private static final int HP_MIN = 14;
    private static final int HP_MAX = 16;
    private static final int A8_HP_MIN = 15;
    private static final int A8_HP_MAX = 17;
    private int attackDamage;
    private int buff;
    public Reisen reisen;
    private int num;

    public Kune() {
        this(0.0f, 0.0f, null, -1);
    }

    public Kune(final float x, final float y, Reisen reisen, int num) {
        super(NAME, ID, HP_MAX, 0.0F, 0, 150.0f, 260.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kune/Spriter/KuneAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.reisen = reisen;
        this.num = num;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.buff = A18_BUFF_AMT;
        } else {
            this.buff = BUFF_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        this.attackDamage = ATTACK_DAMAGE;
        this.damage.add(new DamageInfo(this, this.attackDamage));

        Player.PlayerListener listener = new KuneListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);

        runAnim("Spawn");
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                runAnim("Attack");
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                }
                break;
            }
            case DEBUFF: {
                runAnim("Attack");
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Slimed(), STATUS_AMT, true, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, buff), buff));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int roll) {
        if (num == 0) {
            if (!this.lastMove(ATTACK)) {
                this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base, HITS, true);
            } else {
                this.setMove(DEBUFF, Intent.DEBUFF);
            }
        } else {
            if (!this.lastMove(DEBUFF)) {
                this.setMove(DEBUFF, Intent.DEBUFF);
            } else {
                this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base, HITS, true);
            }
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
        if (reisen != null) {
            if (num == 0 || num == 1) {
                reisen.minions[num] = null;
            }
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Kune");
        NAME = Kune.monsterStrings.NAME;
        MOVES = Kune.monsterStrings.MOVES;
        DIALOG = Kune.monsterStrings.DIALOG;
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

    public class KuneListener implements Player.PlayerListener {

        private Kune character;

        public KuneListener(Kune character) {
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