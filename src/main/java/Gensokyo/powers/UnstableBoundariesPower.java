package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.InvertPowersAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;


public class UnstableBoundariesPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("UnstableBoundariesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int threshold = 5;

    public UnstableBoundariesPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("corruption");

        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        ++this.amount;
        if (this.amount == threshold) {
            this.amount = 0;
            AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(AbstractDungeon.player, false));
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(var3.hasNext()) {
                AbstractMonster mo = (AbstractMonster)var3.next();
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(mo, false));
            }
        }
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0] + threshold + DESCRIPTIONS[1];
    }
}
