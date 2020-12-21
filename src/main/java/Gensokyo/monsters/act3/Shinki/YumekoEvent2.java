
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class YumekoEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("YumekoEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Yumeko2.png");

    private static final int CURSE1_NUM = 2;
    private static final int HIGH_ASC_CURSE1_NUM = 3;
    private int curse1Num;
    AbstractCard curse1 = new Regret();

    private static final int CURSE2_NUM = 2;
    private static final int HIGH_ASC_CURSE2_NUM = 3;
    private int curse2Num;
    AbstractCard curse2 = new Normality();

    public YumekoEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.curse1Num = HIGH_ASC_CURSE1_NUM;
            this.curse2Num = HIGH_ASC_CURSE2_NUM;
        } else {
            this.curse1Num = CURSE1_NUM;
            this.curse2Num = CURSE2_NUM;
        }
        String option1 = OPTIONS[0] + curse1Num + OPTIONS[1] + FontHelper.colorString(curse1.name, "r") + OPTIONS[2];
        String option2 = OPTIONS[3] + curse2Num + OPTIONS[4] + FontHelper.colorString(curse2.name, "r") + OPTIONS[5];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(curse1, curse1Num));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(curse2, curse2Num));
                break;
        }
    }


}
