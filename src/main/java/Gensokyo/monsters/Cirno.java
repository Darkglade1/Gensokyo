package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.cards.Frozen;
import Gensokyo.powers.FairyFury;
import Gensokyo.powers.FairyOfIce;
import Gensokyo.powers.Immortality;
import Gensokyo.powers.Strongest;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class Cirno extends CustomMonster
{
    public static final String ID = "Gensokyo:Cirno";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte BUFF = 1;
    private static final byte ATTACK = 2;
    private static final byte DEBUFF_ATTACK = 3;
    private static final byte REVIVE = 4;
    private static final byte LEAVE = 5;
    private static final byte GROUP_BUFF = 6;
    private static final int NORMAL_ATTACK_DAMAGE = 10;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 11;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 10;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 11;
    private static final int STATUS_COUNT = 1;
    private static final int A18_STATUS_COUNT = 2;
    private static final int GROUP_BUFF_AMT = 1;
    private static final int A18_GROUP_BUFF_AMT = 2;
    private static final int BLOCK = 2;
    private static final int STRENGTH = 1;
    private static final int NEGATIVE_STRENGTH = -10;
    private static final int STRENGTH_INCREMENT = 1;
    private static final int HP_MIN = 20;
    private static final int HP_MAX = 21;
    private static final int A_2_HP_MIN = 21;
    private static final int A_2_HP_MAX = 22;
    private static final int BUFF_COUNTER_THESHOLD = 2;
    private int normalDamage;
    private int debuffDamage;
    private int status;
    private int groupBuffAmt;
    private int buffCounter = 0;

    public Cirno() {
        this(0.0f, 0.0f);
    }

    public Cirno(final float x, final float y) {
        super(Cirno.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 165.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Cirno/Spriter/CirnoAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.status = A18_STATUS_COUNT;
            this.groupBuffAmt = A18_GROUP_BUFF_AMT;
        } else {
            this.status = STATUS_COUNT;
            this.groupBuffAmt = GROUP_BUFF_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
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

        Player.PlayerListener listener = new CirnoListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Gensokyo/TomboyishGirl.mp3");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Immortality(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Strongest(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FairyFury(this, STRENGTH_INCREMENT)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FairyOfIce(this, this.status)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, NEGATIVE_STRENGTH), NEGATIVE_STRENGTH));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BUFF: {
                //runAnim("Spell");
                if (this.firstMove) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                    this.firstMove = false;
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, STRENGTH), STRENGTH));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Frozen(), this.status + 1));
                break;
            }
            case DEBUFF_ATTACK: {
                //runAnim("Fan");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Frozen(), this.status));
                buffCounter++;
                break;
            }
            case ATTACK: {
                //runAnim("Parasol");
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                buffCounter++;
                break;
            }
            case GROUP_BUFF: {
                //runAnim("Parasol");
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (mo instanceof GreaterFairy) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new StrengthPower(mo, groupBuffAmt), groupBuffAmt));
                    } else if (mo instanceof SunflowerFairy || mo instanceof ZombieFairy) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new ThornsPower(mo, groupBuffAmt), groupBuffAmt));
                    }
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, BLOCK));
                }
                buffCounter = 0;
                break;
            }
            case REVIVE: {
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                this.halfDead = false;
                Iterator var1 = AbstractDungeon.player.relics.iterator();
                while(var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var1.next();
                    r.onSpawnMonster(this);
                }
                break;
            }
            case LEAVE:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, LEAVE, Intent.ESCAPE));
                this.animation.setFlip(true, false);
                AbstractDungeon.getCurrRoom().cannotLose = false;
                break;
        }
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) { //helps ensure all monsters flee at the same time
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(mo));
        }
    }

    @Override
    protected void getMove(final int num) {
        //buffs, does multi-attack, then alternates between debuff attack and multi-attack while using group buff when it's off cooldown
        if(this.willLeave()) {
            this.setMove(LEAVE, Intent.ESCAPE);
        } else if (this.halfDead) {
            this.setMove(REVIVE, Intent.NONE);
        } else if (this.firstMove) {
            this.setMove(MOVES[0], BUFF, Intent.UNKNOWN);
        } else if (this.buffCounter >= BUFF_COUNTER_THESHOLD) {
            this.setMove(GROUP_BUFF, Intent.DEFEND_BUFF);
        } else {
            if (this.lastMove(GROUP_BUFF)) {
                this.setMove(Cirno.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            } else if (this.lastMove(ATTACK)){
                this.setMove(Cirno.MOVES[1], DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
            } else {
                this.setMove(Cirno.MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            if (this.hasPower(FairyFury.POWER_ID)) {
                FairyFury fury = (FairyFury)this.getPower(FairyFury.POWER_ID);
                fury.onTrigger();
            }

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
            if (this.nextMove != REVIVE && this.nextMove != LEAVE) {
                this.setMove(REVIVE, Intent.NONE);
                this.createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, REVIVE, Intent.NONE));
            }
            ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
            for (AbstractPower power : this.powers) {
                if (!(power instanceof Immortality) && !(power instanceof StrengthPower) && !(power instanceof FairyFury) && !(power instanceof Strongest) && !(power instanceof FairyOfIce)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }

    public boolean willLeave() {
        if (this.hasPower(StrengthPower.POWER_ID)) {
            return this.getPower(StrengthPower.POWER_ID).amount > 0;
        }
        return false;
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Cirno");
        NAME = Cirno.monsterStrings.NAME;
        MOVES = Cirno.monsterStrings.MOVES;
        DIALOG = Cirno.monsterStrings.DIALOG;
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

    public class CirnoListener implements Player.PlayerListener {

        private Cirno character;

        public CirnoListener(Cirno character) {
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