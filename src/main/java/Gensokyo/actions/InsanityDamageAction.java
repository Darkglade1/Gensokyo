package Gensokyo.actions;

import Gensokyo.powers.act2.Insanity;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class InsanityDamageAction extends AbstractGameAction {
    AbstractCreature target;

    public InsanityDamageAction(AbstractCreature target) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.target = target;
    }

    public void update() {
        this.isDone = false;

        if (target.hasPower(Insanity.POWER_ID)) {
            int damage = target.getPower(Insanity.POWER_ID).amount;
            this.addToBot(new DamageAction(target, new DamageInfo(target, damage, DamageInfo.DamageType.THORNS), AttackEffect.POISON));
        }

        this.isDone = true;
    }
}


