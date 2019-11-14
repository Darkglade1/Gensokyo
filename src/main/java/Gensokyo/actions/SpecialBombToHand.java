package Gensokyo.actions;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class SpecialBombToHand extends AbstractGameAction {

    public SpecialBombToHand() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractCard c = new TheBomb();
            AlwaysRetainField.alwaysRetain.set(c, true);
            c.rawDescription = "Retain. NL " + c.rawDescription;

            if (!c.exhaust) {
                c.exhaust = true;
                c.rawDescription = c.rawDescription + " NL Exhaust.";
            }
            c.initializeDescription();
            AbstractDungeon.actionManager.addToBottom(new MakeSpecialStatEquivalentCopy(c));
            this.tickDuration();
        }
        this.isDone = true;
    }

    public class MakeSpecialStatEquivalentCopy extends AbstractGameAction {
        private AbstractCard c;

        public MakeSpecialStatEquivalentCopy(AbstractCard c) {
            this.actionType = ActionType.CARD_MANIPULATION;
            this.duration = Settings.ACTION_DUR_FAST;
            this.c = c;

        }

        public void update() {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
                tickDuration();
                this.isDone = true;
            }
        }
    }

}