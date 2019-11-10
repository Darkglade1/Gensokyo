package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


public class FairyFury extends AbstractPower implements OnReceivePowerPower {

    public static final String POWER_ID = GensokyoMod.makeID("FairyFury");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Evasive84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Evasive32.png"));

    public FairyFury(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("anger");

        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onTrigger() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, amount), amount));
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == this.owner && this.owner.halfDead) {
            if (power instanceof StrengthPower) { //Allows her to only gain Strength while half dead
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRemove() {
        //In case the buff somehow gets removed
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new FairyFury(this.owner, 1)));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
