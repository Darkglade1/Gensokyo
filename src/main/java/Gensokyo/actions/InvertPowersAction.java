package Gensokyo.actions;

import Gensokyo.powers.act1.FortitudePower;
import Gensokyo.powers.act1.SturdyPower;
import Gensokyo.powers.act1.VigorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class InvertPowersAction extends AbstractGameAction {
    private AbstractCreature target;
    private boolean debuffsOnly;

    public InvertPowersAction(AbstractCreature target, boolean debuffsOnly) {
        this.actionType = ActionType.POWER;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.target = target;
        this.debuffsOnly = debuffsOnly;
    }

    public void update() {
        for (AbstractPower power : target.powers) {
            if (power.canGoNegative) {
                if (power.amount > 0) {
                    if (!debuffsOnly) {
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, power, -power.amount * 2));
                    }
                } else {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, power, -power.amount * 2));
                }
            }
            else if (power instanceof VulnerablePower) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target, target, VulnerablePower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new FortitudePower(target, power.amount), power.amount));
            }
            else if (power instanceof WeakPower) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, WeakPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new VigorPower(target, power.amount), power.amount));
            }
            else if (power instanceof FrailPower) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, FrailPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new SturdyPower(target, power.amount), power.amount));
            }
            else if (power instanceof FortitudePower && !debuffsOnly) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, FortitudePower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new VulnerablePower(target, power.amount, false), power.amount));
            }
            else if (power instanceof VigorPower && !debuffsOnly) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, VigorPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new WeakPower(target, power.amount, false), power.amount));
            }
            else if (power instanceof SturdyPower && !debuffsOnly) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, SturdyPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new FrailPower(target, power.amount, false), power.amount));
            }
            else if (power instanceof GainStrengthPower && !debuffsOnly) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, GainStrengthPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new LoseStrengthPower(target, power.amount), power.amount));
            }
            else if (power instanceof LoseStrengthPower) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, LoseStrengthPower.POWER_ID, power.amount));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new GainStrengthPower(target, power.amount), power.amount));
            }
        }
        this.isDone = true;
    }
}
