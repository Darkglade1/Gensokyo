package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.powers.act3.AnimalFriend;
import Gensokyo.powers.act3.KasenMinon;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kasen extends CustomMonster
{
    public static final String ID = GensokyoMod.makeID("Kasen");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte MULTI_ATTACK = 0;
    private static final byte ATTACK_DEBUFF = 1;
    private static final byte DEFEND = 2;
    private static final byte SUMMON = 3;

    private static final int MULTI_ATTACK_DMG = 7;
    private static final int A3_MULTI_ATTACK_DMG = 8;
    private static final int MULTI_ATTACK_HITS = 2;
    private int multiAttackDmg;

    private static final int ATTACK_DEBUFF_DMG = 13;
    private static final int A3_ATTACK_DEBUFF_DMG = 14;
    private int attackDebuffDmg;

    private static final int DEBUFF_AMT = 1;

    private static final int BLOCK = 14;
    private static final int A8_BLOCK = 15;
    private int block;

    private static final int STRENGTH = 1;
    private static final int A18_STRENGTH = 2;
    private int strength;

    private static final int HP_LOSS = 50;

    private static final int HP = 300;
    private static final int A8_HP = 315;

    private boolean hardMinion1 = true;
    private boolean hardMinion2 = false;

    public AbstractMonster[] minions = new AbstractMonster[2];
    private Map<Byte, EnemyMoveInfo> moves;

    public Kasen() {
        this(-150.0f, 0.0f);
    }

    public Kasen(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kasen/Spriter/KasenAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(A8_HP);
            this.block = A8_BLOCK;
        } else {
            setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            multiAttackDmg = A3_MULTI_ATTACK_DMG;
            attackDebuffDmg = A3_ATTACK_DEBUFF_DMG;
        } else {
            multiAttackDmg = MULTI_ATTACK_DMG;
            attackDebuffDmg = ATTACK_DEBUFF_DMG;
        }

        this.moves = new HashMap<>();
        this.moves.put(MULTI_ATTACK, new EnemyMoveInfo(MULTI_ATTACK, Intent.ATTACK, multiAttackDmg, MULTI_ATTACK_HITS, true));
        this.moves.put(ATTACK_DEBUFF, new EnemyMoveInfo(ATTACK_DEBUFF, Intent.ATTACK_DEBUFF, attackDebuffDmg, 0, false));
        this.moves.put(DEFEND, new EnemyMoveInfo(DEFEND, Intent.DEFEND, -1, 0, false));
        this.moves.put(SUMMON, new EnemyMoveInfo(SUMMON, Intent.UNKNOWN, -1, 0, false));

        Player.PlayerListener listener = new KasenListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        //CustomDungeon.playTempMusicInstantly("MasterSpark");
        Summon();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AnimalFriend(this, strength, HP_LOSS)));
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
            case MULTI_ATTACK: {
                //runAnim("Spark");
                for (int i = 0; i < MULTI_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case ATTACK_DEBUFF: {
                //runAnim("Smack");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
            case DEFEND: {
                //runAnim("Special");
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped()) {
                        addToBot(new GainBlockAction(mo, block));
                    }
                }
                break;
            }
            case SUMMON: {
                //runAnim("Smack");
                Summon();
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    private void Summon() {
        float xPosition1 = -450.0F;
        float xPosition2 = 150f;

        AbstractMonster minion1;
        if (hardMinion1) {
            minion1 = new Kume(xPosition1, 0.0f);
        } else {
            minion1 = new Byrd(xPosition1, 0.0f);
        }
        hardMinion1 = !hardMinion1;
        minions[0] = minion1;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion1, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(minion1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion1, minion1, new KasenMinon(minion1, this)));

        AbstractMonster minion2;
        if (hardMinion2) {
            minion2 = new Snecko(xPosition2, 0.0f);
            int newHP = minion2.maxHealth / 2;
            minion2.maxHealth = newHP;
            minion2.currentHealth = newHP;
            ReflectionHacks.setPrivate(minion2, Snecko.class, "firstTurn", false);
        } else {
            minion2 = new JawWorm(xPosition2, 0.0f, true);
        }
        hardMinion2 = !hardMinion2;
        minions[1] = minion2;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion2, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(minion2));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion2, minion2, new KasenMinon(minion2, this)));
    }

    @Override
    protected void getMove(final int num) {
        if (minions[0] == null && minions[1] == null && !firstMove) {
            setMoveShortcut(SUMMON);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(MULTI_ATTACK)) {
                possibilities.add(MULTI_ATTACK);
            }
            if (!this.lastMove(ATTACK_DEBUFF) && !this.lastMoveBefore(ATTACK_DEBUFF)) {
                possibilities.add(ATTACK_DEBUFF);
            }
            if (!this.lastMove(DEFEND) && !this.lastMoveBefore(DEFEND)) {
                possibilities.add(DEFEND);
            }
            setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped() && mo.hasPower(KasenMinon.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
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

    public class KasenListener implements Player.PlayerListener {

        private Kasen character;

        public KasenListener(Kasen character) {
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