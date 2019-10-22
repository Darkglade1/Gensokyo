package Gensokyo.monsters;

import Gensokyo.actions.InvertPowersAction;
import Gensokyo.powers.UnstableBoundariesPower;
import Gensokyo.vfx.EmptyEffect;
import Gensokyo.vfx.YukariTrainEffect;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Kokoro extends CustomMonster
{
    public static final String ID = "Gensokyo:Kokoro";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int mask = 0;
    private static final int FOX_MASK = 0;
    private static final int SPIDER_MASK = 1;
    private static final int HOPE_MASK = 2;
    private static final int DEMON_MASK = 3;
    private static final int LION_MASK = 4;
    private static final int NUM_MASKS = 5;
    private static final byte MASK_MOVE_1 = 0;
    private static final byte MASK_MOVE_2 = 1;
    private static final byte MASK_CHANGE = 2;
    private static final int HOPE_HEAL = 15;
    private static final int A19_HOPE_HEAL = 18;
    private static final int DEMON_STRENGTH = 2;
    private static final int A19_DEMON_STRENGTH = 3;
    private static final int LION_DAMAGE = 2;
    private static final int A19_LION_DAMAGE = 3;
    private static final int MASK_STRENGTH_BUFF = 2;
    private static final int BLOCK = 8;
    private static final int A9_BLOCK = 10;
    private static final int FOX_ATTACK_DAMAGE = 8;
    private static final int A4_FOX_ATTACK_DAMAGE = 9;
    private static final int FOX_ATTACK_HITS = 2;
    private static final int FOX_DEBUFF_ATTACK_DAMAGE = 8;
    private static final int A4_FOX_DEBUFF_ATTACK_DAMAGE = 9;
    private static final int FOX_WOUND_COUNT = 1;
    private static final int A19_FOX_WOUND_COUNT = 2;
    private static final int SPIDER_ATTACK_DAMAGE = 6;
    private static final int A4_SPIDER_ATTACK_DAMAGE = 7;
    private static final int SPIDER_WEAK = 3;
    private static final int SPIDER_LIFE_STEAL_ATTACK_DAMAGE = 12;
    private static final int A4_SPIDER_LIFE_STEAL_ATTACK_DAMAGE = 14;
    private static final int HOPE_WEAK = 2;
    private static final int HOPE_ATTACK_DAMAGE = 9;
    private static final int A4_HOPE_ATTACK_DAMAGE = 11;
    private static final int DEMON_ATTACK_DAMAGE_1 = 8;
    private static final int A4_DEMON_ATTACK_DAMAGE_1 = 9;
    private static final int DEMON_FRAIL = 3;
    private static final int DEMON_ATTACK_DAMAGE_2 = 7;
    private static final int A4_DEMON_ATTACK_DAMAGE_2 = 8;
    private static final int DEMON_VULNERABLE = 3;
    private static final int LION_ATTACK_DAMAGE_1 = 20;
    private static final int A4_LION_ATTACK_DAMAGE_1 = 22;
    private static final int LION_ATTACK_DAMAGE_2 = 4;
    private static final int A4_LION_ATTACK_DAMAGE_2 = 5;
    private static final int LION_ATTACK_2_HITS = 3;
    private int hopeHeal;
    private int demonStrength;
    private int lionDamage;
    private int foxMultiDamage;
    private int foxDebuffDamage;
    private int wound;
    private int spiderDamage;
    private int spiderLifesteal;
    private int hopeDamage;
    private int demonDamage1;
    private int demonDamage2;
    private int lionDamage1;
    private int lionDamage2;
    private int block;
    private static final int HP = 230;
    private static final int A9_HP = 240;

    public Kokoro() {
        this(0.0f, 0.0f);
    }

    public Kokoro(final float x, final float y) {
        super(Kokoro.NAME, ID, HP, -5.0F, 0, 280.0f, 285.0f, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/Yukari/Spriter/YukariAnimations.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.hopeHeal = A19_HOPE_HEAL;
            this.demonStrength = A19_DEMON_STRENGTH;
            this.lionDamage = A19_LION_DAMAGE;
            this.wound = A19_FOX_WOUND_COUNT;
        } else {
            this.hopeHeal = HOPE_HEAL;
            this.demonStrength = DEMON_STRENGTH;
            this.lionDamage = LION_DAMAGE;
            this.wound = FOX_WOUND_COUNT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.foxMultiDamage = A4_FOX_ATTACK_DAMAGE;
            this.foxDebuffDamage = A4_FOX_DEBUFF_ATTACK_DAMAGE;
            this.spiderDamage = A4_SPIDER_ATTACK_DAMAGE;
            this.spiderLifesteal = A4_SPIDER_LIFE_STEAL_ATTACK_DAMAGE;
            this.hopeDamage = A4_HOPE_ATTACK_DAMAGE;
            this.demonDamage1 = A4_DEMON_ATTACK_DAMAGE_1;
            this.demonDamage2 = A4_DEMON_ATTACK_DAMAGE_2;
            this.lionDamage1 = A4_LION_ATTACK_DAMAGE_1;
            this.lionDamage2 = A4_LION_ATTACK_DAMAGE_2;
        } else {
            this.foxMultiDamage = FOX_ATTACK_DAMAGE;
            this.foxDebuffDamage = FOX_DEBUFF_ATTACK_DAMAGE;
            this.spiderDamage = SPIDER_ATTACK_DAMAGE;
            this.spiderLifesteal = SPIDER_LIFE_STEAL_ATTACK_DAMAGE;
            this.hopeDamage = HOPE_ATTACK_DAMAGE;
            this.demonDamage1 = DEMON_ATTACK_DAMAGE_1;
            this.demonDamage2 = DEMON_ATTACK_DAMAGE_2;
            this.lionDamage1 = LION_ATTACK_DAMAGE_1;
            this.lionDamage2 = LION_ATTACK_DAMAGE_2;
        }
        this.damage.add(new DamageInfo(this, this.foxMultiDamage));
        this.damage.add(new DamageInfo(this, this.foxDebuffDamage));
        this.damage.add(new DamageInfo(this, this.spiderDamage));
        this.damage.add(new DamageInfo(this, this.spiderLifesteal));
        this.damage.add(new DamageInfo(this, this.hopeDamage));
        this.damage.add(new DamageInfo(this, this.demonDamage1));
        this.damage.add(new DamageInfo(this, this.demonDamage2));
        this.damage.add(new DamageInfo(this, this.lionDamage1));
        this.damage.add(new DamageInfo(this, this.lionDamage2));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Gensokyo/Necrofantasia.mp3");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstableBoundariesPower(this)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case MASK_MOVE_1: {
                if (mask == FOX_MASK) {
                    for (int i = 0; i < FOX_ATTACK_HITS; i++) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Kokoro.DIALOG[0]));

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case MASK_MOVE_2: {
                if (mask == FOX_MASK) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), this.wound));
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case MASK_CHANGE: {
                mask++;
                mask = mask % NUM_MASKS;
                if (mask == FOX_MASK) {

                } else if (mask == SPIDER_MASK) {

                } else if (mask == HOPE_MASK) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                } else if (mask == DEMON_MASK) {

                } else if (mask == LION_MASK) {
                    
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, MASK_STRENGTH_BUFF), MASK_STRENGTH_BUFF));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(Kokoro.MOVES[0], OPENING, Intent.UNKNOWN);
            this.firstMove = false;
        }  else if (this.currentHealth < this.maxHealth / 2 && !this.useTrain) {
            this.useTrain = true;
            this.setMove(Kokoro.MOVES[5], LAST_WORD, Intent.DEFEND_BUFF);
        } else if (this.lastMove(LAST_WORD)) {
            useTrainTexture = true;
            this.setMove(Kokoro.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
        } else {
            if (this.useTrain && !this.lastMove(TRAIN) && !this.lastMoveBefore(TRAIN)) { //use train every 3 turns
                useTrainTexture = true;
                this.setMove(Kokoro.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
            }
            else if (num < 35) {
                if (!this.lastMove(STRENGTH_DRAIN)) {
                    this.setMove(Kokoro.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(2)).base);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Kokoro.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    } else {
                        this.setMove(Kokoro.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    }
                }
            } else if (num < 65) {
                if (!this.lastMove(MEGA_DEBUFF)) {
                    this.setMove(Kokoro.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Kokoro.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    } else {
                        this.setMove(Kokoro.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    }
                }
            } else {
                if (!this.lastMove(ATTACK)) {
                    this.setMove(Kokoro.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Kokoro.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    } else {
                        this.setMove(Kokoro.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    }
                }
            }
        }
    }

    @Override
    public Texture getAttackIntent() {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent();
    }
    @Override
    public Texture getAttackIntent(int dmg) {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent(dmg);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yukari");
        NAME = Kokoro.monsterStrings.NAME;
        MOVES = Kokoro.monsterStrings.MOVES;
        DIALOG = Kokoro.monsterStrings.DIALOG;
    }
}