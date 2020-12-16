
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.DollMagnet;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AliceEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("AliceEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Alice2.png");

    private static final int TURNS = 1;
    private static final int NUM_DOLLS = 1;
    private static final int HIGH_ASC_NUM_DOLLS = 2;
    private int numDolls;

    private static final int CURSE_AMT = 2;
    private static final int HIGH_ASC_CURSE_AMT = 3;
    private int curseAmt;
    AbstractCard curse = new Shame();

    public AliceEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.numDolls = HIGH_ASC_NUM_DOLLS;
            this.curseAmt = HIGH_ASC_CURSE_AMT;
        } else {
            this.numDolls = NUM_DOLLS;
            this.curseAmt = CURSE_AMT;
        }
        String option1 = OPTIONS[0] + OPTIONS[1] + numDolls + OPTIONS[2];
        String option2 = OPTIONS[3] + curseAmt + OPTIONS[4] + FontHelper.colorString(curse.name, "r") + OPTIONS[5];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                Alice alice;
                if (shinki.currentDelusion instanceof Alice) {
                    alice = (Alice)shinki.currentDelusion;
                } else {
                    alice = null;
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DollMagnet(AbstractDungeon.player, numDolls, TURNS, alice), numDolls));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(curse, curseAmt));
                break;
        }
    }


}
