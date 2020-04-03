package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.actions.AnimatedMoveActualAction;
import Gensokyo.actions.SetFlipAction;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act2.Purity;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.RivalPosition;
import Gensokyo.powers.act2.TenDesires;
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
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Byakuren extends CustomMonster
{
    public static final String ID = "Gensokyo:Byakuren";
    private static final MonsterStrings monsterStrings;
    private static final UIStrings uiStrings;
    private static final String[] TEXT;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte BUFF = 1;
    private static final byte BUFF_BLOCK = 2;
    private static final byte AOE_ATTACK = 3;

    private static final int NORMAL_ATTACK_DAMAGE = 36;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 40;
    private int normalDamage;

    private static final int AOE_DAMAGE = 24;
    private static final int A4_AOE_DAMAGE = 26;
    private int aoeDamage;

    private static final int STRENGTH = 8;
    private static final int A19_STRENGTH = 10;
    private int strength;

    private static final int BLOCK = 15;
    private static final int A8_BLOCK = 18;
    private int block;

    private static final int BUFF_AMT = 3;
    private static final int DEBUFF_AMT = 1;

    private static final int AOE_COOLDOWN = 3;
    private int counter = -2; //Delay this by 2 turns the first time

    private static final int HP = 400;
    private static final int A9_HP = 420;

    private float particleTimer;
    private float particleTimer2;

    public float originalX;
    public float originalY;

    private Miko rival;

    private Map<Byte, EnemyMoveInfo> moves;

    public Byakuren() {
        this(0.0f, 0.0f);
    }

    public Byakuren(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, -20.0F, 230.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Byakuren/Spriter/ByakurenAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strength = A19_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
            this.aoeDamage = A4_AOE_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.aoeDamage = AOE_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));
        this.moves.put(BUFF_BLOCK, new EnemyMoveInfo(BUFF_BLOCK, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(AOE_ATTACK, new EnemyMoveInfo(AOE_ATTACK, IntentEnums.ATTACK_AREA, this.aoeDamage, 0, false));

        Player.PlayerListener listener = new ByakurenListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            AbstractDungeon.getCurrRoom().playBgmInstantly("CosmicMind");
        } else {
            AbstractDungeon.getCurrRoom().playBgmInstantly("TrueAdmin");
        }
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Miko) {
                rival = (Miko)mo;
            }
        }
        if (rival != null) {
            this.addToBot(new ApplyPowerAction(rival, rival, new RivalPosition(rival, 1)));
            this.addToBot(new ApplyPowerAction(rival, rival, new TenDesires(rival)));
        }
        this.addToBot(new ApplyPowerAction(this, this, new RivalPosition(this, 1)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RivalPlayerPosition(AbstractDungeon.player, 1)));
        this.addToBot(new ApplyPowerAction(this, this, new Purity(this)));
        AbstractDungeon.player.drawX += 480.0F * Settings.scale;
        AbstractDungeon.player.dialogX += 480.0F * Settings.scale;
        this.animation.setFlip(true, false);
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        AbstractCreature target;
        if (!rival.isDeadOrEscaped()) {
            target = rival;
            if (AbstractDungeon.player.hasPower(RivalPlayerPosition.POWER_ID)) {
                if (((RivalPlayerPosition)AbstractDungeon.player.getPower(RivalPlayerPosition.POWER_ID)).isInUnsafeLane()) {
                    target = AbstractDungeon.player;
                }
            }
        } else {
            target = AbstractDungeon.player;
        }

        if(info.base > -1) {
            info.applyPowers(this, target);
        }
        switch (this.nextMove) {
            case ATTACK: {
                runAnim("Magic");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
                counter++;
                break;
            }
            case BUFF: {
                runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                counter++;
                break;
            }
            case BUFF_BLOCK: {
                runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, BUFF_AMT, true), BUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, block));
                counter++;
                break;
            }
            case AOE_ATTACK: {
                runAnim("Magic");
                DamageInfo playerInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                playerInfo.applyPowers(this, AbstractDungeon.player);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, playerInfo, AbstractGameAction.AttackEffect.NONE));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (mo != this) {
                        DamageInfo monsterInfo = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                        monsterInfo.applyPowers(this, mo);
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(mo.drawX, mo.drawY)));
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, monsterInfo, AbstractGameAction.AttackEffect.NONE));
                    }
                }
                counter = 0;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void rivalDefeated() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, RivalPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, RivalPlayerPosition.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
        AbstractDungeon.actionManager.addToBottom(new AnimatedMoveActualAction(this, this.drawX, this.drawY, originalX, originalY));
        AbstractDungeon.actionManager.addToBottom(new SetFlipAction(this));
        AbstractDungeon.onModifyPower();
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || !rival.isDeadOrEscaped()) {
            if (this.counter >= AOE_COOLDOWN) {
                this.setMoveShortcut(AOE_ATTACK);
            } else {
                ArrayList<Byte> possibilities = new ArrayList<>();
                if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
                    possibilities.add(BUFF);
                }
                if (!this.lastMove(BUFF_BLOCK) && !this.lastMoveBefore(BUFF_BLOCK)) {
                    possibilities.add(BUFF_BLOCK);
                }
                if (!this.lastMove(ATTACK)) {
                    possibilities.add(ATTACK);
                }
                this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
            }
        } else {
            if (!this.lastMove(BUFF_BLOCK) && !this.lastMoveBefore(BUFF_BLOCK)) {
                this.setMoveShortcut(BUFF_BLOCK);
            } else {
                this.setMoveShortcut(ATTACK);
            }
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void applyPowers() {
        if (this.nextMove == -1 || this.intent == IntentEnums.ATTACK_AREA) {
            super.applyPowers();
            return;
        }
        AbstractCreature target;
        if (!rival.isDeadOrEscaped()) {
            target = rival;
            if (AbstractDungeon.player.hasPower(RivalPlayerPosition.POWER_ID)) {
                if (((RivalPlayerPosition)AbstractDungeon.player.getPower(RivalPlayerPosition.POWER_ID)).isInUnsafeLane()) {
                    target = AbstractDungeon.player;
                }
            }
        } else {
            target = AbstractDungeon.player;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if (target == rival) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[2] + info.output + TEXT[3];
            }
        } else {
            super.applyPowers();
            if (info.base == -1) {
                Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            } else if(info.base > -1 && !rival.isDeadOrEscaped()) {
                info.applyPowers(this, target);
                Color color = new Color(0.5F, 0.0F, 1.0F, 0.5F);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[0] + info.output + TEXT[1];
            }
        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
        applyPowers();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hasPower(Purity.POWER_ID)) {
            if (this.getPower(Purity.POWER_ID).amount == Purity.THRESHOLD) {
                if (!Settings.DISABLE_EFFECTS) {
                    this.particleTimer -= Gdx.graphics.getDeltaTime();
                    if (this.particleTimer < 0.0F) {
                        this.particleTimer = 0.04F;
                        AbstractDungeon.effectsQueue.add(new FlexibleCalmParticleEffect(this));
                    }
                }

                this.particleTimer2 -= Gdx.graphics.getDeltaTime();
                if (this.particleTimer2 < 0.0F) {
                    this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
                    AbstractDungeon.effectsQueue.add(new FlexibleStanceAuraEffect(CalmStance.STANCE_ID, this));
                }
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Byakuren");
        uiStrings = CardCrawlGame.languagePack.getUIString("Gensokyo:RivalIntents");
        TEXT = uiStrings.TEXT;
        NAME = Byakuren.monsterStrings.NAME;
        MOVES = Byakuren.monsterStrings.MOVES;
        DIALOG = Byakuren.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
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

    public class ByakurenListener implements Player.PlayerListener {

        private Byakuren character;

        public ByakurenListener(Byakuren character) {
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