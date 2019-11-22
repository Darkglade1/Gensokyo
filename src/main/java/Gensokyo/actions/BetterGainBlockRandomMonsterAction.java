package Gensokyo.actions;

import Gensokyo.monsters.NormalEnemies.AbstractFairy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class BetterGainBlockRandomMonsterAction extends AbstractGameAction {
    public BetterGainBlockRandomMonsterAction(AbstractCreature source, int amount) {
        this.duration = 0.5F;
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.BLOCK;
    }

    public void update() {
        if (this.duration == 0.5F) {
            ArrayList<AbstractMonster> validMonsters = new ArrayList();
            Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var2.hasNext()) {
                AbstractMonster m = (AbstractMonster)var2.next();
                if (m != this.source && m.intent != Intent.ESCAPE && !m.isDying && !m.halfDead) {
                    if (m instanceof AbstractFairy) {
                        AbstractFairy fairy = (AbstractFairy)m;
                        if (!(fairy.deathCounter >= AbstractFairy.MAX_DEATHS)) {
                            validMonsters.add(m);
                        }
                    } else {
                        validMonsters.add(m);
                    }
                }
            }

            if (!validMonsters.isEmpty()) {
                this.target = validMonsters.get(AbstractDungeon.aiRng.random(validMonsters.size() - 1));
            } else {
                this.target = this.source;
            }

            if (this.target != null) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SHIELD));
                this.target.addBlock(this.amount);
            }
        }

        this.tickDuration();
    }
}
