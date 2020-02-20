package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act2.Purity;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.RivalPosition;
import Gensokyo.powers.act2.TenDesires;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Byakuren extends CustomMonster
{
    public static final String ID = "Gensokyo:Byakuren";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte BUFF = 1;
    private static final byte BUFF_BLOCK = 2;
    private static final byte AOE_ATTACK = 3;

    private static final int NORMAL_ATTACK_DAMAGE = 30;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 33;
    private int normalDamage;

    private static final int AOE_DAMAGE = 20;
    private static final int A4_AOE_DAMAGE = 22;
    private int aoeDamage;

    private static final int STRENGTH = 6;
    private static final int A19_STRENGTH = 8;
    private int strength;

    private static final int BLOCK = 20;
    private static final int A8_BLOCK = 22;
    private int block;

    private static final int BUFF_AMT = 3;
    private static final int DEBUFF_AMT = 1;

    private static final int AOE_COOLDOWN = 3;
    private int counter;

    private static final int HP = 500;
    private static final int A9_HP = 530;

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
        this.moves.put(AOE_ATTACK, new EnemyMoveInfo(AOE_ATTACK, Intent.ATTACK, this.aoeDamage, 0, false));

        Player.PlayerListener listener = new ByakurenListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Wind God Girl");
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
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
    }
    
    @Override
    public void takeTurn() {
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, -DEBUFF_AMT), -DEBUFF_AMT));
                counter++;
                break;
            }
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, DEBUFF_AMT, true), DEBUFF_AMT));
                counter++;
                break;
            }
            case BUFF_BLOCK: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, BUFF_AMT, true), BUFF_AMT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
                counter++;
                break;
            }
            case AOE_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (mo != this) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                }
                counter = 0;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.counter >= AOE_COOLDOWN) {
            this.setMoveShortcut(AOE_ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(BUFF)) {
                possibilities.add(BUFF);
            }
            if (!this.lastMove(BUFF_BLOCK) && !this.lastMoveBefore(BUFF_BLOCK)) {
                possibilities.add(BUFF_BLOCK);
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
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Byakuren");
        NAME = Byakuren.monsterStrings.NAME;
        MOVES = Byakuren.monsterStrings.MOVES;
        DIALOG = Byakuren.monsterStrings.DIALOG;
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