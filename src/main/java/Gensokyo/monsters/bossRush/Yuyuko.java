package Gensokyo.monsters.bossRush;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.DeathTouch;
import Gensokyo.powers.Evasive;
import Gensokyo.powers.IllusionaryDominance;
import Gensokyo.vfx.FlexibleCalmParticleEffect;
import Gensokyo.vfx.FlexibleStanceAuraEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.stances.CalmStance;

public class Yuyuko extends CustomMonster
{
    private static final Texture FAN = new Texture("GensokyoResources/images/monsters/Yuyuko/Fan.png");
    private TextureRegion FAN_REGION;
    public static final String ID = "Gensokyo:Yuyuko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte BUFF = 1;
    private static final byte ATTACK = 2;
    private static final byte DEBUFF_ATTACK = 3;
    private static final int NORMAL_ATTACK_DAMAGE = 6;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 7;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 8;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 9;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int BLOCK = 9;
    private static final int A8_BLOCK = 10;
    private static final int STRENGTH = 3;
    private static final int A18_STRENGTH = 4;
    private static final int HP_MIN = 70;
    private static final int HP_MAX = 72;
    private static final int A_2_HP_MIN = 72;
    private static final int A_2_HP_MAX = 74;
    private int normalDamage;
    private int debuffDamage;
    private int block;
    private int strength;


    public Yuyuko() {
        this(0.0f, 0.0f);
    }

    public Yuyuko(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A3_DEBUFF_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));
        this.FAN_REGION = new TextureRegion(FAN);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new DeathTouch(this)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BUFF: {
                if (this.firstMove) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                    this.firstMove = false;
                }
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
                break;
            }
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
            case ATTACK: {
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
            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
        } else if (!this.lastMove(BUFF) && !this.lastMoveBefore(BUFF)) {
            this.setMove(MOVES[0], BUFF, Intent.DEFEND_BUFF);
        } else {
            if (this.lastMove(BUFF)) {
                this.setMove(MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            } else if (this.lastMove(ATTACK)){

            } else {
                this.setMove(MOVES[2], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;
        sb.setColor(Color.WHITE);
        sb.draw(FAN_REGION, this.drawX - this.FAN_REGION.getRegionWidth() * scaleWidth, this.drawY + (this.FAN_REGION.getRegionHeight() * scaleHeight) / 2, 0.0F, 0.0F, this.FAN_REGION.getRegionWidth(), this.FAN_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yuyuko");
        NAME = Yuyuko.monsterStrings.NAME;
        MOVES = Yuyuko.monsterStrings.MOVES;
        DIALOG = Yuyuko.monsterStrings.DIALOG;
    }
}