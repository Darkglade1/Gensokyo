package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.act2.LunaticRedEyes;
import Gensokyo.powers.act2.ThingOfNightmares;
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
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reisen extends CustomMonster
{
    public static final String ID = "Gensokyo:Reisen";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte DEBUFF = 0;
    private static final byte ATTACK = 1;
    private static final byte DEBUFF_ATTACK = 2;
    private static final byte SUMMON = 3;
    private static final int NORMAL_ATTACK_DAMAGE = 19;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 21;
    private static final int DEBUFF_ATTACK_DAMAGE = 13;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 14;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int BLOCK = 6;
    private static final int A8_BLOCK = 7;
    private static final int MARK_AMOUNT = 3;
    private static final int INSANITY_AMT = 2;
    private static final int A18_INSANITY_AMT = 3;
    private static final int COOLDOWN = 2;
    private static final int HP_MIN = 135;
    private static final int HP_MAX = 141;
    private static final int A_2_HP_MIN = 139;
    private static final int A_2_HP_MAX = 147;
    private int normalDamage;
    private int debuffDamage;
    private int insanityAmt;
    private int block;
    private int turnCounter = 2;
    private Map<Byte, EnemyMoveInfo> moves;
    public AbstractMonster[] minions = new AbstractMonster[2];

    public Reisen() {
        this(0.0f, 0.0f);
    }

    public Reisen(final float x, final float y) {
        super(Reisen.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 265.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Reisen/Spriter/ReisenAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.insanityAmt = A18_INSANITY_AMT;
        } else {
            this.insanityAmt = INSANITY_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A3_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.DEFEND_DEBUFF, -1, 0, false));
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, this.normalDamage, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.debuffDamage, 0, false));
        this.moves.put(SUMMON, new EnemyMoveInfo(SUMMON, Intent.UNKNOWN, -1, 0, false));

        Player.PlayerListener listener = new ReisenListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Wind God Girl");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LunaticRedEyes(this, insanityAmt)));
        Summon();
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case DEBUFF: {
                //runAnim("Spellcard");
                if (this.hasPower(LunaticRedEyes.POWER_ID)) {
                    LunaticRedEyes eyes = (LunaticRedEyes)this.getPower(LunaticRedEyes.POWER_ID);
                    int marks = MARK_AMOUNT;
                    for (int i = AbstractDungeon.player.drawPile.group.size() - 1; i >= 0; i--) {
                        AbstractCard card = AbstractDungeon.player.drawPile.group.get(i);
                        if (!eyes.isCardMarked(card)) {
                            eyes.markedCards.add(card);
                            marks--;
                        }
                        if (marks <= 0) {
                            i = 0;
                        }
                    }
                }
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    this.addToBot(new GainBlockAction(mo, this, block));
                }
                turnCounter = 0;
                break;
            }
            case DEBUFF_ATTACK: {
                //runAnim("AttackDash");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                turnCounter++;
                break;
            }
            case ATTACK: {
                //runAnim("AttackFan");
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                turnCounter++;
                break;
            }
            case SUMMON: {
                //runAnim("AttackFan");
                Summon();
                turnCounter++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void Summon() {
        Kune minion2 = new Kune(-300.0F, 0.0F, this, 1);
        minions[1] = minion2;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion2, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion2, this, new ThingOfNightmares(minion2, this)));

        Kune minion1 = new Kune(-500.0F, 0.0F, this, 0);
        minions[0] = minion1;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion1, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion1, this, new ThingOfNightmares(minion1, this)));
    }

    @Override
    protected void getMove(final int num) {
        if (!firstMove && minions[0] == null && minions[1] == null) {
            this.setMoveShortcut(SUMMON);
        } else if (turnCounter >= COOLDOWN) {
            this.setMoveShortcut(DEBUFF);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(ATTACK)) {
                possibilities.add(ATTACK);
            }
            if (!this.lastMove(DEBUFF_ATTACK)) {
                possibilities.add(DEBUFF_ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Reisen");
        NAME = Reisen.monsterStrings.NAME;
        MOVES = Reisen.monsterStrings.MOVES;
        DIALOG = Reisen.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Kune) {
                if (!mo.isDead && !mo.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
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

    public class ReisenListener implements Player.PlayerListener {

        private Reisen character;

        public ReisenListener(Reisen character) {
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