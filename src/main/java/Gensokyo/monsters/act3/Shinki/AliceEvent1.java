
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.AliceEventEnergy;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.DrawPower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AliceEvent1 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("AliceEvent1");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Alice1.png");

    private static final int DRAW = 1;
    private static final int ENERGY_TURN = 3;
    private static final int ENERGY_AMT = 3;

    public AliceEvent1(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        String option1 = OPTIONS[0] + DRAW + OPTIONS[1];
        String option2 = OPTIONS[2] + ENERGY_TURN + OPTIONS[3] + ENERGY_AMT + OPTIONS[4];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawPower(AbstractDungeon.player, DRAW), DRAW));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AliceEventEnergy(AbstractDungeon.player, ENERGY_TURN, ENERGY_AMT)));
                break;
        }
    }


}
