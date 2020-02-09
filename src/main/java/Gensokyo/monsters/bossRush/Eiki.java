package Gensokyo.monsters.bossRush;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.BalanceShiftAction;
import Gensokyo.powers.Guilt;
import Gensokyo.powers.Virtue;
import Gensokyo.powers.Innocence;
import Gensokyo.powers.Judgement;
import Gensokyo.powers.Resolve;
import actlikeit.dungeons.CustomDungeon;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Eiki extends CustomMonster
{
    private static final Texture SCALE_BODY = new Texture("GensokyoResources/images/monsters/Eiki/Body.png");
    private static final Texture SCALE_RIGHT_ARM = new Texture("GensokyoResources/images/monsters/Eiki/RightArm.png");
    private static final Texture SCALE_RIGHT_SCALE = new Texture("GensokyoResources/images/monsters/Eiki/RightScale.png");
    //private static final Texture SCALE_LEFT_ARM = new Texture("GensokyoResources/images/monsters/Eiki/LeftArm.png");
    private static final Texture SCALE_LEFT_SCALE = new Texture("GensokyoResources/images/monsters/Eiki/LeftScale.png");
    private TextureRegion SCALE_BODY_REGION;
    private TextureRegion SCALE_RIGHT_ARM_REGION;
    private TextureRegion SCALE_RIGHT_SCALE_REGION;
    private TextureRegion SCALE_LEFT_ARM_REGION;
    private TextureRegion SCALE_LEFT_SCALE_REGION;
    public static final String ID = "Gensokyo:Eiki";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte LAST_JUDGEMENT = 0;
    private static final byte TRIAL = 1;
    private static final byte GUILTY_OR_NOT = 2;
    private static final byte WANDERING_SIN = 3;

    private static final int DEBUFF_AMOUNT = 2;
    private static final float JUDGEMENT_PERCENT = 0.5F;

    private static final float BONUS_HP_DAMAGE = 0.2F;
    private int bonusDamage;

    private static final int COOLDOWN = 3;
    private int turnCounter = 0;

    private static final int TRIAL_DAMAGE = 20;
    private static final int A4_TRIAL_DAMAGE = 22;
    private int trialDamage;

    private static final int SIN_DAMAGE = 10;
    private static final int A4_SIN_DAMAGE = 11;
    private int sinDamage;

    public static final int STARTING_GUILT = 7;
    public static final int A19_STARTING_GUILT = 10;
    private int startingGuilt;

    public static final int GUILT_THRESHOLD = 20;
    public static final int A19_GUILT_THRESHOLD = 15;
    public int guiltThreshold;

    public static final int VIRTUE_AMOUNT = 20;
    private static final int HP = 150;
    private static final int A9_HP = 160;

    public float angle = 0.0F;
    private Map<Byte, EnemyMoveInfo> moves;
    public ArrayList<AbstractAnimation> guilt = new ArrayList<>();
    public ArrayList<AbstractAnimation> innocence = new ArrayList<>();

    public Eiki() {
        this(0.0f, 0.0f);
    }

    public Eiki(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 280.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Spriter/EikiAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.startingGuilt = A19_STARTING_GUILT;
            this.guiltThreshold = A19_GUILT_THRESHOLD;
        } else {
            this.startingGuilt = STARTING_GUILT;
            this.guiltThreshold = GUILT_THRESHOLD;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.trialDamage = A4_TRIAL_DAMAGE;
            this.sinDamage = A4_SIN_DAMAGE;
        } else {
            this.trialDamage = TRIAL_DAMAGE;
            this.sinDamage = SIN_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(LAST_JUDGEMENT, new EnemyMoveInfo(LAST_JUDGEMENT, Intent.UNKNOWN, -1, 0, false));
        this.moves.put(TRIAL, new EnemyMoveInfo(TRIAL, Intent.ATTACK, this.trialDamage, 0, false));
        this.moves.put(GUILTY_OR_NOT, new EnemyMoveInfo(GUILTY_OR_NOT, Intent.BUFF, -1, 0, true));
        this.moves.put(WANDERING_SIN, new EnemyMoveInfo(WANDERING_SIN, Intent.ATTACK_DEBUFF, this.sinDamage, 0, false));

        this.SCALE_BODY_REGION = new TextureRegion(SCALE_BODY);
        this.SCALE_RIGHT_ARM_REGION = new TextureRegion(SCALE_RIGHT_ARM);
        this.SCALE_RIGHT_SCALE_REGION = new TextureRegion(SCALE_RIGHT_SCALE);
        this.SCALE_LEFT_ARM_REGION = new TextureRegion(SCALE_RIGHT_ARM);
        this.SCALE_LEFT_ARM_REGION.flip(false, true);
        this.SCALE_LEFT_SCALE_REGION = new TextureRegion(SCALE_LEFT_SCALE);
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("FateOfSixtyYears");
        this.addToBot(new ApplyPowerAction(this, this, new Virtue(this)));
        this.addToBot(new ApplyPowerAction(this, this, new Resolve(this, VIRTUE_AMOUNT)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new Innocence(AbstractDungeon.player, 0, this), 0));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new Guilt(AbstractDungeon.player, startingGuilt, this), startingGuilt));
        AbstractDungeon.actionManager.addToBottom(new BalanceShiftAction(this));
        for (int i = 0; i < STARTING_GUILT; i++) {
            guilt.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Guilt/Spriter/GuiltAnimation.scml"));
        }
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.base += bonusDamage;
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case LAST_JUDGEMENT: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new Judgement(AbstractDungeon.player, (int)(AbstractDungeon.player.maxHealth * JUDGEMENT_PERCENT))));
                turnCounter++;
                break;
            }
            case TRIAL: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                turnCounter++;
                break;
            }
            case GUILTY_OR_NOT: {
                int innocence = 0;
                int guilt = 0;
                if (AbstractDungeon.player.hasPower(Innocence.POWER_ID)) {
                    innocence = AbstractDungeon.player.getPower(Innocence.POWER_ID).amount;
                }
                if (AbstractDungeon.player.hasPower(Guilt.POWER_ID)) {
                    guilt = AbstractDungeon.player.getPower(Guilt.POWER_ID).amount;
                }
                int difference = guilt - innocence;
                if (difference < 0) {
                    difference = startingGuilt;
                }
                difference = (int)(difference * 1.5f);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, difference), difference));
                turnCounter = 0;
                break;
            }
            case WANDERING_SIN: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                if (this.angle <= (BalanceShiftAction.MAX_ANGLE / 2)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                }
                turnCounter++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(LAST_JUDGEMENT);
        } else if (turnCounter >= COOLDOWN) {
            this.setMoveShortcut(GUILTY_OR_NOT);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastTwoMoves(WANDERING_SIN)) {
                possibilities.add(WANDERING_SIN);
            }
            if (!this.lastTwoMoves(TRIAL)) {
                possibilities.add(TRIAL);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        bonusDamage = (int)(AbstractDungeon.player.currentHealth * BONUS_HP_DAMAGE);
        int newBase = info.baseDamage + bonusDamage;
        this.setMove(MOVES[next], next, info.intent, newBase, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            super.damage(info);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;
        sb.setColor(Color.WHITE);
        float armXOffset = 10.0F;
        float armYOffset = 150.0F;
        float scaleXOffset = 15.0F;
        float scaleYOffset = 30.0F;

        //calculates offsets if the angle is changed
        double radians = Math.toRadians(angle / 2);
        double length = (Math.sin(radians) * (SCALE_RIGHT_ARM_REGION.getRegionWidth() - armXOffset)) * 2;
        float offsetX = (float)(Math.sin(radians) * length);
        float offsetY = (float)(Math.cos(radians) * length);


        //renders the souls
        float xOffsetIncrement = 25.0F;
        float yOffsetIncrement = 40.0F;
        float startX = 50.0F;
        float startY = 35.0F;
        float soulXOffset = startX;
        float soulYOffset = startY;

        for (int i = 1; i < guilt.size() + 1; i++) {
            AbstractAnimation soul = guilt.get(i - 1);
            soul.renderSprite(sb, (960.0F + armXOffset + SCALE_RIGHT_ARM_REGION.getRegionWidth() - (SCALE_RIGHT_SCALE_REGION.getRegionWidth() / 2.0F) - scaleXOffset - offsetX + soulXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_RIGHT_SCALE_REGION.getRegionHeight() + scaleYOffset - offsetY + soulYOffset) * scaleHeight);
            soulXOffset += xOffsetIncrement;
            if (i % 4 == 0) {
                soulYOffset += yOffsetIncrement;
                soulXOffset = startX;
            }
        }

        soulXOffset = startX;
        soulYOffset = startY;

        for (int i = 1; i < innocence.size() + 1; i++) {
            AbstractAnimation soul = innocence.get(i - 1);
            soul.renderSprite(sb, (960.0F - armXOffset - SCALE_LEFT_ARM_REGION.getRegionWidth() - (SCALE_LEFT_SCALE_REGION.getRegionWidth() / 2.0F) + scaleXOffset + offsetX + soulXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_LEFT_SCALE_REGION.getRegionHeight() + scaleYOffset + offsetY + soulYOffset) * scaleHeight);
            soulXOffset += xOffsetIncrement;
            if (i % 4 == 0) {
                soulYOffset += yOffsetIncrement;
                soulXOffset = startX;
            }
        }


        //renders the balance
        sb.draw(SCALE_RIGHT_SCALE_REGION, (960.0F + armXOffset + SCALE_RIGHT_ARM_REGION.getRegionWidth() - (SCALE_RIGHT_SCALE_REGION.getRegionWidth() / 2.0F) - scaleXOffset - offsetX) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_RIGHT_SCALE_REGION.getRegionHeight() + scaleYOffset - offsetY) * scaleHeight, 0.0F, 0.0F, SCALE_RIGHT_SCALE_REGION.getRegionWidth(), SCALE_RIGHT_SCALE_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        sb.draw(SCALE_RIGHT_ARM_REGION, (960.0F + armXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset) * scaleHeight, 0.0F, (SCALE_RIGHT_ARM_REGION.getRegionHeight() / 2.0F) * scaleHeight, SCALE_RIGHT_ARM_REGION.getRegionWidth(), SCALE_RIGHT_ARM_REGION.getRegionHeight(), scaleWidth, scaleHeight, -angle);
        sb.draw(SCALE_LEFT_SCALE_REGION, (960.0F - armXOffset - SCALE_LEFT_ARM_REGION.getRegionWidth() - (SCALE_LEFT_SCALE_REGION.getRegionWidth() / 2.0F) + scaleXOffset + offsetX) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_LEFT_SCALE_REGION.getRegionHeight() + scaleYOffset + offsetY) * scaleHeight, 0.0F, 0.0F, SCALE_LEFT_SCALE_REGION.getRegionWidth(), SCALE_LEFT_SCALE_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        sb.draw(SCALE_LEFT_ARM_REGION, (960.0F - armXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset) * scaleHeight, 0.0F, (SCALE_LEFT_ARM_REGION.getRegionHeight() / 2.0F) * scaleHeight, SCALE_LEFT_ARM_REGION.getRegionWidth(), SCALE_LEFT_ARM_REGION.getRegionHeight(), scaleWidth, scaleHeight, -(angle + 180));
        sb.draw(SCALE_BODY_REGION, (960.0F - (SCALE_BODY_REGION.getRegionWidth() / 2.0F)) * scaleWidth, AbstractDungeon.floorY, 0.0F, 0.0F, SCALE_BODY_REGION.getRegionWidth(), SCALE_BODY_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        this.animation.setFlip(horizontal, vertical);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Eiki");
        NAME = Eiki.monsterStrings.NAME;
        MOVES = Eiki.monsterStrings.MOVES;
        DIALOG = Eiki.monsterStrings.DIALOG;
    }
}