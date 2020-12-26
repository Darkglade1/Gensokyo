package Gensokyo.monsters.act3;

import Gensokyo.CustomIntents.IntentEnums;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.TemporaryMaxHPLossAction;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.Empower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class YuyukoSoul extends AbstractSpriterMonster {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GensokyoMod.makeID("CurseAttackIntent"));
    private static final String[] TEXT = uiStrings.TEXT;
    protected static final byte NONE = 0;
    protected static final byte DEBUFF = 1;
    protected static final int HP = 18;
    protected static final int MAX_HP_REDUCTION = 5;
    protected static final int A19_MAX_HP_REDUCTION = 7;
    protected int hp_reduction;
    protected int bonus = 0;
    private static final float HB_W = 50.0F;
    private static final float HB_H = 100.0f;
    public boolean active = false;
    protected int cooldown;
    protected Yuyuko master;

    public YuyukoSoul(String name, String id, int maxHealth, float hb_x, float hb_y, String imgUrl, float offsetX, float offsetY, Yuyuko master, int bonusHealth) {
        super(name, id, maxHealth, hb_x, hb_y, HB_W, HB_H, imgUrl, offsetX, offsetY);
        this.master = master;
        this.setHp(HP + bonusHealth);
        this.cooldown = 1;
        if (AbstractDungeon.ascensionLevel >= 19) {
            hp_reduction = A19_MAX_HP_REDUCTION;
        } else {
            hp_reduction = MAX_HP_REDUCTION;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.halfDead = true;
        this.currentHealth = 0;
        this.healthBarUpdatedEvent();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void realRender(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        if (this instanceof BlueSoul) {
            master.blueSouls.remove(this);
            master.nextBlueSoul();
        }
        if (this instanceof PurpleSoul) {
            master.purpleSouls.remove(this);
            master.nextPurpleSoul();
        }
    }

    @Override
    public void takeTurn() {
        if (active && cooldown > 0) {
            cooldown--;
        }
        switch (this.nextMove) {
            case DEBUFF: {
                if (AbstractDungeon.player.maxHealth > 1 || TempHPField.tempHp.get(AbstractDungeon.player) > 0) {
                    Yuyuko.playGhostEffect(master);
                    int bonus = 0;
                    AbstractPower power = getPower(Empower.POWER_ID);
                    if (power != null) {
                        bonus = power.amount;
                    }
                    AbstractDungeon.actionManager.addToBottom(new TemporaryMaxHPLossAction(hp_reduction + bonus));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void applyPowers() {
        //Make sure the intent isn't affected by damage modifiers
        if (this.intent == IntentEnums.ATTACK_CURSE) {
            bonus = 0;
            AbstractPower power = getPower(Empower.POWER_ID);
            if (power != null) {
                bonus = power.amount;
            }
            rollMove();
            createIntent();

            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", hp_reduction + bonus);
            PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
            intentTip.body = TEXT[1] + (hp_reduction + bonus) + TEXT[2];
        } else {
            super.applyPowers();
        }
    }

    @Override
    protected void getMove(int num) {
        bonus = 0;
        AbstractPower power = getPower(Empower.POWER_ID);
        if (power != null) {
            bonus = power.amount;
        }
        if (!active) {
            this.setMove(NONE, Intent.NONE);
        } else if (cooldown > 0) {
            this.setMove(NONE, Intent.NONE);
        } else {
            this.setMove(null, DEBUFF, IntentEnums.ATTACK_CURSE, hp_reduction + bonus, 0, false);
        }
    }
}
