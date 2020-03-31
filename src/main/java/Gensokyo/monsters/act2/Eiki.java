package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.BalanceShiftAction;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act2.MirrorPower;
import Gensokyo.powers.act2.NextTurnInnocence;
import Gensokyo.vfx.FlexibleStanceAuraEffect;
import Gensokyo.vfx.FlexibleWrathParticleEffect;
import actlikeit.dungeons.CustomDungeon;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.stances.WrathStance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Eiki extends AbstractSpriterMonster
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
    private static final byte CLEANSED_CRYSTAL_JUDGEMENT = 0;
    private static final byte WANDERING_SIN = 1;
    private static final byte GUILTY_OR_NOT = 2;
    private static final byte ROD_OF_REMORSE = 3;

    private static final int DEBUFF_AMOUNT = 1;

    private static final int ROD_DAMAGE = 18;
    private static final int A4_ROD_DAMAGE = 20;
    private int rodDamage;

    private static final int SIN_DAMAGE = 13;
    private static final int A4_SIN_DAMAGE = 14;
    private int sinDamage;

    private static final int STRENGTH = 3;
    private static final int A19_STRENGTH = 4;
    private int strength;

    public int guiltCount;
    public int innocenceCount;

    private static final int HP = 300;
    private static final int A9_HP = 315;

    private boolean mirrorDead = false;

    public float angle = 0.0F;
    private Map<Byte, EnemyMoveInfo> moves;
    public ArrayList<AbstractAnimation> guilt = new ArrayList<>();
    public ArrayList<AbstractAnimation> innocence = new ArrayList<>();

    private float particleTimer;
    private float particleTimer2;

    public Eiki() {
        this(200.0f, 0.0f);
    }

    public Eiki(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 280.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Spriter/EikiAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strength = A19_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.rodDamage = A4_ROD_DAMAGE;
            this.sinDamage = A4_SIN_DAMAGE;
        } else {
            this.rodDamage = ROD_DAMAGE;
            this.sinDamage = SIN_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(CLEANSED_CRYSTAL_JUDGEMENT, new EnemyMoveInfo(CLEANSED_CRYSTAL_JUDGEMENT, Intent.UNKNOWN, -1, 0, false));
        this.moves.put(WANDERING_SIN, new EnemyMoveInfo(WANDERING_SIN, Intent.ATTACK_DEBUFF, this.sinDamage, 0, false));
        this.moves.put(GUILTY_OR_NOT, new EnemyMoveInfo(GUILTY_OR_NOT, Intent.BUFF, -1, 0, false));
        this.moves.put(ROD_OF_REMORSE, new EnemyMoveInfo(ROD_OF_REMORSE, Intent.ATTACK, this.rodDamage, 0, false));

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
            case CLEANSED_CRYSTAL_JUDGEMENT: {
                Mirror mirror = new Mirror(-100.0F, 0.0F, this);
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mirror, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mirror, this, new MirrorPower(mirror, mirror)));
                break;
            }
            case WANDERING_SIN: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
            case GUILTY_OR_NOT: {
                if (!mirrorDead) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (mo instanceof Mirror) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new NextTurnInnocence(mo)));
                        }
                    }
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength * 2), this.strength * 2));
                }
                break;
            }
            case ROD_OF_REMORSE: {
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void mirrorDeath() {
        if (!this.isDead && !this.isDying) {
            mirrorDead = true;
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
        }
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(CLEANSED_CRYSTAL_JUDGEMENT);
        } else {
            if (!mirrorDead) {
                if (this.lastMove(WANDERING_SIN)) {
                    this.setMoveShortcut(GUILTY_OR_NOT);
                } else if (this.lastMove(GUILTY_OR_NOT)) {
                    this.setMoveShortcut(ROD_OF_REMORSE);
                } else {
                    this.setMoveShortcut(WANDERING_SIN);
                }
            } else {
                if (this.lastMove(GUILTY_OR_NOT)) {
                    this.setMoveShortcut(ROD_OF_REMORSE);
                } else {
                    this.setMoveShortcut(GUILTY_OR_NOT);
                }
            }
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        if (next == CLEANSED_CRYSTAL_JUDGEMENT) {
            this.setMove(MOVES[next] + AbstractDungeon.player.title, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        } else {
            this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        }
    }

    public void incrementGuilt(int amount) {
        for (int i = 0; i < amount; i++) {
            guilt.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Guilt/Spriter/GuiltAnimation.scml"));
        }
        guiltCount += amount;
        AbstractDungeon.actionManager.addToBottom(new BalanceShiftAction(this));
    }

    public void incrementInnocence(int amount) {
        for (int i = 0; i < amount; i++) {
            innocence.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Innocence/Spriter/InnocenceAnimation.scml"));
        }
        innocenceCount += amount;
        AbstractDungeon.actionManager.addToBottom(new BalanceShiftAction(this));
    }

    public void resetBalance() {
        guilt.clear();
        guiltCount = 0;
        innocence.clear();
        innocenceCount = 0;
        AbstractDungeon.actionManager.addToBottom(new BalanceShiftAction(this));
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

        //Render particles when she uses debuff attack
        if (this.nextMove == WANDERING_SIN) {
            if (!Settings.DISABLE_EFFECTS) {
                this.particleTimer -= Gdx.graphics.getDeltaTime();
                if (this.particleTimer < 0.0F) {
                    this.particleTimer = 0.05F;
                    AbstractDungeon.effectsQueue.add(new FlexibleWrathParticleEffect(this));
                }
            }

            this.particleTimer2 -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer2 < 0.0F) {
                this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
                AbstractDungeon.effectsQueue.add(new FlexibleStanceAuraEffect(WrathStance.STANCE_ID, this));
            }
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Mirror) {
                if (!mo.isDead && !mo.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Eiki");
        NAME = Eiki.monsterStrings.NAME;
        MOVES = Eiki.monsterStrings.MOVES;
        DIALOG = Eiki.monsterStrings.DIALOG;
    }
}