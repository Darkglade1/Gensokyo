package Gensokyo.monsters.NormalEnemies;

import Gensokyo.actions.SetDeadAction;
import Gensokyo.powers.CowardlyImmortality;
import Gensokyo.powers.OnKillPower;
import Gensokyo.powers.StrengthInNumbers;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractFairy extends CustomMonster
{

    protected static final byte REVIVE = 3;
    protected static final byte LEAVE = 4;
    public static final int MAX_DEATHS = 2;
    public int deathCounter = 0;

    public AbstractFairy(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CowardlyImmortality(this, MAX_DEATHS)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthInNumbers(this, 1)));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            deathCounter++;
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

            for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                for (AbstractPower power : mo.powers) {
                    if (power instanceof OnKillPower) {
                        ((OnKillPower) power).onKill(false); //forces a trigger to this method
                    }
                }
            }

            if (this.nextMove != REVIVE && this.nextMove != LEAVE) {
                this.setMove(REVIVE, Intent.NONE);
                this.createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, REVIVE, Intent.NONE));
            }
            ArrayList<AbstractPower> powersToRemove = new ArrayList<>();
            for (AbstractPower power : this.powers) {
                if (!(power instanceof CowardlyImmortality) && !(power instanceof StrengthInNumbers)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }
        }
    }

    protected void Leave() {
        AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
        AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, LEAVE, Intent.ESCAPE));
        this.animation.setFlip(true, false);
        //Need this so phantom enemies don't end up still taking turns for some reason and prolonging combat.
        //Needs to be in an action so the sprite doesn't instantly disappear
        AbstractDungeon.actionManager.addToBottom(new SetDeadAction(this));
        if (isLastFairy()) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }
    }

    protected boolean isLastFairy() {
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof AbstractFairy) {
                if (!mo.isDead && !mo.isEscaping && !mo.isDying && mo != this) {
                    return false;
                }
            }
        }
        return true;
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}