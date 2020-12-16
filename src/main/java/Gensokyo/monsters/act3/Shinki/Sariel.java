package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.HashMap;
import java.util.Map;

public class Sariel extends AbstractShinkiDelusion
{
    public static final String ID = GensokyoMod.makeID("Sariel");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte DEBUFF_ATTACK = 0;
    private static final byte ATTACK = 1;

    private static final int ATTACK_DMG = 24;
    private static final int A4_ATTACK_DMG = 26;
    private int attackDmg;

    private static final int DEBUFF_ATTACK_DMG = 14;
    private static final int A4_DEBUFF_ATTACK_DMG = 15;
    private int debuffAttackDmg;

    private static final int DEBUFF_AMT = 2;
    private static final int A19_DEBUFF_AMT = 3;
    private int debuffAmt;

//    private static final int POWER_AMT = 25;
//    private static final int A19_POWER_AMT = 10;
//    private int powerAmt;

    private static final int HP = 300;
    private static final int A9_HP = 320;

    private Map<Byte, EnemyMoveInfo> moves;

    public Sariel(final float x, final float y, Shinki shinki) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 245.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sariel/Spriter/SarielAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.shinki = shinki;
        this.event1 = new SarielEvent1(shinki);
        this.event2 = new SarielEvent2(shinki);
        this.event3 = new SarielEvent3(shinki);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmt = A19_DEBUFF_AMT;
            //this.powerAmt = A19_POWER_AMT;
        } else {
            this.debuffAmt = DEBUFF_AMT;
            //this.powerAmt = POWER_AMT;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(A9_HP);
        } else {
            setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            attackDmg = A4_ATTACK_DMG;
            debuffAttackDmg = A4_DEBUFF_ATTACK_DMG;
        } else {
            attackDmg = ATTACK_DMG;
            debuffAttackDmg = DEBUFF_ATTACK_DMG;
        }
        this.moves = new HashMap<>();
        this.moves.put(ATTACK, new EnemyMoveInfo(ATTACK, Intent.ATTACK, attackDmg, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, debuffAttackDmg, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExposedBack(this, powerAmt)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        switch (this.nextMove) {
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case DEBUFF_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (!lastMove(DEBUFF_ATTACK) && !lastMoveBefore(DEBUFF_ATTACK)) {
            setMoveShortcut(DEBUFF_ATTACK);
        } else {
            setMoveShortcut(ATTACK);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
    }

    @Override
    public String eventDialog(int eventNum) {
        if (eventNum == 1 && AbstractDungeon.player.hasPower(ConstrictedPower.POWER_ID)) {
            return DIALOG[4];
        } else {
            return DIALOG[eventNum];
        }
    }
}