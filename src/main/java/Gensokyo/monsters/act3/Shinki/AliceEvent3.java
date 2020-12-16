
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AliceEvent3 extends AbstractShinkiEvent{
    public static final String ID = GensokyoMod.makeID("AliceEvent3");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Alice3.png");

    private static final int METALLICIZE = 15;
    private static final int HIGH_METALLICIZE = 25;
    private int metallicize;

    private static final int ENEMY_HEAL = 60;
    private static final int HIGH_ASC_ENEMY_HEAL = 90;
    private int enemyHeal;

    public AliceEvent3(Shinki shinki) {
        this.title = NAME;
        this.bodyText = DESCRIPTIONS[0];
        this.image = IMG;
        this.shinki = shinki;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.metallicize = HIGH_METALLICIZE;
            this.enemyHeal = HIGH_ASC_ENEMY_HEAL;
        } else {
            this.metallicize = METALLICIZE;
            this.enemyHeal = ENEMY_HEAL;
        }
        String option1 = OPTIONS[0] + metallicize + OPTIONS[1];
        String option2 = OPTIONS[2] + enemyHeal + OPTIONS[3];
        this.options.add(option1);
        this.options.add(option2);
    }

    public void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shinki.currentDelusion, shinki.currentDelusion, new MetallicizePower(shinki.currentDelusion, metallicize), metallicize));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new HealAction(shinki.currentDelusion, shinki.currentDelusion, enemyHeal));
                break;
        }
    }


}
