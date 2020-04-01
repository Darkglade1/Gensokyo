package Gensokyo.monsters.act1.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Gryphon extends CustomMonster
{
    public static final String ID = "Gensokyo:Gryphon";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte ATTACK = 2;
    private static final byte BUFF = 3;
    private static final int DEBUFF_ATTACK_DAMAGE = 7;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 8;
    private static final int ATTACK_DAMAGE = 5;
    private static final int A2_ATTACK_DAMAGE = 6;
    private static final int MULTI_HIT_AMT = 2;
    private static final int DEBUFF_AMOUNT = 1;
    private static final int STRENGTH = 2;
    private static final int A17_STRENGTH = 3;
    private static final int HP_MIN = 45;
    private static final int HP_MAX = 48;
    private static final int A7_HP_MIN = 47;
    private static final int A7_HP_MAX = 50;
    private int attackDamage;
    private int debuffAttackDamage;
    private int debuff;
    private int strength;

    public Gryphon() {
        this(0.0f, 0.0f);
    }

    public Gryphon(final float x, final float y) {
        super(Gryphon.NAME, ID, HP_MAX, -5.0F, 0, 220.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Gryphon/Spriter/GryphonAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.strength = A17_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
            this.debuffAttackDamage = A2_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
            this.debuffAttackDamage = DEBUFF_ATTACK_DAMAGE;
        }
        this.debuff = DEBUFF_AMOUNT;

        this.damage.add(new DamageInfo(this, this.debuffAttackDamage));
        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (whichDebuff()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuff, true)));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuff, true)));
                }
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            }
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(BUFF)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base, MULTI_HIT_AMT, true);
        } else if (AbstractDungeon.player.hasPower(FrailPower.POWER_ID)) {
            if (!this.lastMove(ATTACK)) {
                this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base, MULTI_HIT_AMT, true);
            } else {
                if (num < 50) {
                    this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    this.setMove(BUFF, Intent.BUFF);
                }
            }
        } else if (AbstractDungeon.player.hasPower(WeakPower.POWER_ID)) {
            if (!this.lastMove(BUFF)) {
                this.setMove(BUFF, Intent.BUFF);
            } else {
                if (num < 50) {
                    this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base, MULTI_HIT_AMT, true);
                }
            }
        } else {
            if (!this.lastMove(DEBUFF_ATTACK)) {
                this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            } else {
                if (num < 50) {
                    this.setMove(BUFF, Intent.BUFF);
                } else {
                    this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base, MULTI_HIT_AMT, true);
                }
            }
        }
    }

    //Returns true if there were more attacks played than non-Attacks this turn, and false if vice versa. Checks the last card played if equal
    private boolean whichDebuff() {
        int attackCount = 0;
        int nonAttackCount = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                attackCount++;
            } else {
                nonAttackCount++;
            }
        }
        if (attackCount > nonAttackCount) {
            return true;
        } else if (nonAttackCount > attackCount) {
            return false;
        }
        AbstractCard lastCard = AbstractDungeon.actionManager.lastCard;
        if (lastCard != null && lastCard.type == AbstractCard.CardType.ATTACK) {
            return true;
        } else {
            return false;
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Gryphon");
        NAME = Gryphon.monsterStrings.NAME;
        MOVES = Gryphon.monsterStrings.MOVES;
        DIALOG = Gryphon.monsterStrings.DIALOG;
    }
}