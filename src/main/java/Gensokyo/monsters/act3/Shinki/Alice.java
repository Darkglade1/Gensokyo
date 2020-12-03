package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.CustomIntents.IntentEnums;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act3.DollJudgement;
import com.badlogic.gdx.graphics.Color;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Alice extends AbstractShinkiDelusion
{
    public static final String ID = GensokyoMod.makeID("Alice");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte MULTI_ATTACK = 1;
    private static final byte BUFF = 2;
    private static final byte SUMMON = 3;

    private static final int ATTACK_DMG = 22;
    private static final int A4_ATTACK_DMG = 24;
    private int attackDmg;

    private static final int MULTI_ATTACK_DMG = 8;
    private static final int A4_MULTI_ATTACK_DMG = 9;
    private static final int MULTI_ATTACK_HITS = 2;
    private int multiAttackDmg;

    private static final int BUFF_AMT = 2;

    private static final int STRENGTH = 1;

    public static final int DOLLS_EXPLODE_TIMER = 3;

    private static final int HP = 200;
    private static final int A9_HP = 220;

    public ArrayList<Doll> dolls = new ArrayList<>();
    private Map<Byte, EnemyMoveInfo> moves;

    public Alice(final float x, final float y, Shinki shinki) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Marisa/Spriter/MarisaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.shinki = shinki;
        this.event1 = new AliceEvent1(shinki);
        this.event2 = new AliceEvent2(shinki);
        this.event3 = new AliceEvent3(shinki);
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(A9_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            attackDmg = A4_ATTACK_DMG;
            multiAttackDmg = A4_MULTI_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
            multiAttackDmg = MULTI_ATTACK_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, IntentEnums.ATTACK_AREA, attackDmg, 0, false));
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, IntentEnums.ATTACK_AREA, multiAttackDmg, MULTI_ATTACK_HITS, true));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
        this.moves.put(SUMMON, new EnemyMoveInfo(SUMMON, Intent.UNKNOWN, -1, 0, false));

        Player.PlayerListener listener = new AliceListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DollJudgement(this, STRENGTH)));
        Summon();
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
            case ATTACK: {
                DamageInfo playerInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                playerInfo.applyPowers(this, AbstractDungeon.player);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, playerInfo, AbstractGameAction.AttackEffect.NONE));

                DamageInfo monsterInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                monsterInfo.applyPowers(this, shinki);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(shinki.drawX, shinki.drawY)));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(shinki, monsterInfo, AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case MULTI_ATTACK: {
                for (int i = 0; i < MULTI_ATTACK_HITS; i++) {
                    DamageInfo playerInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                    playerInfo.applyPowers(this, AbstractDungeon.player);
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, playerInfo, AbstractGameAction.AttackEffect.NONE));

                    DamageInfo monsterInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                    monsterInfo.applyPowers(this, shinki);
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(shinki.hb.cX, shinki.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(shinki, monsterInfo, AbstractGameAction.AttackEffect.NONE));
                }
                break;
            }
            case BUFF: {
                addToBot(new ApplyPowerAction(this, this, new VigorPower(this, BUFF_AMT, true), BUFF_AMT));
                break;
            }
            case SUMMON: {
                Summon();
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void Summon() {
        Doll minion1 = new Doll(-720.0F, 0.0F, this);
        dolls.add(minion1);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion1, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion1, this, new ExplosivePower(minion1, DOLLS_EXPLODE_TIMER)));

        Doll minion2 = new Doll(-240.0F, 0.0F, this);
        dolls.add(minion2);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion2, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion2, this, new ExplosivePower(minion2, DOLLS_EXPLODE_TIMER)));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (dolls.size() == 0 && !firstMove) {
            setMoveShortcut(SUMMON);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK)) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(MULTI_ATTACK)) {
                possibilities.add(MULTI_ATTACK);
            }
            if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
                possibilities.add(BUFF);
            }
            setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    @Override
    public void die(boolean triggerRelics) {
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

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class AliceListener implements Player.PlayerListener {

        private Alice character;

        public AliceListener(Alice character) {
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