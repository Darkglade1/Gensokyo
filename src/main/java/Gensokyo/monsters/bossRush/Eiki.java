package Gensokyo.monsters.bossRush;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.actions.BalanceShiftAction;
import Gensokyo.actions.YeetPlayerAction;
import Gensokyo.cards.Butterfly;
import Gensokyo.powers.Guilt;
import Gensokyo.powers.Innocence;
import Gensokyo.vfx.EmptyEffect;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Eiki extends CustomMonster
{
    private static final Texture SCALE_BODY = new Texture("GensokyoResources/images/monsters/Eiki/Body.png");
    private static final Texture SCALE_RIGHT_ARM = new Texture("GensokyoResources/images/monsters/Eiki/RightArm.png");
    private static final Texture SCALE_RIGHT_SCALE = new Texture("GensokyoResources/images/monsters/Eiki/RightScale.png");
    private static final Texture SCALE_LEFT_ARM = new Texture("GensokyoResources/images/monsters/Eiki/LeftArm.png");
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
    private static final byte GHOSTLY_BUTTERFLY = 0;
    private static final byte GHASTLY_DREAM = 1;
    private static final byte LAW_OF_MORTALITY = 2;
    private static final byte RESURRECTION_BUTTERFLY = 3;
    private static final byte SAIGYOUJI_PARINIRVANA = 4;
    private static final byte BUTTERFLY_DELUSION = 5;
    private static final int COOLDOWN = 2;
    private static final int GHOSTLY_BUTTERFLY_DAMAGE = 24;
    private static final int A4_GHOSTLY_BUTTERFLY_DAMAGE = 26;
    private static final int GHASTLY_DREAM_DAMAGE = 18;
    private static final int A4_GHASTLY_DREAM_DAMAGE = 20;
    private static final int RESURRECTION_BUTTERFLY_DAMAGE = 16;
    private static final int A4_RESURRECTION_BUTTERFLY_DAMAGE = 18;
    private static final int BLOCK = 20;
    private static final int A9_BLOCK = 22;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int FIRST_TURN_STATUS_COUNT = 3;
    private static final int STATUS_COUNT = 2;
    private static final int A19_STATUS_COUNT = 3;
    private static final int FAN_INCREMENT = 1;
    private static final int A19_FAN_INCREMENT = 2;
    public static final int FAN_THRESHOLD = 10;
    public static final int STARTING_GUILT = 7;
    private static final int HP = 150;
    private static final int A9_HP = 160;
    private int ghostlyButterflyDamage;
    private int ghastlyDreamDamage;
    private int resurrectionButterflyDamage;
    private int statusCount;
    private int fanIncrement;
    private int block;
    public int fanCounter;
    private int turnCounter;
    public float angle = 0.0F;
    private Map<Byte, EnemyMoveInfo> moves;
    public ArrayList<AbstractAnimation> guilt = new ArrayList<>();
    public ArrayList<AbstractAnimation> innocence = new ArrayList<>();
    private AbstractAnimation attackAnimations = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/AttackAnimations/Spriter/AttackAnimations.scml");

    public Eiki() {
        this(0.0f, 0.0f);
    }

    public Eiki(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.statusCount = A19_STATUS_COUNT;
            this.fanIncrement = A19_FAN_INCREMENT;
        } else {
            this.statusCount = STATUS_COUNT;
            this.fanIncrement = FAN_INCREMENT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.ghostlyButterflyDamage = A4_GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = A4_GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = A4_RESURRECTION_BUTTERFLY_DAMAGE;
        } else {
            this.ghostlyButterflyDamage = GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = RESURRECTION_BUTTERFLY_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GHOSTLY_BUTTERFLY, new EnemyMoveInfo(GHOSTLY_BUTTERFLY, Intent.ATTACK, this.ghostlyButterflyDamage, 0, false));
        this.moves.put(GHASTLY_DREAM, new EnemyMoveInfo(GHASTLY_DREAM, Intent.ATTACK_DEFEND, this.ghastlyDreamDamage, 0, false));
        this.moves.put(LAW_OF_MORTALITY, new EnemyMoveInfo(LAW_OF_MORTALITY, Intent.DEBUFF, -1, 0, true));
        this.moves.put(RESURRECTION_BUTTERFLY, new EnemyMoveInfo(RESURRECTION_BUTTERFLY, Intent.ATTACK_DEBUFF, this.resurrectionButterflyDamage, 0, false));
        this.moves.put(SAIGYOUJI_PARINIRVANA, new EnemyMoveInfo(SAIGYOUJI_PARINIRVANA, IntentEnums.DEATH, -1, 0, false));
        this.moves.put(BUTTERFLY_DELUSION, new EnemyMoveInfo(BUTTERFLY_DELUSION, Intent.DEFEND_DEBUFF, -1, 0, false));

        this.SCALE_BODY_REGION = new TextureRegion(SCALE_BODY);
        this.SCALE_RIGHT_ARM_REGION = new TextureRegion(SCALE_RIGHT_ARM);
        this.SCALE_RIGHT_SCALE_REGION = new TextureRegion(SCALE_RIGHT_SCALE);
        this.SCALE_LEFT_ARM_REGION = new TextureRegion(SCALE_RIGHT_ARM);
        this.SCALE_LEFT_ARM_REGION.flip(false, true);
        this.SCALE_LEFT_SCALE_REGION = new TextureRegion(SCALE_LEFT_SCALE);
    }

    @Override
    public void usePreBattleAction() {
//        AbstractDungeon.getCurrRoom().playBgmInstantly("BorderOfLife");
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new Innocence(AbstractDungeon.player, 0, this), 0));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new Guilt(AbstractDungeon.player, STARTING_GUILT, this), STARTING_GUILT));
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
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GHOSTLY_BUTTERFLY: {
                //runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                turnCounter++;
                break;
            }
            case GHASTLY_DREAM: {
                //runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                turnCounter++;
                break;
            }
            case LAW_OF_MORTALITY: {
                //runAnim("MagicCircle");
                CardCrawlGame.sound.playV("Gensokyo:magic", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                turnCounter++;
                break;
            }
            case RESURRECTION_BUTTERFLY: {
                //runAnim("ButterflyCircle");
                CardCrawlGame.sound.playV("Gensokyo:pest", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Butterfly(), statusCount, true, true));
                //AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                //incrementFan(fanIncrement);
                turnCounter = 0;
                break;
            }
            case SAIGYOUJI_PARINIRVANA: {
               // runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                addToBot(new YeetPlayerAction());
                break;
            }
            case BUTTERFLY_DELUSION: {
                //runAnim("MagicCircle");
                CardCrawlGame.sound.playV("Gensokyo:magic", 1.5F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Butterfly(), FIRST_TURN_STATUS_COUNT));
                turnCounter++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(BUTTERFLY_DELUSION);
        } else if (this.fanCounter >= FAN_THRESHOLD) {
            this.setMoveShortcut(SAIGYOUJI_PARINIRVANA);
        } else if (turnCounter >= COOLDOWN) {
            this.setMoveShortcut(RESURRECTION_BUTTERFLY);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GHOSTLY_BUTTERFLY)) {
                possibilities.add(GHOSTLY_BUTTERFLY);
            }
            if (!this.lastMove(GHASTLY_DREAM)) {
                possibilities.add(GHASTLY_DREAM);
            }
            if (!this.lastMove(LAW_OF_MORTALITY) && !this.lastMoveBefore(LAW_OF_MORTALITY)) {
                possibilities.add(LAW_OF_MORTALITY);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
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


        //renders the guilt souls
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


        //renders the balance
        sb.draw(SCALE_RIGHT_SCALE_REGION, (960.0F + armXOffset + SCALE_RIGHT_ARM_REGION.getRegionWidth() - (SCALE_RIGHT_SCALE_REGION.getRegionWidth() / 2.0F) - scaleXOffset - offsetX) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_RIGHT_SCALE_REGION.getRegionHeight() + scaleYOffset - offsetY) * scaleHeight, 0.0F, 0.0F, SCALE_RIGHT_SCALE_REGION.getRegionWidth(), SCALE_RIGHT_SCALE_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        sb.draw(SCALE_RIGHT_ARM_REGION, (960.0F + armXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset) * scaleHeight, 0.0F, (SCALE_RIGHT_ARM_REGION.getRegionHeight() / 2.0F) * scaleHeight, SCALE_RIGHT_ARM_REGION.getRegionWidth(), SCALE_RIGHT_ARM_REGION.getRegionHeight(), scaleWidth, scaleHeight, -angle);
        sb.draw(SCALE_LEFT_SCALE_REGION, (960.0F - armXOffset - SCALE_LEFT_ARM_REGION.getRegionWidth() - (SCALE_LEFT_SCALE_REGION.getRegionWidth() / 2.0F) + scaleXOffset + offsetX) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset - SCALE_LEFT_SCALE_REGION.getRegionHeight() + scaleYOffset + offsetY) * scaleHeight, 0.0F, 0.0F, SCALE_LEFT_SCALE_REGION.getRegionWidth(), SCALE_LEFT_SCALE_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        sb.draw(SCALE_LEFT_ARM_REGION, (960.0F - armXOffset) * scaleWidth, AbstractDungeon.floorY + (SCALE_BODY_REGION.getRegionHeight() - armYOffset) * scaleHeight, 0.0F, (SCALE_LEFT_ARM_REGION.getRegionHeight() / 2.0F) * scaleHeight, SCALE_LEFT_ARM_REGION.getRegionWidth(), SCALE_LEFT_ARM_REGION.getRegionHeight(), scaleWidth, scaleHeight, -(angle + 180));
        sb.draw(SCALE_BODY_REGION, (960.0F - (SCALE_BODY_REGION.getRegionWidth() / 2.0F)) * scaleWidth, AbstractDungeon.floorY, 0.0F, 0.0F, SCALE_BODY_REGION.getRegionWidth(), SCALE_BODY_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);


        attackAnimations.renderSprite(sb, AbstractDungeon.player.drawX, AbstractDungeon.player.drawY);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Eiki");
        NAME = Eiki.monsterStrings.NAME;
        MOVES = Eiki.monsterStrings.MOVES;
        DIALOG = Eiki.monsterStrings.DIALOG;
    }
}