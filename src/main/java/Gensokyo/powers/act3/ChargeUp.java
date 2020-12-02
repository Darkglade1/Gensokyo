package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class ChargeUp extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("ChargeUp");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int THRESHOLD = 8;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ChargeUp84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ChargeUp32.png"));

    public ChargeUp(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = 0;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            AbstractPower str = owner.getPower(StrengthPower.POWER_ID);
            if (str != null) {
                if (str.amount > 0) {
                    this.flash();
                    addToBot(new RemoveSpecificPowerAction(owner, owner, StrengthPower.POWER_ID));
                }
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        amount2++;
        if (amount2 % THRESHOLD == 0) {
            this.flash();
            amount2 = 0;
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        }
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + THRESHOLD + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
