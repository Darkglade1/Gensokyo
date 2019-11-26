package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.Iterator;

public class GainStrengthRandomMonsterAction extends AbstractGameAction {
    public GainStrengthRandomMonsterAction(AbstractCreature source, int amount) {
        this.duration = 0.5F;
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.POWER;
    }

    public void update() {
        if (this.duration == 0.5F) {
            ArrayList<AbstractMonster> validMonsters = new ArrayList();
            Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var2.hasNext()) {
                AbstractMonster m = (AbstractMonster)var2.next();
                if (m != this.source && m.intent != Intent.ESCAPE && !m.isDying) {
                    validMonsters.add(m);
                }
            }

            if (!validMonsters.isEmpty()) {
                this.target = validMonsters.get(AbstractDungeon.aiRng.random(validMonsters.size() - 1));
            } else {
                this.target = this.source;
            }

            if (this.target != null) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, amount), amount));
            }
        }

        this.tickDuration();
    }
}
