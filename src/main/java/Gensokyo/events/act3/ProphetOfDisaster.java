package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ProphetOfDisaster extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("ProphetOfDisaster");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Iku.png");

    private int screenNum = 0;

    private static final float HEALTH_LOSS_PERCENTAGE = 0.20F;
    private static final float HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.25F;

    private static final float RARE_HEALTH_LOSS_PERCENTAGE = 0.40F;
    private static final float RARE_HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION = 0.50F;

    private static final int NUM_OPTIONS = 3;
    private ArrayList<AbstractRelic> relics = new ArrayList<>();

    private int healthLoss;
    private int rareHealthLoss;

    public ProphetOfDisaster() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel < 15) {
            healthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
            rareHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * RARE_HEALTH_LOSS_PERCENTAGE);
        } else {
            healthLoss = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
            rareHealthLoss = (int) ((float) AbstractDungeon.player.maxHealth * RARE_HEALTH_LOSS_PERCENTAGE_HIGH_ASCENSION);
        }

        this.imageEventText.setDialogOption(OPTIONS[9]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[1] + rareHealthLoss + OPTIONS[2] + OPTIONS[3] + NUM_OPTIONS + OPTIONS[4] + OPTIONS[7]);
                this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[1] + healthLoss + OPTIONS[2] + OPTIONS[3] + NUM_OPTIONS + OPTIONS[5] + OPTIONS[7]);
                this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[3] + NUM_OPTIONS + OPTIONS[6] + OPTIONS[7]);
                break;
            case 1:
                screenNum = 2;
                AbstractRelic.RelicTier tier;
                if (buttonPressed == 0) {
                    tier = AbstractRelic.RelicTier.RARE;
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                    CardCrawlGame.sound.play("ATTACK_POISON");
                    AbstractDungeon.player.damage(new DamageInfo(null, rareHealthLoss));
                } else if (buttonPressed == 1) {
                    tier = AbstractRelic.RelicTier.UNCOMMON;
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                    CardCrawlGame.sound.play("ATTACK_POISON");
                    AbstractDungeon.player.damage(new DamageInfo(null, healthLoss));
                } else {
                    tier = AbstractRelic.RelicTier.COMMON;
                }
                generateRelics(tier);
                break;
            case 2:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relics.get(buttonPressed));
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                this.imageEventText.clearRemainingOptions();
                break;
            default:
                this.openMap();
        }
    }

    private void generateRelics(AbstractRelic.RelicTier rarity) {
        for (int i = 0; i < NUM_OPTIONS; i++) {
            AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(rarity);
            relics.add(relic);
        }
        this.imageEventText.clearAllDialogs();
        for (AbstractRelic relic : relics) {
            this.imageEventText.setDialogOption(FontHelper.colorString(relic.name, "g"), relic);
        }
    }
}
