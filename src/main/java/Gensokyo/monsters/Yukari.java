package Gensokyo.monsters;

import Gensokyo.vfx.EmptyEffect;
import Gensokyo.vfx.TrainEffect;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Yukari extends CustomMonster
{
    public static final String ID = "Gensokyo:Yukari";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private boolean secondMove = true;
    private static final byte OPENING = 1;
    private static final byte STRENGTH_DRAIN = 2;
    private static final byte MEGA_DEBUFF = 3;
    private static final byte ATTACK = 4;
    private static final byte LAST_WORD = 5;
    private static final int NORMAL_ATTACK_DAMAGE = 8;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 9;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int TRAIN_ATTACK_DAMAGE = 7;
    private static final int A4_TRAIN_ATTACK_DAMAGE = 7;
    private static final int TRAIN_ATTACK_HITS = 3;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int A19_DEBUFF_AMOUNT = 3;
    private static final int STRENGTH_DRAIN_AMOUNT = 2;
    private static final int A19_STRENGTH_DRAIN_AMOUNT = 3;
    private int normalDamage;
    private int trainDamage;
    private int debuffAmount;
    private int strengthDrain;
    private boolean useTrain = false;
    private static final int HP = 220;
    public static final int A9_HP = 230;

    public Yukari() {
        this(0.0f, 0.0f);
    }

    public Yukari(final float x, final float y) {
        super(Yukari.NAME, ID, HP, -5.0F, 0, 280.0f, 255.0f, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/Komachi/Spriter/YukariAnimations.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmount = A19_DEBUFF_AMOUNT;
            this.strengthDrain = A19_STRENGTH_DRAIN_AMOUNT;
        } else {
            this.debuffAmount = DEBUFF_AMOUNT;
            this.strengthDrain = STRENGTH_DRAIN_AMOUNT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
            this.trainDamage = A4_TRAIN_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.trainDamage = TRAIN_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.trainDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case OPENING: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Yukari.DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case STRENGTH_DRAIN: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case MEGA_DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                break;
            }
            case ATTACK: {
                if (this.useTrain) {
                    for (int i = 0; i < TRAIN_ATTACK_HITS; i++) {
                        if (i == 0) {
                            AbstractDungeon.actionManager.addToBottom(new SFXAction("Gensokyo:Train"));
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new TrainEffect(), 0.5F));
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.6F));
                        }
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                } else {
                    for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    }
                }
                break;
            }
            case LAST_WORD: {
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(Yukari.MOVES[0], OPENING, Intent.UNKNOWN);
            this.firstMove = false;
        }  else if (this.currentHealth < this.maxHealth / 2 && !this.useTrain) {
            this.useTrain = true;
            this.setMove(LAST_WORD, Intent.BUFF);
        } else if (this.lastMove(LAST_WORD)) {
            this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK);
        } else if (this.secondMove){
            this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK);
            this.secondMove = false;
        } else {
            if (num < 25) {
                if (!this.lastMove(STRENGTH_DRAIN)) {
                    this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.DEBUFF);
                } else {
                    this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK);
                }
            } else if (num < 50) {
                if (!this.lastMove(STRENGTH_DRAIN)) {
                    this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.STRONG_DEBUFF);
                } else {
                    this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK);
                }
            } else {
                if (!this.lastTwoMoves(ATTACK)) {
                    this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK);
                } else {
                    this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.DEBUFF);
                }
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yukari");
        NAME = Yukari.monsterStrings.NAME;
        MOVES = Yukari.monsterStrings.MOVES;
        DIALOG = Yukari.monsterStrings.DIALOG;
    }
}