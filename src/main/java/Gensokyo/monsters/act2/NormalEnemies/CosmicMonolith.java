package Gensokyo.monsters.act2.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.vfx.RedLaserBeamEffect;
import Gensokyo.vfx.RedSmallLaserEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CosmicMonolith extends AbstractSpriterMonster
{
    public static final String ID = "Gensokyo:CosmicMonolith";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte BUFF_ATTACK = 0;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte BIG_ATTACK = 2;
    private static final int BUFF_ATTACK_DAMAGE = 6;
    private static final int A2_BUFF_ATTACK_DAMAGE = 7;
    private static final int DEBUFF_ATTACK_DAMAGE = 10;
    private static final int A2_DEBUFF_ATTACK_DAMAGE = 11;
    private static final int BIG_ATTACK_DAMAGE = 18;
    private static final int A2_BIG_ATTACK_DAMAGE = 20;
    private static final int STATUS = 1;
    private static final int A17_STATUS = 2;
    private static final int DEBUFF = 1;
    private static final int METALLICIZE = 9;
    private static final int HP_MIN = 60;
    private static final int HP_MAX = 64;
    private static final int A7_HP_MIN = 62;
    private static final int A7_HP_MAX = 67;
    private int buffAttackDamage;
    private int debuffAttackDamage;
    private int bigAttackDamage;
    private int status;

    private Map<Byte, EnemyMoveInfo> moves;

    public CosmicMonolith() {
        this(0.0f, 0.0f);
    }

    public CosmicMonolith(final float x, final float y) {
        super(CosmicMonolith.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 250.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/CosmicMonolith/Spriter/CosmicMonolithAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.status = A17_STATUS;
        } else {
            this.status = STATUS;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.buffAttackDamage = A2_BUFF_ATTACK_DAMAGE;
            this.debuffAttackDamage = A2_DEBUFF_ATTACK_DAMAGE;
            this.bigAttackDamage = A2_BIG_ATTACK_DAMAGE;
        } else {
            this.buffAttackDamage = BUFF_ATTACK_DAMAGE;
            this.debuffAttackDamage = DEBUFF_ATTACK_DAMAGE;
            this.bigAttackDamage = BIG_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(BUFF_ATTACK, new EnemyMoveInfo(BUFF_ATTACK, Intent.ATTACK_BUFF, buffAttackDamage, 0, false));
        this.moves.put(DEBUFF_ATTACK, new EnemyMoveInfo(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, debuffAttackDamage, 0, false));
        this.moves.put(BIG_ATTACK, new EnemyMoveInfo(BIG_ATTACK, Intent.ATTACK_DEBUFF, bigAttackDamage, 0, false));

    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, METALLICIZE)));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, METALLICIZE));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case BUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedSmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedSmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, 1, true), 1));
                break;
            }
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedSmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedSmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF, true), DEBUFF));
                break;
            }
            case BIG_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedLaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), status));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(BUFF_ATTACK)) {
            this.setMoveShortcut(BIG_ATTACK);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(BUFF_ATTACK) && !this.lastMoveBefore(BUFF_ATTACK)) {
                possibilities.add(BUFF_ATTACK);
            }
            if (!this.lastMove(DEBUFF_ATTACK) || !this.lastMoveBefore(DEBUFF_ATTACK)) {
                possibilities.add(DEBUFF_ATTACK);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:CosmicMonolith");
        NAME = CosmicMonolith.monsterStrings.NAME;
        MOVES = CosmicMonolith.monsterStrings.MOVES;
        DIALOG = CosmicMonolith.monsterStrings.DIALOG;
    }
}