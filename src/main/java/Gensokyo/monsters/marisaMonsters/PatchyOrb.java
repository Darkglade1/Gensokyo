package Gensokyo.monsters.marisaMonsters;

import Gensokyo.powers.ElementalBarrier;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class PatchyOrb extends CustomMonster {
    protected static final int HP = 10;
    protected static final int A8_HP = 11;
    private float movement = Patchouli.orbOffset;
    private Patchouli master;
    private int increaseAmount = Patchouli.INVINCIBLE_INCREMENT;

    public PatchyOrb(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, Patchouli master) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.master = master;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
        } else {
            this.setHp(HP);
        }
    }

    @Override
    public void usePreBattleAction() {
        this.halfDead = true;
        this.currentHealth = 0;
        this.healthBarUpdatedEvent();
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
                if (!(power instanceof MinionPower)) {
                    powersToRemove.add(power);
                }
            }
            for (AbstractPower power : powersToRemove) {
                this.powers.remove(power);
            }

            master.shiftIntent();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(master, master, new ElementalBarrier(master, increaseAmount), increaseAmount));
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if (master.isDeadOrEscaped()) {
            super.die(triggerRelics);
        }
    }

    @Override
    public void takeTurn() {
    }

    @Override
    protected void getMove(int num) {
        this.setMove((byte)0, Intent.NONE); //Setting intent to none makes their turn go by much faster
    }
}
