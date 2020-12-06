
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class YumekoEvent3 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("YumekoEvent3");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Alice2.png");

    private static final int STATUS_1 = 3;
    private static final int HIGH_ASC_STATUS_1 = 5;
    private int status1;
    private AbstractCard status1Card = new Dazed();

    private static final int STATUS_2 = 2;
    private static final int HIGH_ASC_STATUS_2 = 3;
    private int status2;
    private AbstractCard status2Card = new VoidCard();

    public YumekoEvent3(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.status1 = HIGH_ASC_STATUS_1;
            this.status2 = HIGH_ASC_STATUS_2;
        } else {
            this.status1 = STATUS_1;
            this.status2 = STATUS_2;
        }
        String option1 = OPTIONS[0] + status1 + OPTIONS[1] + FontHelper.colorString(status1Card.name, "r") + OPTIONS[2];
        String option2 = OPTIONS[3] + status2 + OPTIONS[4] + FontHelper.colorString(status2Card.name, "r") + OPTIONS[5];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(status1Card, status1, true, true));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(status2Card, status2));
                break;
        }
    }


}
