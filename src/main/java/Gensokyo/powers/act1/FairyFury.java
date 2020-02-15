package Gensokyo.powers.act1;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

import java.util.Collections;


public class FairyFury extends AbstractPower  {

    public static final String POWER_ID = GensokyoMod.makeID("FairyFury");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final UIStrings uiStrings;
    public static final String[] TEXT;

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

    //hacky stuff to let her gain Strength while half-dead. Also comes with the added benefit of not allowing other powers/relics to interfere with this, decreasing the likelihood of the player getting softlocked for some stupid reason.
    public void onTrigger() {
        if (!this.owner.hasPower(StrengthPower.POWER_ID)) {
            AbstractPower powerToApply = new StrengthPower(this.owner, this.amount);
            this.owner.powers.add(powerToApply);
            Collections.sort(this.owner.powers);
            powerToApply.onInitialApplication();
            powerToApply.flash();
            AbstractDungeon.effectList.add(new PowerBuffEffect(this.owner.hb.cX - this.owner.animX, this.owner.hb.cY + this.owner.hb.height / 2.0F, powerToApply.name));
            AbstractDungeon.onModifyPower();
        } else {
            AbstractPower strength = this.owner.getPower(StrengthPower.POWER_ID);
            strength.stackPower(this.amount);
            strength.flash();
            AbstractDungeon.effectList.add(new PowerBuffEffect(this.owner.hb.cX - this.owner.animX, this.owner.hb.cY + this.owner.hb.height / 2.0F, "+" + Integer.toString(this.amount) + " " + strength.name));
            strength.updateDescription();
            AbstractDungeon.onModifyPower();
        }
    }

    @Override
    public void onRemove() {
        //In case the buff somehow gets removed
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new FairyFury(this.owner, 1)));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ApplyPowerAction");
        TEXT = uiStrings.TEXT;
    }
}
