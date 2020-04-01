package Gensokyo.monsters.act1;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act1.Teleportation;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sumireko extends CustomMonster
{
    public static final String ID = "Gensokyo:Sumireko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte GUN = 0;
    private static final byte PYROKINESIS = 1;
    private static final byte BULLET_CANCEL = 2;
    private static final byte PSYCHOKINESIS = 3;
    private static final byte SUMMON = 4;
    private static final int GUN_DAMAGE = 14;
    private static final int GUN_ACT_DAMAGE_BONUS = 7;
    private static final int A3_GUN_DAMAGE = 15;
    private static final int PYROKINESIS_DAMAGE = 10;
    private static final int PYROKINESIS_ACT_DAMAGE_BONUS = 5;
    private static final int A3_PYROKINESIS_DAMAGE = 11;
    private static final int BURN_AMT = 1;
    private static final int A18_BURN_AMT = 2;
    private static final int BLOCK = 12;
    private static final int BLOCK_ACT_BONUS = 6;
    private static final int A8_BLOCK = 11;
    private static final int BUFFER_AMT = 1;
    private static final int DEBUFF_AMT = 1;
    private static final int HIGH_ACT_DEBUFF_AMT = 2;
    private static float actMultiplier = 0.0f;
    private static final float ACT_1_MULTIPLIER = 1.0f;
    private static final float ACT_2_MULTIPLIER = 1.5f;
    private static final float ACT_3_MULTIPLIER = 2.0f;
    private static final float ACT_4_MULTIPLIER = 2.5f;
    private static final float DOPPEL_MULTIPLIER = 0.75F;
    private static final int HP_MIN = 100;
    private static final int HP_MAX = 105;
    private static final int A8_HP_MIN = 103;
    private static final int A8_HP_MAX = 108;
    private int gunDamage;
    private int pyroDamage;
    private int burnAmt;
    private int block;
    private int buffer;
    private int debuffAmt;
    private float doppelMultiplier;
    private boolean isDoppel;
    private Map<Byte, EnemyMoveInfo> moves;

    public Sumireko() {
        this(0.0f, 0.0f, false);
    }

    public Sumireko(final float x, final float y, boolean isDoppel) {
        super(Sumireko.NAME, ID, HP_MAX, -5.0F, 0, 220.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sumireko/Spriter/SumirekoAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.isDoppel = isDoppel;
        if (this.isDoppel) {
            doppelMultiplier = DOPPEL_MULTIPLIER;
        } else {
            doppelMultiplier = 1.0F;
        }
        if (AbstractDungeon.actNum == 1) {
            actMultiplier = ACT_1_MULTIPLIER;
            this.debuffAmt = DEBUFF_AMT;
        } else if (AbstractDungeon.actNum == 2) {
            actMultiplier = ACT_2_MULTIPLIER;
            this.debuffAmt = HIGH_ACT_DEBUFF_AMT;
        } else if (AbstractDungeon.actNum == 3) {
            actMultiplier = ACT_3_MULTIPLIER;
            this.debuffAmt = HIGH_ACT_DEBUFF_AMT;
        } else {
            actMultiplier = ACT_4_MULTIPLIER;
            this.debuffAmt = HIGH_ACT_DEBUFF_AMT;
        }
        this.buffer = BUFFER_AMT;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.burnAmt = A18_BURN_AMT;
        } else {
            this.burnAmt = BURN_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp((int)(A8_HP_MIN * actMultiplier * doppelMultiplier), (int)(A8_HP_MAX * actMultiplier * doppelMultiplier));
            this.block = A8_BLOCK + (BLOCK_ACT_BONUS * (AbstractDungeon.actNum - 1));
        }
        else {
            this.setHp((int)(HP_MIN * actMultiplier * doppelMultiplier), (int)(HP_MAX * actMultiplier * doppelMultiplier));
            this.block = BLOCK + (BLOCK_ACT_BONUS * (AbstractDungeon.actNum - 1));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.gunDamage = A3_GUN_DAMAGE + (GUN_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.pyroDamage = A3_PYROKINESIS_DAMAGE + (PYROKINESIS_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        }
        else {
            this.gunDamage = GUN_DAMAGE + (GUN_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.pyroDamage = PYROKINESIS_DAMAGE + (PYROKINESIS_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        }

        this.moves = new HashMap<>();
        this.moves.put(GUN, new EnemyMoveInfo(GUN, Intent.ATTACK, this.gunDamage, 0, false));
        this.moves.put(PYROKINESIS, new EnemyMoveInfo(PYROKINESIS, Intent.ATTACK_DEBUFF, this.pyroDamage, 0, false));
        this.moves.put(BULLET_CANCEL, new EnemyMoveInfo(BULLET_CANCEL, Intent.DEFEND_BUFF, -1, 0, true));
        this.moves.put(PSYCHOKINESIS, new EnemyMoveInfo(PSYCHOKINESIS, Intent.DEBUFF, -1, 0, false));
        this.moves.put(SUMMON, new EnemyMoveInfo(SUMMON, Intent.UNKNOWN, -1, 0, false));

        Player.PlayerListener listener = new SumirekoListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("LastOccult");
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Teleportation(this, Teleportation.RIGHT, 0)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Teleportation(AbstractDungeon.player, Teleportation.MIDDLE, 0)));
        AbstractDungeon.player.drawX += Teleportation.movement;
    }
    
    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GUN: {
                runAnim("Gun");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
            case PYROKINESIS: {
                runAnim("PyroBlast");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), burnAmt));
                break;
            }
            case BULLET_CANCEL: {
                runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, buffer), buffer));
                break;
            }
            case PSYCHOKINESIS: {
                runAnim("Special");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                break;
            }
            case SUMMON: {
                runAnim("Special");
                Sumireko doppel = new Sumireko(0.0F, 0.0F, true);
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(doppel, true));
                int position;
                int amount2;
                if (this.hasPower(Teleportation.POWER_ID)) {
                    position = getPower(Teleportation.POWER_ID).amount;
                    amount2 = ((TwoAmountPower)getPower(Teleportation.POWER_ID)).amount2;
                } else {
                    position = 0;
                    amount2 = 0;
                }
                int spawnPosition;
                if (position == Teleportation.RIGHT) {
                    doppel.drawX = this.drawX - (Teleportation.movement * 2);
                    spawnPosition = Teleportation.LEFT;
                    doppel.setFlip(true, false);
                } else if (position == Teleportation.MIDDLE) {
                    doppel.drawX = this.drawX + Teleportation.movement;
                    spawnPosition = Teleportation.RIGHT;
                    doppel.setFlip(false, false);
                } else {
                    doppel.drawX = this.drawX + Teleportation.movement;
                    spawnPosition = Teleportation.MIDDLE;
                    doppel.setFlip(true, false);
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(doppel, doppel, new Teleportation(doppel, spawnPosition, amount2)));
                this.firstMove = false;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove && !isDoppel) {
            this.setMoveShortcut(SUMMON);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GUN)) {
                possibilities.add(GUN);
            }
            if (!this.lastMove(PYROKINESIS)) {
                possibilities.add(PYROKINESIS);
            }
            if (!isDoppel && !this.lastMove(BULLET_CANCEL)) {
                possibilities.add(BULLET_CANCEL);
            }
            if (isDoppel && !this.lastMove(PSYCHOKINESIS)) {
                possibilities.add(PSYCHOKINESIS);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Sumireko");
        NAME = Sumireko.monsterStrings.NAME;
        MOVES = Sumireko.monsterStrings.MOVES;
        DIALOG = Sumireko.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
        if (!isDoppel) {
            //kills off the doppel
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo instanceof Sumireko) {
                    if (!mo.isDead && !mo.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                    }
                }
            }
        }
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

    public class SumirekoListener implements Player.PlayerListener {

        private Sumireko character;

        public SumirekoListener(Sumireko character) {
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