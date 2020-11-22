package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.Retribution;
import Gensokyo.powers.act3.SistersPlayerPosition;
import Gensokyo.powers.act3.SistersPosition;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
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
import com.megacrit.cardcrawl.powers.VulnerablePower;

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
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GensokyoMod.makeID("SistersIntents"));
    private static final String[] TEXT = uiStrings.TEXT;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte HEAL = 2;

    private static final int NORMAL_ATTACK_DAMAGE = 25;

    private static final int DEBUFF_ATTACK_DAMAGE = 15;

    private static final int HEAL_AMT = 10;

    private static final int DEBUFF_AMT = 1;

    private static final int HP = 150;

    private static final int COOLDOWN = 2;
    private int counter = COOLDOWN;

    public float originalX;
    public float originalY;

    private Flandre sister;

    private Map<Byte, EnemyMoveInfo> moves;

    public Remilia() {
        this(0.0f, 0.0f);
    }

    public Remilia(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0.0F, 230.0f, 205.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Remilia/Spriter/RemiliaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, NORMAL_ATTACK_DAMAGE, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, DEBUFF_ATTACK_DAMAGE, 0, false));
        this.moves.put(HEAL, new EnemyMoveInfo(HEAL, Intent.BUFF, -1, 0, false));

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
                sister = (Flandre)mo;
            }
        }
        if (sister != null) {
            this.addToBot(new ApplyPowerAction(sister, sister, new SistersPosition(sister, 1, false)));
        }
        this.addToBot(new ApplyPowerAction(this, this, new SistersPosition(this, 1, true)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SistersPlayerPosition(AbstractDungeon.player, 1)));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                halfDead = true;
                healthBarUpdatedEvent();
                this.isDone = true;
            }
        });
    }
    
    @Override
    public void takeTurn() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                halfDead = false;
                healthBarUpdatedEvent();
                this.isDone = true;
            }
        });
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        AbstractCreature target = sister;

        if(info.base > -1) {
            info.applyPowers(this, target);
        }
        switch (this.nextMove) {
            case ATTACK: {
                runAnim("Attack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                counter--;
                break;
            }
            case DEBUFF_ATTACK: {
                runAnim("Attack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new VulnerablePower(target, DEBUFF_AMT, true), DEBUFF_AMT));
                counter--;
                break;
            }
            case HEAL: {
                runAnim("Spell");
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, HEAL_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Retribution(this)));
                counter = COOLDOWN;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void sisterDefeated() {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
        AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (counter == 0) {
            this.setMoveShortcut(HEAL);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(DEBUFF_ATTACK)) {
                possibilities.add(DEBUFF_ATTACK);
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
        if (this.nextMove == -1 || sister.isDeadOrEscaped()) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            super.applyPowers();
            return;
        }
        AbstractCreature target = sister;
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if (target == sister) {
            Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentColor", color);
            if(info.base > -1) {
                info.applyPowers(this, target);
                ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", info.output);
                PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
                intentTip.body = TEXT[10] + info.output + TEXT[11];
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
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        if (sister != null) {
            sister.sisterDefeated();
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