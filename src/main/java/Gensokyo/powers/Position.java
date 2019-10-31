package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Reimu;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class Position extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("PositionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float originalY;
    private static float movement = Reimu.orbOffset;

    public Position(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.originalY = owner.drawY;

        this.loadRegion("storm");

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount > 1) {
                this.flash();
                amount--;
                owner.drawY -= movement;
            }
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            if (amount < 3) {
                this.flash();
                amount++;
                owner.drawY += movement;
            }
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        owner.drawY = originalY;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
