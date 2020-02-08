package Gensokyo.monsters.bossRush;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.RezAction;
import Gensokyo.cards.Butterfly;
import Gensokyo.cards.ImpossibleRequests.ImpossibleRequest;
import Gensokyo.powers.HouraiImmortal;
import Gensokyo.powers.LunaticPrincess;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Kaguya extends CustomMonster
{
    public static final String ID = "Gensokyo:Kaguya";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte BRILLIANT_DRAGON_BULLET = 0;
    private static final byte BUDDHIST_DIAMOND = 1;

    private static final int BRILLIANT_DRAGON_BULLET_DAMAGE = 12;
    private static final int A4_BRILLIANT_DRAGON_BULLET_DAMAGE = 13;
    private static final int BRILLIANT_DRAGON_BULLET_HITS = 2;
    private int brilliantDragonBulletDamage;

    private static final int BUDDHIST_DIAMOND_DAMAGE = 16;
    private static final int A4_BUDDHIST_DIAMOND_DAMAGE = 18;
    private int buddhistDiamondDamage;

    private static final int STRENGTH_GAIN = 3;
    private static final int A19_STRENGTH_GAIN = 4;
    private int strengthGain;

    private static final int HP = 100;
    private static final int A9_HP = 110;
    private int originalMaxHP;

    private Map<Byte, EnemyMoveInfo> moves;
    private ArrayList<AbstractAnimation> souls = new ArrayList<>();

    public Kaguya() {
        this(0.0f, 0.0f);
    }

    public Kaguya(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kaguya/Spriter/KaguyaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strengthGain = A19_STRENGTH_GAIN;
        } else {
            this.strengthGain = STRENGTH_GAIN;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }
        this.originalMaxHP = maxHealth;

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.brilliantDragonBulletDamage = A4_BRILLIANT_DRAGON_BULLET_DAMAGE;
            this.buddhistDiamondDamage = A4_BUDDHIST_DIAMOND_DAMAGE;
        } else {
            this.brilliantDragonBulletDamage = BRILLIANT_DRAGON_BULLET_DAMAGE;
            this.buddhistDiamondDamage = BUDDHIST_DIAMOND_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(BRILLIANT_DRAGON_BULLET, new EnemyMoveInfo(BRILLIANT_DRAGON_BULLET, Intent.ATTACK, this.brilliantDragonBulletDamage, BRILLIANT_DRAGON_BULLET_HITS, true));
        this.moves.put(BUDDHIST_DIAMOND, new EnemyMoveInfo(BUDDHIST_DIAMOND, Intent.ATTACK_BUFF, this.buddhistDiamondDamage, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
//        AbstractDungeon.getCurrRoom().playBgmInstantly("BorderOfLife");
        this.addToBot(new ApplyPowerAction(this, this, new HouraiImmortal(this)));
        ImpossibleRequest request = new ImpossibleRequest();
        request.transform();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new LunaticPrincess(AbstractDungeon.player, this, request)));
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(request));
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
            case BRILLIANT_DRAGON_BULLET: {
                this.useFastAttackAnimation();
                for (int i = 0; i < BRILLIANT_DRAGON_BULLET_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            }
            case BUDDHIST_DIAMOND: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                strengthGain++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(BUDDHIST_DIAMOND);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(BRILLIANT_DRAGON_BULLET)) {
                possibilities.add(BRILLIANT_DRAGON_BULLET);
            }
            if (!this.lastMove(BUDDHIST_DIAMOND)) {
                possibilities.add(BUDDHIST_DIAMOND);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            Iterator var2 = this.powers.iterator();

            while (var2.hasNext()) {
                AbstractPower p = (AbstractPower) var2.next();
                p.onDeath();
            }

            var2 = AbstractDungeon.player.relics.iterator();

            while (var2.hasNext()) {
                AbstractRelic r = (AbstractRelic) var2.next();
                r.onMonsterDeath(this);
            }

            ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
            for (AbstractPower power : this.powers) {
                if (!(power instanceof HouraiImmortal) && !(power instanceof LunaticPrincess)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }

            AbstractDungeon.actionManager.addToBottom(new RezAction(this));

            this.setMoveShortcut(this.nextMove);
            this.createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, this.nextMove, this.intent));
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
        if (this.maxHealth <= 0) {
            this.maxHealth = originalMaxHP;
            AbstractDungeon.actionManager.addToBottom(new InstantKillAction(this));
        }
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Kaguya");
        NAME = Kaguya.monsterStrings.NAME;
        MOVES = Kaguya.monsterStrings.MOVES;
        DIALOG = Kaguya.monsterStrings.DIALOG;
    }
}