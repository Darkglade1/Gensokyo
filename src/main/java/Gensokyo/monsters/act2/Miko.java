package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act2.Counter;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.WishfulSoul;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Miko extends CustomMonster
{
    public static final String ID = "Gensokyo:Miko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte STRONG_DEBUFF = 1;
    private static final byte DEFEND_DEBUFF = 2;
    private static final byte AOE_ATTACK = 3;
    private static final byte DEBUFF_ATTACK = 4;

    private static final int NORMAL_ATTACK_DAMAGE = 12;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 13;
    private static final int NORMAL_ATTACK_HITS = 2;
    private int normalDamage;

    private static final int AOE_DAMAGE = 8;
    private static final int A4_AOE_DAMAGE = 9;
    private static final int AOE_HITS = 2;
    private int aoeDamage;

    private static final int DEBUFF_ATTACK_DAMAGE = 16;
    private static final int A4_DEBUFF_ATTACK_DAMAGE = 18;
    private static final int DEBUFF_AMOUNT = 1;
    private int debuffDamage;

    private static final float HEALING = 0.10F;
    private static final float A9_HEALING = 0.12F;
    private float healing;

    private static final int STRENGTH_STEAL = 3;
    private static final int A19_STRENGTH_STEAL = 4;
    private int strengthSteal;

    private static final int COOLDOWN = 2;
    private int counter;

    private static final int HP = 250;
    private static final int A9_HP = 265;

    private Byakuren rival;

    private Map<Byte, EnemyMoveInfo> moves;

    public Miko() {
        this(0.0f, 0.0f);
    }

    public Miko(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, -20.0F, 230.0f, 275.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Miko/Spriter/MikoAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strengthSteal = A19_STRENGTH_STEAL;
        } else {
            this.strengthSteal = STRENGTH_STEAL;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.healing = A9_HEALING;
        } else {
            this.setHp(HP);
            this.healing = HEALING;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A4_DEBUFF_ATTACK_DAMAGE;
            this.aoeDamage = A4_AOE_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.aoeDamage = AOE_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, NORMAL_ATTACK_HITS, true));
        this.moves.put(STRONG_DEBUFF, new EnemyMoveInfo(STRONG_DEBUFF, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(DEFEND_DEBUFF, new EnemyMoveInfo(DEFEND_DEBUFF, Intent.DEFEND_DEBUFF, -1, 0, false));
        this.moves.put(AOE_ATTACK, new EnemyMoveInfo(AOE_ATTACK, Intent.ATTACK, this.aoeDamage, AOE_HITS, true));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.debuffDamage, 0, false));

        Player.PlayerListener listener = new ByakurenListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Byakuren) {
                rival = (Byakuren) mo;
            }
        }
        this.animation.setFlip(true, false);
        this.dialogX -= 960.0F * Settings.scale;
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
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                counter++;
               break;
            }
            case STRONG_DEBUFF: {
                this.addToBot(new ApplyPowerAction(target, this, new Counter(target), 1));
                counter++;
                break;
            }
            case DEFEND_DEBUFF: {
                this.addToBot(new HealAction(this, this, (int)(this.maxHealth * healing)));
                this.addToBot(new ApplyPowerAction(target, this, new WishfulSoul(target, this, strengthSteal), strengthSteal));
                counter++;
                break;
            }
            case AOE_ATTACK: {
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (mo != this) {
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                    }
                }
                counter = 0;
                break;
            }
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new VulnerablePower(target, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this, new FrailPower(target, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.counter >= COOLDOWN) {
            this.setMoveShortcut(AOE_ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(STRONG_DEBUFF)) {
                possibilities.add(STRONG_DEBUFF);
            }
            if (!this.lastMove(DEFEND_DEBUFF) && rival.hasPower(StrengthPower.POWER_ID)) {
                possibilities.add(DEFEND_DEBUFF);
            }
            if (!this.lastTwoMoves(ATTACK)) {
                possibilities.add(ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Miko");
        NAME = Miko.monsterStrings.NAME;
        MOVES = Miko.monsterStrings.MOVES;
        DIALOG = Miko.monsterStrings.DIALOG;
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

    public class ByakurenListener implements Player.PlayerListener {

        private Miko character;

        public ByakurenListener(Miko character) {
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