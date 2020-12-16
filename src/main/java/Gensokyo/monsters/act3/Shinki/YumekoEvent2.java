
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.BurdenOfFailure;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class YumekoEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("YumekoEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Yumeko2.png");

    private static final int STR = 10;
    private static final int HIGH_ASC_STR = 15;
    private int str;

    private static final int POWER = 30;
    private static final int HIGH_ASC_POWER = 45;
    private int power;

    public YumekoEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.str = HIGH_ASC_STR;
            this.power = HIGH_ASC_POWER;
        } else {
            this.str = STR;
            this.power = POWER;
        }
        String option1 = OPTIONS[0] + power + OPTIONS[1];
        String option2 = OPTIONS[2] + str + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                if (shinki.currentDelusion instanceof Yumeko) {
                    Yumeko yumeko = (Yumeko)shinki.currentDelusion;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(yumeko, yumeko, new BurdenOfFailure(yumeko, power, yumeko), power));
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(shinki.currentDelusion, shinki.currentDelusion, BurdenOfFailure.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shinki.currentDelusion, shinki.currentDelusion, new StrengthPower(shinki.currentDelusion, str), str));
                break;
        }
    }


}
