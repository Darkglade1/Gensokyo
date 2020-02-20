package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.Evasive;
import Gensokyo.powers.act2.RivalPlayerPosition;
import Gensokyo.powers.act2.RivalPosition;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

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
    private static final byte BUFF = 1;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 3;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 7;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 8;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 9;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int BLOCK = 9;
    private static final int A8_BLOCK = 10;
    private static final int STRENGTH = 3;
    private static final int A18_STRENGTH = 4;
    private static final int HP_MIN = 70;
    private static final int HP_MAX = 72;
    private static final int A_2_HP_MIN = 72;
    private static final int A_2_HP_MAX = 74;
    private int normalDamage;
    private int debuffDamage;
    private int block;
    private int strength;

    private Miko rival;

    private Map<Byte, EnemyMoveInfo> moves;

    public Byakuren() {
        this(0.0f, 0.0f);
    }

    public Byakuren(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, -20.0F, 230.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Byakuren/Spriter/ByakurenAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A3_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, 0, false));

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
        }
        this.addToBot(new ApplyPowerAction(this, this, new RivalPosition(this, 1)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RivalPlayerPosition(AbstractDungeon.player, 1)));
        AbstractDungeon.player.drawX += 480.0F * Settings.scale;
        AbstractDungeon.player.dialogX += 480.0F * Settings.scale;
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        this.setMoveShortcut(ATTACK);
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