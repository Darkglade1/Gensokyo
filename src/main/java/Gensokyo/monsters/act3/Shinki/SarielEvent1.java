
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.SarielEventPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class SarielEvent1 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("SarielEvent1");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sariel1.png");

    private static final int CARD_THRESHOLD = 5;

    public SarielEvent1(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        String option1 = OPTIONS[0] + CARD_THRESHOLD + OPTIONS[1];
        String option2 = OPTIONS[2] + CARD_THRESHOLD + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SarielEventPower(AbstractDungeon.player, CARD_THRESHOLD, AbstractCard.CardType.ATTACK)));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SarielEventPower(AbstractDungeon.player, CARD_THRESHOLD, AbstractCard.CardType.SKILL)));
                break;
        }
    }


}
