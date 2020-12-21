package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
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
import Gensokyo.events.act3.SomeoneElsesStory;
import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act3.SomeoneElse;
import Gensokyo.vfx.EmptyEffect;
import actlikeit.dungeons.CustomDungeon;
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
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private static final int PHASE2_BLAZING_KICK_DAMAGE = 45;
    private static final int A4_PHASE2_BLAZING_KICK_DAMAGE = 50;
    private int blazingKickDamage;

    private static final int HONEST_MAN_DEATH_DAMAGE = 8;
    private static final int A4_HONEST_MAN_DEATH_DAMAGE = 9;
    private static final int HITS = 2;
    private static final int PHASE2_HONEST_MAN_DEATH_DAMAGE = 11;
    private static final int A4_PHASE2_HONEST_MAN_DEATH_DAMAGE = 12;
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

    private static final int DEBUFF_AMT = 1;

    private static final int HP = 70;
    private static final int A9_HP = 74;
    private static final int PHASE2_HP = 500;
    private static final int A9_PHASE2_HP = 530;
    private int originalMaxHP;

    private static final int COOLDOWN = 2;
    private int counter = COOLDOWN;

    public static final int KAGUYA_HP = 60;
    private static final float BONUS_THRESHOLD = 0.75f;
    private int bonus;

    private boolean phase2 = false;

    private Map<Byte, EnemyMoveInfo> moves;
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
            this.statusAmt = A19_STATUS_AMT;
        } else {
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
        this.moves.put(HONEST_MAN_DEATH, new EnemyMoveInfo(HONEST_MAN_DEATH, Intent.ATTACK_DEBUFF, this.honestManDeathDamage, hits, true));
        this.moves.put(FUJIYAMA_VOLCANO, new EnemyMoveInfo(FUJIYAMA_VOLCANO, Intent.ATTACK_DEBUFF, this.fujiyamaVolcanoDamage, 0, false));
        this.moves.put(PHOENIX_REBIRTH, new EnemyMoveInfo(PHOENIX_REBIRTH, Intent.DEFEND_BUFF, -1, 0, false));

        Player.PlayerListener listener = new MokouListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);

        assignPhase2Values();
    }

    private void assignPhase2Values() {
        phase2 = true;
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
        this.moves.put(HONEST_MAN_DEATH, new EnemyMoveInfo(HONEST_MAN_DEATH, Intent.ATTACK_DEBUFF, this.honestManDeathDamage, hits, true));
        this.moves.put(FUJIYAMA_VOLCANO, new EnemyMoveInfo(FUJIYAMA_VOLCANO, Intent.ATTACK_DEBUFF, this.fujiyamaVolcanoDamage, 0, false));
        this.moves.put(PHOENIX_REBIRTH, new EnemyMoveInfo(PHOENIX_REBIRTH, Intent.DEFEND_BUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("ImmortalSmoke");
        kaguya = new Kaguya(-1600.0F, -30.0f);
        kaguya.setFlip(true, false);
        kaguya.drawX = AbstractDungeon.player.drawX;
        bonus = (int)(KAGUYA_HP * BONUS_THRESHOLD);
        SomeoneElse power = new SomeoneElse(AbstractDungeon.player, bonus, this);
        power.updateDescription();
        AbstractDungeon.player.powers.add(power);

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

        float duration = 2.0f;
        float waitDuration = 1.5f;
        addToBot(new TalkAction(this, DIALOG[0], duration, duration));
        addToBot(new VFXAction(new EmptyEffect(), waitDuration));
        addToBot(new TalkAction(true, DIALOG[1], duration, duration));
        addToBot(new VFXAction(new EmptyEffect(), waitDuration));
    }
    
    @Override
    public void takeTurn() {
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
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WeakPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
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
                addToBot(new ApplyPowerAction(this, this, new VigorPower(this, VIGOR_AMT, true), VIGOR_AMT));
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
        cards.add(new MorningStar());

        ArrayList<AbstractCard> randomCards = new ArrayList<>();
        randomCards.add(new RisingWorld());
        randomCards.add(new HouraiInAPot());
        randomCards.add(new Dawn());
        randomCards.add(new UnhurriedMind());
        Collections.shuffle(randomCards, AbstractDungeon.cardRandomRng.random);
        cards.addAll(new ArrayList<>(randomCards.subList(0, 2)));
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
        if (AbstractDungeon.getCurrRoom() instanceof EventRoom) {
            EventRoom event = (EventRoom) AbstractDungeon.getCurrRoom();
            if (event.event instanceof SomeoneElsesStory) {
                ((SomeoneElsesStory) event.event).kaguyaWins(AbstractDungeon.player.currentHealth >= bonus);
            }
        }
        runAnim("Defeat");
        ((BetterSpriterAnimation) this.animation).startDying();
        this.onBossVictoryLogic();
        super.die();
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