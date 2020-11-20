package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.actions.AnimatedMoveActualAction;
import Gensokyo.actions.SetFlipAction;
import Gensokyo.monsters.act2.Miko;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act2.Purity;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.RivalPosition;
import Gensokyo.powers.act2.TenDesires;
import Gensokyo.powers.act3.Retribution;
import Gensokyo.vfx.FlexibleCalmParticleEffect;
import Gensokyo.vfx.FlexibleStanceAuraEffect;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Remilia extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Remilia");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte LIFESTEAL_ATTACK = 1;
    private static final byte DEFEND = 2;
    private static final byte DEBUFF = 3;

    private static final int NORMAL_ATTACK_DAMAGE = 25;

    private static final int LIFESTEAL_ATTACK_DAMAGE = 15;

    private static final int BLOCK = 15;

    private static final int DEBUFF_AMT = 2;

    private static final int HP = 150;

    private static final int COOLDOWN = 2;
    private int counter = 0;

    public float originalX;
    public float originalY;

    private Flandre rival;

    private Map<Byte, EnemyMoveInfo> moves;

    public Remilia() {
        this(0.0f, 0.0f);
    }

    public Remilia(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, -20.0F, 230.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Byakuren/Spriter/ByakurenAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, NORMAL_ATTACK_DAMAGE, 0, false));
        this.moves.put(LIFESTEAL_ATTACK, new EnemyMoveInfo(LIFESTEAL_ATTACK, Intent.ATTACK_BUFF, LIFESTEAL_ATTACK_DAMAGE, 0, false));
        this.moves.put(DEFEND, new EnemyMoveInfo(DEFEND, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEBUFF, -1, 0, false));

        Player.PlayerListener listener = new RemiliaListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
//        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
//            AbstractDungeon.getCurrRoom().playBgmInstantly("CosmicMind");
//        } else {
//            AbstractDungeon.getCurrRoom().playBgmInstantly("TrueAdmin");
//        }
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Flandre) {
                rival = (Flandre)mo;
            }
        }
        if (rival != null) {
            this.addToBot(new ApplyPowerAction(rival, rival, new RivalPosition(rival, 1)));
        }
        this.addToBot(new ApplyPowerAction(this, this, new RivalPosition(this, 1)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RivalPlayerPosition(AbstractDungeon.player, 1)));
        AbstractDungeon.player.drawX += 480.0F * Settings.scale;
        AbstractDungeon.player.dialogX += 480.0F * Settings.scale;
        this.animation.setFlip(true, false);
        this.halfDead = true;
    }
    
    @Override
    public void takeTurn() {
        this.halfDead = false;
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        AbstractCreature target = rival;

        if(info.base > -1) {
            info.applyPowers(this, target);
        }
        switch (this.nextMove) {
            case ATTACK: {
                //runAnim("Magic");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                counter--;
                break;
            }
            case LIFESTEAL_ATTACK: {
                //runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(target, info, AbstractGameAction.AttackEffect.POISON));
                counter--;
                break;
            }
            case DEFEND: {
                //runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, BLOCK));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Retribution(this)));
                counter = COOLDOWN;
                break;
            }
            case DEBUFF: {
                //runAnim("Magic");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new WeakPower(target, DEBUFF_AMT, true), DEBUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new VulnerablePower(target, DEBUFF_AMT, true), DEBUFF_AMT));
                counter--;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

//    public void rivalDefeated() {
//        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, RivalPosition.POWER_ID));
//        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, RivalPlayerPosition.POWER_ID));
//        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
//        AbstractDungeon.actionManager.addToBottom(new AnimatedMoveActualAction(this, this.drawX, this.drawY, originalX, originalY));
//        AbstractDungeon.actionManager.addToBottom(new SetFlipAction(this));
//        AbstractDungeon.onModifyPower();
//    }
//
//    public void setFlip(boolean horizontal, boolean vertical) {
//        this.animation.setFlip(horizontal, vertical);
//    }

    @Override
    protected void getMove(final int num) {
        if (counter == 0) {
            this.setMoveShortcut(DEFEND);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(LIFESTEAL_ATTACK) && !this.lastMoveBefore(LIFESTEAL_ATTACK)) {
                possibilities.add(LIFESTEAL_ATTACK);
            }
            if (!this.lastMove(DEBUFF) && !this.lastMoveBefore(DEBUFF)) {
                possibilities.add(DEBUFF);
            }
            if (!this.lastMove(ATTACK)) {
                possibilities.add(ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void applyPowers() {
        if (this.nextMove == -1 || rival.isDeadOrEscaped()) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            super.applyPowers();
            return;
        }
        AbstractCreature target = rival;
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if (target == rival) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                //PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                //intentTip.body = TEXT[2] + info.output + TEXT[3];
            }
        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
        applyPowers();
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        if (rival != null) {
            rival.rivalDefeated();
            if (rival.isDeadOrEscaped() || rival.isDying) {
                this.onBossVictoryLogic();
            }
        }
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

    public class RemiliaListener implements Player.PlayerListener {

        private Remilia character;

        public RemiliaListener(Remilia character) {
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