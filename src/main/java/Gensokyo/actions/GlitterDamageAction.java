package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GlitterDamageAction extends AbstractGameAction {
    AbstractCreature creature;
    int multiplier;

    public GlitterDamageAction(AbstractCreature creature, int multiplier) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.creature = creature;
        this.multiplier = multiplier;
    }

    @Override
    public void update() {
        this.isDone = false;

        int debuffs = 0;
        for (AbstractPower power : creature.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs++;
            }
        }

        int totalDamage = debuffs * multiplier;
        AbstractDungeon.actionManager.addToBottom(new DamageAction(creature, new DamageInfo(creature, totalDamage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));

        this.isDone = true;
    }
}


