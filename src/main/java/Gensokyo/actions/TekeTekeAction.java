package Gensokyo.actions;

import Gensokyo.cards.TekeTeke;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class TekeTekeAction extends AbstractGameAction {
    public int[] damage;
    private boolean firstFrame;
    TekeTeke card;

    public TekeTekeAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect, TekeTeke card, boolean isFast) {
        this.firstFrame = true;
        this.setValues(null, source, amount[0]);
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        this.card = card;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }

    }

    public TekeTekeAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect, TekeTeke card) {
        this(source, amount, type, effect, card,false);
    }

    public void update() {
        if (this.firstFrame) {
            boolean playedMusic = false;

            for(int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); ++i) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isDying && AbstractDungeon.getCurrRoom().monsters.monsters.get(i).currentHealth > 0 && !AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            Iterator var4 = AbstractDungeon.player.powers.iterator();

            while(var4.hasNext()) {
                AbstractPower p = (AbstractPower)var4.next();
                p.onDamageAllEnemies(this.damage);
            }

            int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for(int i = 0; i < temp; ++i) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.color.set(Color.CHARTREUSE);
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AttackEffect.FIRE) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.color.set(Color.RED);
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.changeColor(Color.WHITE.cpy());
                    }
                    AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                    boolean aboveHalf = false;
                    boolean belowHalf = false;
                    int threshold = (int) Math.ceil(((double)target.maxHealth) / 2);
                    if (target.currentHealth >= threshold) {
                        aboveHalf = true;
                    }
                    target.damage(new DamageInfo(this.source, this.damage[i], this.damageType));
                    if (target.currentHealth < threshold) {
                        belowHalf = true;
                    }
                    if (aboveHalf && belowHalf && !card.triggered) {
                        card.triggered = true;
                        System.out.println(card.baseDamage);
                        AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(card.uuid, card.magicNumber));
                    }
                }
            }


            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
