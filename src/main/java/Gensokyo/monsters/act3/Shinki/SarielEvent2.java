
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.HexPower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class SarielEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("SarielEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sariel2.png");

    private static final int DEBUFF1_AMT = 5;
    private static final int HIGH_ASC_DEBUFF1_AMT = 8;
    private int debuff1Amt;

    private static final int DEBUFF2_AMT = 1;
    private static final int HIGH_ASC_DEBUFF2_AMT = 2;
    private int debuff2Amt;

    public SarielEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            debuff1Amt = HIGH_ASC_DEBUFF1_AMT;
            debuff2Amt = HIGH_ASC_DEBUFF2_AMT;
        } else {
            debuff1Amt = DEBUFF1_AMT;
            debuff2Amt = DEBUFF2_AMT;
        }
        String option1 = OPTIONS[0] + debuff1Amt + OPTIONS[1];
        String option2 = OPTIONS[2] + debuff2Amt + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConstrictedPower(AbstractDungeon.player, AbstractDungeon.player, debuff1Amt),debuff1Amt));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HexPower(AbstractDungeon.player, debuff2Amt),debuff2Amt));
                break;
        }
    }


}
