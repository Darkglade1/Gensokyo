package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.TakeSecondTurnAction;
import Gensokyo.powers.act2.Weather;
import Gensokyo.util.PreviewIntent;
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
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tenshi extends CustomMonster
{
    public static final String ID = "Gensokyo:Tenshi";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;

    public static final int WEATHER_1 = 0;
    public static final int WEATHER_2 = 1;
    public static final int WEATHER_3 = 2;
    public int weather = WEATHER_1;

    private static final byte ATTACK = 0;
    private static final byte BLOCK_ATTACK = 1;
    private static final byte BUFF = 2;

    private static final int NORMAL_ATTACK_DAMAGE = 13;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 14;
    private static final int HITS = 2;

    private static final int BLOCK_ATTACK_DAMAGE = 14;
    private static final int A3_BLOCK_ATTACK_DAMAGE = 15;

    private static final int STRENGTH = 2;
    private static final int A18_STRENGTH = 3;

    private static final int BLOCK = 10;
    private static final int A8_BLOCK = 11;

    private static final int SELF_DEBUFF = 2;
    private static final float WEATHER_THRESHOLD = 0.34F;

    private static final int HP_MIN = 150;
    private static final int HP_MAX = 152;
    private static final int A8_HP_MIN = 156;
    private static final int A8_HP_MAX = 160;
    private int normalDamage;
    private int blockDamage;
    private int block;
    private int strength;
    private EnemyMoveInfo secondMove;
    private PreviewIntent secondIntent;

    private Map<Byte, EnemyMoveInfo> moves;

    public Tenshi() {
        this(0.0f, 0.0f);
    }

    public Tenshi(final float x, final float y) {
        super(Tenshi.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Tenshi/Spriter/TenshiAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            strength = A18_STRENGTH;
        } else {
            strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
            this.blockDamage = A3_BLOCK_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.blockDamage = BLOCK_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, HITS, true));
        this.moves.put(BLOCK_ATTACK, new EnemyMoveInfo(BLOCK_ATTACK, Intent.ATTACK_DEFEND, this.blockDamage, 0, false));
        this.moves.put(BUFF, new EnemyMoveInfo(BUFF, Intent.BUFF, -1, 0, false));

        Player.PlayerListener listener = new TenshiListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Hartmann");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Weather(this, (int)(maxHealth * WEATHER_THRESHOLD), this)));
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
                //runAnim("magicAttackForward");
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, SELF_DEBUFF, true), SELF_DEBUFF));
                break;
            }
            case BLOCK_ATTACK: {
                //runAnim("occultAttack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
                break;
            }
            case BUFF: {
                //runAnim("spellA");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                break;
            }
        }
        if (weather == WEATHER_1) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.8F));
            AbstractDungeon.actionManager.addToBottom(new TakeSecondTurnAction(this));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void takeSecondTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.secondMove.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.secondMove.nextMove) {
            case ATTACK: {
                //runAnim("magicAttackForward");
                for (int i = 0; i < HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, SELF_DEBUFF, true), SELF_DEBUFF));
                break;
            }
            case BLOCK_ATTACK: {
                //runAnim("occultAttack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
                break;
            }
            case BUFF: {
                //runAnim("spellA");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                break;
            }
        }
    }

    @Override
    protected void getMove(final int num) {
        if (weather == WEATHER_1 || weather == WEATHER_2) {
            if (this.lastMove(BLOCK_ATTACK)) {
                this.setMoveShortcut(ATTACK);
            } else {
                this.setMoveShortcut(BLOCK_ATTACK);
            }
        }
        if (weather == WEATHER_3) {
            this.setMoveShortcut(ATTACK);
        }
    }

    protected void getSecondMove(final int num) {
        if (this.nextMove == BLOCK_ATTACK) {
            this.setSecondMoveShortcut(BUFF);
        } else {
            this.setSecondMoveShortcut(ATTACK);
        }
    }

    @Override
    public void rollMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
        if (weather == WEATHER_1) {
            this.getSecondMove(AbstractDungeon.aiRng.random(99));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    private void setSecondMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        secondMove = new EnemyMoveInfo(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        secondIntent = new PreviewIntent(this, secondMove);
        applySecondPowers();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        applySecondPowers();
    }

    private void applySecondPowers() {
        if (this.secondMove.baseDamage > -1) {
            AbstractPlayer target = AbstractDungeon.player;
            int dmg = secondMove.baseDamage;
            float tmp = (float)dmg;
            if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                tmp *= mod;
            }

            AbstractPower p;
            Iterator var6;
            for(var6 = this.powers.iterator(); var6.hasNext(); tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL)) {
                p = (AbstractPower)var6.next();
            }

            for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL)) {
                p = (AbstractPower)var6.next();
            }

            tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);

            for(var6 = this.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL)) {
                p = (AbstractPower)var6.next();
            }

            for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL)) {
                p = (AbstractPower)var6.next();
            }

            dmg = MathUtils.floor(tmp);
            if (dmg < 0) {
                dmg = 0;
            }
            this.secondIntent.updateDamage(dmg);
        }
    }

    @Override
    public void renderIntent(SpriteBatch sb) {
        super.renderIntent(sb);
        if (secondIntent != null && weather == WEATHER_1) {
            secondIntent.update();
            secondIntent.render(sb);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Tenshi");
        NAME = Tenshi.monsterStrings.NAME;
        MOVES = Tenshi.monsterStrings.MOVES;
        DIALOG = Tenshi.monsterStrings.DIALOG;
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

    public class TenshiListener implements Player.PlayerListener {

        private Tenshi character;

        public TenshiListener(Tenshi character) {
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