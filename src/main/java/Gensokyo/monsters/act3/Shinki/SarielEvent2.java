
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.VigorPower;
import Gensokyo.powers.act3.ExposedBack;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Gensokyo.GensokyoMod.makeEventPath;

public class SarielEvent2 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("SarielEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Sariel2.png");

    private static final int DAMAGE_BOOST = 34;

    public SarielEvent2(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        String option1 = OPTIONS[0];
        String option2 = OPTIONS[1] + DAMAGE_BOOST + OPTIONS[2];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                if (shinki.currentDelusion instanceof Sariel) {
                    Sariel sariel = (Sariel)shinki.currentDelusion;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(sariel, sariel, ExposedBack.POWER_ID));
                    sariel.target = AbstractDungeon.player;
                    sariel.setFlip(false, false);
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shinki.currentDelusion, shinki.currentDelusion, new VigorPower(shinki.currentDelusion, 99, true),99));
                break;
        }
    }


}
