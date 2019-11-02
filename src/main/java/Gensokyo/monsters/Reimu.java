package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.Position;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;

public class Reimu extends CustomMonster
{
    public static final String ID = "Gensokyo:Reimu";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte OPENING = 1;
    private static final byte SUMMON = 2;
    private static final byte MEGA_DEBUFF = 3;
    private static final byte ATTACK = 4;
    private static final byte LAST_WORD = 5;
    private static final byte TRAIN = 6;
    private static final int NORMAL_ATTACK_DAMAGE = 10;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 11;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 14;
    private static final int A4_DEBUFF_ATTACK_DAMAGE = 16;
    private static final int TRAIN_ATTACK_DAMAGE = 8;
    private static final int A4_TRAIN_ATTACK_DAMAGE = 9;
    private static final int TRAIN_ATTACK_HITS = 3;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int A19_DEBUFF_AMOUNT = 3;
    private static final int STRENGTH_DRAIN_AMOUNT = 2;
    private static final int A19_STRENGTH_DRAIN_AMOUNT = 3;
    private static final int WOUND_AMOUNT = 1;
    private static final int A19_WOUND_AMOUNT = 2;
    private static final int BLOCK = 10;
    private static final int A9_BLOCK = 13;
    private int normalDamage;
    private int debuffDamage;
    private int trainDamage;
    private int debuffAmount;
    private int strengthDrain;
    private int block;
    private int dazes;
    private static final int HP = 220;
    private static final int A9_HP = 230;

    public static final float orbOffset = 225.0F * Settings.scale;
    public ArrayList[][] orbs = new ArrayList[3][3];

    public Reimu() {
        this(0.0f, 0.0f);
    }

    public Reimu(final float x, final float y) {
        super(Reimu.NAME, ID, HP, -5.0F, 0, 280.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Reimu/Spriter/ReimuAnimations.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmount = A19_DEBUFF_AMOUNT;
            this.strengthDrain = A19_STRENGTH_DRAIN_AMOUNT;
            this.dazes = A19_WOUND_AMOUNT;
        } else {
            this.debuffAmount = DEBUFF_AMOUNT;
            this.strengthDrain = STRENGTH_DRAIN_AMOUNT;
            this.dazes = WOUND_AMOUNT;
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

        for(int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                orbs[i][j] = new ArrayList<AbstractMonster>();
            }
        }

        Player.PlayerListener listener = new ReimuListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Gensokyo/G Free.mp3");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new Position(AbstractDungeon.player, 1)));
    }

    private void spawnOrb() {
        for (int i = 0; i < orbs.length; i++) {
            ArrayList<Integer> emptySpots = new ArrayList<>();
            for (int j = 0; j < orbs[i].length; j++) {
                System.out.println(orbs[i][j].size());
                if (orbs[i][j].isEmpty()) {
                    System.out.println(j);
                    emptySpots.add(j);
                }
            }
            Collections.shuffle(emptySpots, AbstractDungeon.monsterRng.random);
            System.out.println(emptySpots.size());
            while(emptySpots.size() > 1) {
                System.out.println(emptySpots.size());
                int position = emptySpots.remove(0) + 1;
                int delay = i + 1;
                int type = AbstractDungeon.monsterRng.random(1, 3);
                float x = -orbOffset * (4 - delay);
                float y = orbOffset * (position - 1);
                AbstractMonster orb = new YinYangOrb(x, y, type, position, delay, this);
                orbs[delay - 1][position - 1].add(orb);
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(orb, true));
            }
        }
    }

    private int orbNum() {
        int counter = 0;
        for (int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                counter += orbs[i][j].size();
            }
        }
        return counter;
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case OPENING: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Reimu.DIALOG[0]));
                spawnOrb();
                break;
            }
            case SUMMON: {
                spawnOrb();
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            setMove(OPENING, Intent.UNKNOWN);
            this.firstMove = false;
        } else {
            if (orbNum() < 3) {
                setMove(SUMMON, Intent.UNKNOWN);
            } else {
                this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
            }

        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Reimu");
        NAME = Reimu.monsterStrings.NAME;
        MOVES = Reimu.monsterStrings.MOVES;
        DIALOG = Reimu.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof YinYangOrb) {
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
            }
        }
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

    public class ReimuListener implements Player.PlayerListener {

        private Reimu character;

        public ReimuListener(Reimu character) {
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