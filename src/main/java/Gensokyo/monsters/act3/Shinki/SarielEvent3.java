
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class SarielEvent3 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("SarielEvent3");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sariel3.png");

    private static final int DEBUFFS = 3;
    private static final int ENGTANGLE = 1;

    public SarielEvent3(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        String option1 = OPTIONS[0] + DEBUFFS + OPTIONS[1];
        String option2 = OPTIONS[2] + ENGTANGLE + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, shinki.currentDelusion, new WeakPower(AbstractDungeon.player, DEBUFFS, true), DEBUFFS));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, shinki.currentDelusion, new FrailPower(AbstractDungeon.player, DEBUFFS, true), DEBUFFS));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, shinki.currentDelusion, new EntanglePower(AbstractDungeon.player)));
                break;
        }
    }


}
