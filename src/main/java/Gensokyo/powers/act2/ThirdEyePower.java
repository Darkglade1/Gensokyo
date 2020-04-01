package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;


public class ThirdEyePower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("ThirdEyePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractRelic relic;

    public ThirdEyePower(final AbstractCreature owner, AbstractRelic relic) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.relic = relic;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.loadRegion("focus");

        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
        updateDescription();
    }

    @Override
    public void onVictory() {
        AbstractDungeon.player.loseRelic(this.relic.relicId);
    }

    // Update the description when you apply this power.
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
