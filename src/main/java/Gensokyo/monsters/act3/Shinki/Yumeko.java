package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.BurdenOfFailure;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.HashMap;
import java.util.Map;

public class Yumeko extends AbstractShinkiDelusion
{
    public static final String ID = GensokyoMod.makeID("Yumeko");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte DEFEND_DEBUFF = 2;

    private static final int ATTACK_DMG = 20;
    private static final int A4_ATTACK_DMG = 22;
    private int attackDmg;

    private static final int DEBUFF_ATTACK_DMG = 14;
    private static final int A4_DEBUFF_ATTACK_DMG = 15;
    private int debuffAttackDmg;

    private static final int STATUS_AMT = 2;

    private static final int A9_BLOCK = 12;
    private static final int BLOCK = 10;
    private int block;

    private static final int DEBUFF_AMT = 1;

    private static final int POWER_AMT = 30;

    private static final int HP = 300;
    private static final int A9_HP = 330;

    public boolean powerTriggered = false;
    private Map<Byte, EnemyMoveInfo> moves;

    public Yumeko(final float x, final float y, Shinki shinki) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yumeko/Spriter/YumekoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.shinki = shinki;
        this.event1 = new YumekoEvent1(shinki);
        this.event2 = new YumekoEvent2(shinki);
        this.event3 = new YumekoEvent3(shinki);
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            setHp(HP);
            this.block = BLOCK;
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
        this.moves.put(DEFEND_DEBUFF, new EnemyMoveInfo(DEFEND_DEBUFF, Intent.DEFEND_DEBUFF, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BurdenOfFailure(this, POWER_AMT, this)));
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
            case ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case DEBUFF_ATTACK: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), STATUS_AMT, true, true));
                break;
            }
            case DEFEND_DEBUFF: {
                addToBot(new GainBlockAction(this, block));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                powerTriggered = false;
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
        if (powerTriggered) {
            setMoveShortcut(DEFEND_DEBUFF);
        } else if (lastMove(ATTACK)){
            setMoveShortcut(DEBUFF_ATTACK);
        } else {
            setMoveShortcut(ATTACK);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
    }
}