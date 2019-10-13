package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.ManorOfTheDishes;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;


public class ManorOfTheDishesPower extends TwoAmountPower {

    public static final String POWER_ID = GensokyoMod.makeID("ManorOfTheDishesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int cardThreshold = ManorOfTheDishes.cardThreshold;

    public ManorOfTheDishesPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount2 = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("darkembrace");

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        this.flash();
        this.amount2++;
        if (this.amount2 % cardThreshold == 0) {
            this.amount2 = this.amount2 - cardThreshold;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, this.amount), this.amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + cardThreshold + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
