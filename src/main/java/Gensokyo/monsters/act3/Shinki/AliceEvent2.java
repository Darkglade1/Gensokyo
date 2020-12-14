
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AliceEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("AliceEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Alice2.png");

    private static final int STR = 3;
    private static final int HIGH_ASC_STR = 5;
    private int str;

    private static final int METALLICIZE = 10;
    private static final int HIGH_ASC_METALLICIZE = 15;
    private int metallicize;

    public AliceEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.str = HIGH_ASC_STR;
            this.metallicize = HIGH_ASC_METALLICIZE;
        } else {
            this.str = STR;
            this.metallicize = METALLICIZE;
        }
        String option1 = OPTIONS[0] + str + OPTIONS[1];
        String option2 = OPTIONS[2] + metallicize + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shinki.currentDelusion, shinki.currentDelusion, new StrengthPower(shinki.currentDelusion, str), str));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shinki.currentDelusion, shinki.currentDelusion, new MetallicizePower(shinki.currentDelusion, metallicize), metallicize));
                break;
        }
    }


}
