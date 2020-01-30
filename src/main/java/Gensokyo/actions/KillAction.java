package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;

public class KillAction extends AbstractGameAction {

    public KillAction(AbstractMonster m) {
        this.m = m;
    }

    private AbstractMonster m;

    @Override
    public void update() {
        this.m.currentHealth = 0;
        this.m.healthBarUpdatedEvent();
        this.m.useStaggerAnimation();
        AbstractDungeon.effectList.add(new StrikeEffect(this.m, this.m.hb.cX, this.m.hb.cY, 9999));
        this.m.damage(new DamageInfo(null, 0, DamageInfo.DamageType.HP_LOSS));
        this.isDone = true;
    }
}
