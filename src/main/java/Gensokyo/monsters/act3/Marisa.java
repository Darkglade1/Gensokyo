package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act3.ChargeUp;
import actlikeit.dungeons.CustomDungeon;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Marisa extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Marisa");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK_STATUS = 0;
    private static final byte MULTI_ATTACK = 1;
    private static final byte ATTACK_DEBUFF = 2;
    private static final byte BUFF = 3;

    private static final int ATTACK_STATUS_DMG = 25;
    private static final int A3_ATTACK_STATUS_DMG = 27;
    private int attackStatusDmg;

    private static final int STATUS_AMT = 1;
    private static final int A18_STATUS_AMT = 2;
    private int statusAmt;

    private static final int MULTI_ATTACK_DMG = 3;
    private static final int A3_MULTI_ATTACK_DMG = 3;
    private static final int MULTI_ATTACK_HITS = 6;
    private int multiAttackDmg;

    private static final int ATTACK_DEBUFF_DMG = 9;
    private static final int A3_ATTACK_DEBUFF_DMG = 10;
    private static final int ATTACK_DEBUFF_HITS = 2;
    private int attackDebuffDmg;

    private static final int DEBUFF_AMT = 2;
    
    private static final int BUFF_AMT = 2;

    private static final int STRENGTH = 5;
    private static final int A18_STRENGTH = 8;
    private int strength;

    private static final int HP = 250;
    private static final int A8_HP = 265;

    private Map<Byte, EnemyMoveInfo> moves;

    public Marisa() {
        this(0.0f, 0.0f);
    }

    public Marisa(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Marisa/Spriter/MarisaAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
            this.statusAmt = A18_STATUS_AMT;
        } else {
            this.strength = STRENGTH;
            this.statusAmt = STATUS_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(A8_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            attackStatusDmg = A3_ATTACK_STATUS_DMG;
            multiAttackDmg = A3_MULTI_ATTACK_DMG;
            attackDebuffDmg = A3_ATTACK_DEBUFF_DMG;
        } else {
            attackStatusDmg = ATTACK_STATUS_DMG;
            multiAttackDmg = MULTI_ATTACK_DMG;
            attackDebuffDmg = ATTACK_DEBUFF_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK_STATUS, new EnemyMoveInfo(ATTACK_STATUS, Intent.ATTACK_DEBUFF, attackStatusDmg, 0, false));
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, Intent.ATTACK, multiAttackDmg, MULTI_ATTACK_HITS, true));
        this.moves.put(ATTACK_DEBUFF, new EnemyMoveInfo(ATTACK_DEBUFF, Intent.ATTACK_DEBUFF, attackDebuffDmg, ATTACK_DEBUFF_HITS, true));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));

        Player.PlayerListener listener = new MarisaListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("MasterSpark");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargeUp(this, strength)));
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
            case ATTACK_STATUS: {
                runAnim("Spark");
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 5.0F * Settings.scale), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), statusAmt));
                break;
            }
            case MULTI_ATTACK: {
                runAnim("Spark");
                for (int i = 0; i < MULTI_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            }
            case ATTACK_DEBUFF: {
                runAnim("Smack");
                for (int i = 0; i < ATTACK_DEBUFF_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
            case BUFF: {
                runAnim("Special");
                addToBot(new ApplyPowerAction(this, this, new VigorPower(this, BUFF_AMT, true), BUFF_AMT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            setMoveShortcut(MULTI_ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK_STATUS)) {
                possibilities.add(ATTACK_STATUS);
            }
            if (!this.lastMove(MULTI_ATTACK) && !this.lastMoveBefore(MULTI_ATTACK)) {
                possibilities.add(MULTI_ATTACK);
            }
            if (!this.lastMove(ATTACK_DEBUFF) && !this.lastMoveBefore(ATTACK_DEBUFF)) {
                possibilities.add(ATTACK_DEBUFF);
            }
            if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
                possibilities.add(BUFF);
            }
            setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
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

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class MarisaListener implements Player.PlayerListener {

        private Marisa character;

        public MarisaListener(Marisa character) {
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