package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.AnimatedMoveActualAction;
import Gensokyo.actions.RezAction;
import Gensokyo.cards.Lunar.BrilliantDragonBullet;
import Gensokyo.cards.Lunar.BuddhistDiamond;
import Gensokyo.cards.Lunar.Dawn;
import Gensokyo.cards.Lunar.DreamlikeParadise;
import Gensokyo.cards.Lunar.EverlastingLife;
import Gensokyo.cards.Lunar.HouraiInAPot;
import Gensokyo.cards.Lunar.LifeSpringInfinity;
import Gensokyo.cards.Lunar.MorningMist;
import Gensokyo.cards.Lunar.MorningStar;
import Gensokyo.cards.Lunar.NewMoon;
import Gensokyo.cards.Lunar.RainbowDanmaku;
import Gensokyo.cards.Lunar.RisingWorld;
import Gensokyo.cards.Lunar.SalamanderShield;
import Gensokyo.cards.Lunar.UnhurriedMind;
import Gensokyo.cards.Lunar.UnlikelyAid;
import Gensokyo.cards.NewImpossibleRequests.NewImpossibleRequest;
import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act3.MakePlayerInvisible;
import Gensokyo.powers.act3.MokouHouraiImmortal;
import Gensokyo.powers.act3.NewDummyLunaticPrincess;
import Gensokyo.powers.act3.NewLunaticPrincess;
import Gensokyo.vfx.EmptyEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
import com.megacrit.cardcrawl.unlock.UnlockTracker;
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
    private static final int PHASE2_BLOCK = 20;
    private static final int A9_PHASE2_BLOCK = 24;
    private int block;

    private static final int VIGOR_AMT = 2;

    private static final int STATUS_AMT = 2;
    private static final int A19_STATUS_AMT = 2;
    private int statusAmt;

    public static final int STRENGTH_GAIN = 3;
    public static final int A19_STRENGTH_GAIN = 3;
    private int strengthGain;

    private static final int HP = 70;
    private static final int A9_HP = 74;
    private static final int PHASE2_HP = 500;
    private static final int A9_PHASE2_HP = 530;
    private int originalMaxHP;

    private static final int COOLDOWN = 2;
    private int counter = COOLDOWN;

    private static final int DEATH_THRESHOLD = 5;
    private int deathCounter = DEATH_THRESHOLD;

    private boolean phase2 = false;

    private Map<Byte, EnemyMoveInfo> moves;
    public NewImpossibleRequest request;
    private int requestsCompleted = 0;
    private Kaguya kaguya = null;

    public Mokou() {
        this(0.0f, 0.0f);
    }

    public Mokou(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 285.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Mokou/Spriter/MokouAnimation.scml");
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

        Player.PlayerListener listener = new MokouListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    private void assignPhase2Values() {
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_PHASE2_HP);
            this.block = A9_PHASE2_BLOCK;
        } else {
            this.setHp(PHASE2_HP);
            this.block = PHASE2_BLOCK;
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
        this.moves.put(PHOENIX_REBIRTH, new EnemyMoveInfo(PHOENIX_REBIRTH, Intent.DEFEND_BUFF, -1, 0, false));
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
        UnlockTracker.unlockCard(NewImpossibleRequest.ID);
        addToBot(new TalkAction(this, DIALOG[0]));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove && !phase2) {
            addToBot(new TalkAction(this, DIALOG[1]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        switch (this.nextMove) {
            case BLAZING_KICK: {
                runAnim("Kick");
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new GhostIgniteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale), 0.05F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                break;
            }
            case HONEST_MAN_DEATH: {
                runAnim("MagicA");
                for (int i = 0; i < hits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            }
            case FUJIYAMA_VOLCANO: {
                runAnim("MagicA");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                Burn burn = new Burn();
                if (phase2) {
                    burn.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(burn, statusAmt));
                break;
            }
            case PHOENIX_REBIRTH: {
                runAnim("MagicB");
                if (phase2) {
                    addToBot(new ApplyPowerAction(this, this, new VigorPower(this, VIGOR_AMT, true), VIGOR_AMT));
                } else {
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                }
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
        if (counter <= 0) {
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
        if (this.currentHealth <= 0 && !this.halfDead && !phase2) {
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
                if (!(power instanceof MokouHouraiImmortal) && !(power instanceof NewDummyLunaticPrincess) && !(power instanceof StrengthPower) && !(power instanceof GainStrengthPower)) {
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
            if (AbstractDungeon.player.hasPower(NewLunaticPrincess.POWER_ID)) {
                NewLunaticPrincess power = (NewLunaticPrincess)AbstractDungeon.player.getPower(NewLunaticPrincess.POWER_ID);
                power.counter = 0;
            }
            request.transform();

            deathCounter--;
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, MokouHouraiImmortal.POWER_ID, 1));
            if (this.deathCounter <= 0) {
                transitionToPhase2();
            }
        }
    }

    private void transitionToPhase2() {
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;

        phase2 = true;
        assignPhase2Values();
        counter = COOLDOWN;
        AbstractDungeon.actionManager.addToBottom(new RezAction(this));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, NewLunaticPrincess.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, NewDummyLunaticPrincess.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        kaguya = new Kaguya(-1600.0F, -30.0f);
        kaguya.setFlip(true, false);
        addToBot(new AnimatedMoveActualAction(kaguya, kaguya.drawX, kaguya.drawY, AbstractDungeon.player.drawX, kaguya.drawY));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.5f));

        float duration = 2.0f;
        float waitDuration = 1.5f;
        AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOG[2], duration, duration));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), waitDuration));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3], duration, duration));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), waitDuration));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DIALOG[4] + AbstractDungeon.player.name + DIALOG[5], duration, duration));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), waitDuration));

        float originalPlayerX = AbstractDungeon.player.drawX;
        float originalPlayerY = AbstractDungeon.player.drawY;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                this.isDone = true;
            }
        });
        addToBot(new AnimatedMoveActualAction(AbstractDungeon.player, AbstractDungeon.player.drawX, AbstractDungeon.player.drawY, -200.0F * scaleWidth, AbstractDungeon.player.drawY));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.5f));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MakePlayerInvisible(AbstractDungeon.player)));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.drawX = originalPlayerX;
                AbstractDungeon.player.drawY = originalPlayerY;
                this.isDone = true;
            }
        });

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.drawPile.group.clear();
                AbstractDungeon.player.discardPile.group.clear();
                AbstractDungeon.player.exhaustPile.group.clear();
                AbstractDungeon.player.hand.group.clear();
                this.isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> newStartingDeck = getKaguyaStartingDeck();
                AbstractDungeon.player.drawPile.group.addAll(newStartingDeck);
                AbstractDungeon.player.drawPile.shuffle();
                for (AbstractCard card : newStartingDeck) {
                    UnlockTracker.unlockCard(card.cardID);
                }
                this.isDone = true;
            }
        });

        if (requestsCompleted >= 5) {
            addToBot(new MakeTempCardInHandAction(new UnhurriedMind(), 1));
        }
        if (requestsCompleted >= 4) {
            addToBot(new MakeTempCardInDrawPileAction(new Dawn(), 1, true, true, false));
        }
        if (requestsCompleted >= 3) {
            addToBot(new MakeTempCardInDrawPileAction(new HouraiInAPot(), 1, true, true, false));
        }
        if (requestsCompleted >= 2) {
            addToBot(new MakeTempCardInDrawPileAction(new RisingWorld(), 1, true, true, false));
        }
        if (requestsCompleted >= 1) {
            addToBot(new MakeTempCardInDrawPileAction(new MorningStar(), 1, true, true, false));
        }

        if (!AbstractDungeon.actionManager.turnHasEnded) {
            addToBot(new SkipEnemiesTurnAction());
            addToBot(new PressEndTurnButtonAction());
        }

    }

    private ArrayList<AbstractCard> getKaguyaStartingDeck() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cards.add(new BrilliantDragonBullet());
            cards.add(new SalamanderShield());
        }
        for (int i = 0; i < 2; i++) {
            cards.add(new BuddhistDiamond());
            cards.add(new EverlastingLife());
            cards.add(new MorningMist());
            cards.add(new NewMoon());
        }
        cards.add(new DreamlikeParadise());
        cards.add(new LifeSpringInfinity());
        cards.add(new RainbowDanmaku());
        cards.add(new UnlikelyAid());
        return cards;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (kaguya != null) {
            kaguya.render(sb);
        }
    }

    @Override
    public void die() {
        if (phase2) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            runAnim("Defeat");
            ((BetterSpriterAnimation)this.animation).startDying();
            this.onBossVictoryLogic();
            super.die();
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