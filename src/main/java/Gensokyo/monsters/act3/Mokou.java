package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.RezAction;
import Gensokyo.cards.NewImpossibleRequests.NewImpossibleRequest;
import Gensokyo.powers.act2.DummyLunaticPrincess;
import Gensokyo.powers.act2.HouraiImmortal;
import Gensokyo.powers.act2.LunaticPrincess;
import Gensokyo.powers.act3.MokouHouraiImmortal;
import Gensokyo.powers.act3.NewDummyLunaticPrincess;
import Gensokyo.powers.act3.NewLunaticPrincess;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Mokou extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Mokou");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte BLAZING_KICK = 0;
    private static final byte HONEST_MAN_DEATH = 1;
    private static final byte FUJIYAMA_VOLCANO = 2;
    private static final byte PHOENIX_REBIRTH = 3;

    private static final int BLAZING_KICK_DAMAGE = 24;
    private static final int A4_BLAZING_KICK_DAMAGE = 26;
    private static final int PHASE2_BLAZING_KICK_DAMAGE = 50;
    private static final int A4_PHASE2_BLAZING_KICK_DAMAGE = 55;
    private int blazingKickDamage;

    private static final int HONEST_MAN_DEATH_DAMAGE = 10;
    private static final int A4_HONEST_MAN_DEATH_DAMAGE = 11;
    private static final int HITS = 2;
    private static final int PHASE2_HONEST_MAN_DEATH_DAMAGE = 13;
    private static final int A4_PHASE2_HONEST_MAN_DEATH_DAMAGE = 14;
    private static final int PHASE2_HITS = 3;
    private int honestManDeathDamage;
    private int hits;

    private static final int FUJIYAMA_VOLCANO_DAMAGE = 18;
    private static final int A4_FUJIYAMA_VOLCANO_DAMAGE = 20;
    private static final int PHASE2_FUJIYAMA_VOLCANO_DAMAGE = 30;
    private static final int A4_PHASE2_FUJIYAMA_VOLCANO_DAMAGE = 33;
    private int fujiyamaVolcanoDamage;

    private static final int BLOCK = 10;
    private static final int A9_BLOCK = 12;
    private int block;

    private static final int STATUS_AMT = 2;
    private static final int A19_STATUS_AMT = 3;
    private int statusAmt;

    public static final int STRENGTH_GAIN = 3;
    public static final int A19_STRENGTH_GAIN = 4;
    private int strengthGain;

    private static final int HP = 70;
    private static final int A9_HP = 74;
    private static final int PHASE2_HP = 500;
    private static final int A9_PHASE2_HP = 530;
    private int originalMaxHP;

    private static final int COOLDOWN = 2;
    private int counter = COOLDOWN;

    private static final int DEATH_THRESHOLD = 6;
    private int deathCounter = DEATH_THRESHOLD;

    private boolean phase2 = false;

    private Map<Byte, EnemyMoveInfo> moves;
    public NewImpossibleRequest request;
    private int requestsCompleted = 0;

    public Mokou() {
        this(0.0f, 0.0f);
    }

    public Mokou(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kaguya/Spriter/KaguyaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strengthGain = A19_STRENGTH_GAIN;
            this.statusAmt = A19_STATUS_AMT;
        } else {
            this.strengthGain = STRENGTH_GAIN;
            this.statusAmt = STATUS_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }
        this.originalMaxHP = maxHealth;

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.blazingKickDamage = A4_BLAZING_KICK_DAMAGE;
            this.honestManDeathDamage = A4_HONEST_MAN_DEATH_DAMAGE;
            this.fujiyamaVolcanoDamage = A4_FUJIYAMA_VOLCANO_DAMAGE;
        } else {
            this.blazingKickDamage = BLAZING_KICK_DAMAGE;
            this.honestManDeathDamage = HONEST_MAN_DEATH_DAMAGE;
            this.fujiyamaVolcanoDamage = FUJIYAMA_VOLCANO_DAMAGE;
        }
        this.hits = HITS;

        this.moves = new HashMap<>();
        this.moves.put(BLAZING_KICK, new EnemyMoveInfo(BLAZING_KICK, Intent.ATTACK, this.blazingKickDamage, 0, false));
        this.moves.put(HONEST_MAN_DEATH, new EnemyMoveInfo(HONEST_MAN_DEATH, Intent.ATTACK, this.honestManDeathDamage, hits, true));
        this.moves.put(FUJIYAMA_VOLCANO, new EnemyMoveInfo(FUJIYAMA_VOLCANO, Intent.ATTACK_DEBUFF, this.fujiyamaVolcanoDamage, 0, false));
        this.moves.put(PHOENIX_REBIRTH, new EnemyMoveInfo(PHOENIX_REBIRTH, Intent.DEFEND_BUFF, -1, 0, false));
    }

    private void assignPhase2Moves() {
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_PHASE2_HP);
        } else {
            this.setHp(PHASE2_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.blazingKickDamage = A4_PHASE2_BLAZING_KICK_DAMAGE;
            this.honestManDeathDamage = A4_PHASE2_HONEST_MAN_DEATH_DAMAGE;
            this.fujiyamaVolcanoDamage = A4_PHASE2_FUJIYAMA_VOLCANO_DAMAGE;
        } else {
            this.blazingKickDamage = PHASE2_BLAZING_KICK_DAMAGE;
            this.honestManDeathDamage = PHASE2_HONEST_MAN_DEATH_DAMAGE;
            this.fujiyamaVolcanoDamage = PHASE2_FUJIYAMA_VOLCANO_DAMAGE;
        }
        this.hits = PHASE2_HITS;
        moves.clear();
        this.moves.put(BLAZING_KICK, new EnemyMoveInfo(BLAZING_KICK, Intent.ATTACK, this.blazingKickDamage, 0, false));
        this.moves.put(HONEST_MAN_DEATH, new EnemyMoveInfo(HONEST_MAN_DEATH, Intent.ATTACK, this.honestManDeathDamage, hits, true));
        this.moves.put(FUJIYAMA_VOLCANO, new EnemyMoveInfo(FUJIYAMA_VOLCANO, Intent.ATTACK_DEBUFF, this.fujiyamaVolcanoDamage, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        //CustomDungeon.playTempMusicInstantly("LunaticPrincess");
        request = new NewImpossibleRequest();
        if (AbstractDungeon.ascensionLevel >= 19) {
            request.upgrade();
        }
        request.transform();
        addToBot(new ApplyPowerAction(this, this, new MokouHouraiImmortal(this, DEATH_THRESHOLD)));
        addToBot(new ApplyPowerAction(this, this, new NewDummyLunaticPrincess(this)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new NewLunaticPrincess(AbstractDungeon.player, this, request)));
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(request));
        addToBot(new TalkAction(this, DIALOG[0]));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            addToBot(new TalkAction(this, DIALOG[1]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        switch (this.nextMove) {
            case BLAZING_KICK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new GhostIgniteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale), 0.05F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                break;
            }
            case HONEST_MAN_DEATH: {
                for (int i = 0; i < hits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            }
            case FUJIYAMA_VOLCANO: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                Burn burn = new Burn();
                if (phase2) {
                    burn.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(burn, statusAmt));
                break;
            }
            case PHOENIX_REBIRTH: {
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                addToBot(new GainBlockAction(this, block));
                counter = COOLDOWN + 1;
                break;
            }
        }
        counter--;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (counter <= 0 && !phase2) {
            setMoveShortcut(PHOENIX_REBIRTH);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(BLAZING_KICK)) {
                possibilities.add(BLAZING_KICK);
            }
            if (!this.lastMove(HONEST_MAN_DEATH) && !this.lastMoveBefore(HONEST_MAN_DEATH)) {
                possibilities.add(HONEST_MAN_DEATH);
            }
            if (!this.lastMove(FUJIYAMA_VOLCANO) && !this.lastMoveBefore(FUJIYAMA_VOLCANO)) {
                possibilities.add(FUJIYAMA_VOLCANO);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
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

            ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
            for (AbstractPower power : this.powers) {
                if (!(power instanceof HouraiImmortal) && !(power instanceof DummyLunaticPrincess) && !(power instanceof StrengthPower) && !(power instanceof GainStrengthPower)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }

            AbstractDungeon.actionManager.addToBottom(new RezAction(this));
            AbstractDungeon.onModifyPower();

            if (request.completed) {
                requestsCompleted++;
            }

            request.completed = false;
            request.requestCounter++;
            if (AbstractDungeon.player.hasPower(LunaticPrincess.POWER_ID)) {
                LunaticPrincess power = (LunaticPrincess)AbstractDungeon.player.getPower(LunaticPrincess.POWER_ID);
                power.counter = 0;
            }
            request.transform();

            deathCounter--;
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, HouraiImmortal.POWER_ID, 1));
            if (this.deathCounter <= 0) {
                phase2 = true;
                assignPhase2Moves();
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
            }
        }
    }

    @Override
    public void die() {
        if (phase2) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.onBossVictoryLogic();
        }
        if (this.maxHealth <= 0) {
            this.maxHealth = originalMaxHP;
            AbstractDungeon.actionManager.addToBottom(new InstantKillAction(this));
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

    public class MokouListener implements Player.PlayerListener {

        private Mokou character;

        public MokouListener(Mokou character) {
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