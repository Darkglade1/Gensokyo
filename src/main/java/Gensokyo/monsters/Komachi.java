package Gensokyo.monsters;

import Gensokyo.relics.CelestialsFlawlessClothing;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Komachi extends CustomMonster
{
    public static final String ID = "Gensokyo:Komachi";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte SCYTHE = 1;
    private static final byte DEBUFF = 2;
    private static final byte DEATH = 3;
    private static final int SCYTHE_DAMAGE = 15;
    private static final int SCYTHE_ACT_DAMAGE_BONUS = 5;
    private static final int DEATH_COUNTER = 5;
    private static final int A_2_SCYTHE_DAMAGE = 18;
    private static final int A_2_DEATH_COUNTER = 4;
    private int scytheDmg;
    private int deathCounter;
    private static final int WEAK_AMT = 2;
    private static final int FRAIL_AMT = 2;
    private static float actMultiplier = 1.0f;
    private static final float ACT_2_MULTIPLIER = 1.5f;
    private static final float ACT_3_MULTIPLIER = 2.0f;
    private static final float ACT_4_MULTIPLIER = 2.5f;
    private static final int HP_MIN = 80;
    private static final int HP_MAX = 84;
    private static final int A_2_HP_MIN = 84;
    private static final int A_2_HP_MAX = 90;
    
    public Komachi() {
        this(0.0f, 0.0f);
    }
    
    public Komachi(final float x, final float y) {
        super(Komachi.NAME, ID, HP_MAX, -5.0F, 0, 280.0f, 255.0f, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/Komachi/Spriter/KomachiAnimations.scml");
        this.type = AbstractMonster.EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y + 75.0F) * Settings.scale;
        if (AbstractDungeon.actNum == 2) {
            actMultiplier = ACT_2_MULTIPLIER;
        } else if (AbstractDungeon.actNum == 3) {
            actMultiplier = ACT_3_MULTIPLIER;
        } else if (AbstractDungeon.actNum == 4) {
            actMultiplier = ACT_4_MULTIPLIER;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp((int)(A_2_HP_MIN * actMultiplier), (int)(A_2_HP_MAX * actMultiplier));
        }
        else {
            this.setHp((int)(HP_MIN * actMultiplier), (int)(HP_MAX * actMultiplier));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.scytheDmg = A_2_SCYTHE_DAMAGE + (SCYTHE_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.deathCounter = A_2_DEATH_COUNTER;
        }
        else {
            this.scytheDmg = SCYTHE_DAMAGE + (SCYTHE_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.deathCounter = DEATH_COUNTER;
        }
        this.damage.add(new DamageInfo(this, this.scytheDmg));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1: {
                if (this.firstMove) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Komachi.DIALOG[0]));
                    this.firstMove = false;
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case 2: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, WEAK_AMT, true), WEAK_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, FRAIL_AMT, true), FRAIL_AMT));
                break;
            }
            case 3: {
//                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
//                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
//                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ColorShiftPower(this, (AbstractDungeon.ascensionLevel >= 18) ? 2 : 1)));
    }
    
//    @Override
//    public void changeState(final String stateName) {
//        switch (stateName) {
//            case "ATTACK": {
//                this.state.setAnimation(0, "Attack", false);
//                this.state.addAnimation(0, "Idle", true, 0.0f);
//                break;
//            }
//            case "ATTACK_2": {
//                this.state.setAnimation(0, "Attack_2", false);
//                this.state.addAnimation(0, "Idle", true, 0.0f);
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void damage(final DamageInfo info) {
//        super.damage(info);
//        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
//            this.state.setAnimation(0, "Hit", false);
//            this.state.addAnimation(0, "Idle", true, 0.0f);
//        }
//    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(Komachi.MOVES[0], (byte) 1, Intent.ATTACK, (this.damage.get(0)).base);
        } else {
            if (this.lastMove((byte) 1)) {
                this.setMove(Komachi.MOVES[1], (byte) 2, Intent.DEBUFF);
            } else {
                this.setMove(Komachi.MOVES[0], (byte) 1, Intent.ATTACK, (this.damage.get(0)).base);
            }
        }
    }
    
//    @Override
//    public void die() {
//        super.die();
//        if (AbstractDungeon.player.hasRelic(CelestialsFlawlessClothing.ID)) {
//            CelestialsFlawlessClothing relic = (CelestialsFlawlessClothing)AbstractDungeon.player.getRelic(CelestialsFlawlessClothing.ID);
//            relic.elite = null;
//        }
//    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Komachi");
        NAME = Komachi.monsterStrings.NAME;
        MOVES = Komachi.monsterStrings.MOVES;
        DIALOG = Komachi.monsterStrings.DIALOG;
    }
}