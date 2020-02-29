package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act1.PerfectCherryBlossom;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.Mango;
import com.megacrit.cardcrawl.relics.Pear;
import com.megacrit.cardcrawl.relics.Strawberry;
import com.megacrit.cardcrawl.relics.Waffle;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ABanquetForGhosts extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("ABanquetForGhosts");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Ghost.png");

    private static final float HP_PERCENT = 1.5F;
    private static final float A15_HP_PERCENT = 2.0F;
    private static final int STRAWBERRY = 7;
    private static final int PEAR = 10;
    private static final int MANGO = 14;
    private boolean hasFood;
    private AbstractRelic foodToGive;
    private int foodToGet;
    private AbstractRelic foodToGetRelic;
    private int hpLoss;
    private float hpLossPercent;
    private int screenNum = 0;


    public ABanquetForGhosts() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractRelic> foodOnHand = new ArrayList<>();
        if (p.hasRelic(Strawberry.ID)) {
            foodOnHand.add(p.getRelic(Strawberry.ID));
        }
        if (p.hasRelic(Pear.ID)) {
            foodOnHand.add(p.getRelic(Pear.ID));
        }
        if (p.hasRelic(Mango.ID)) {
            foodOnHand.add(p.getRelic(Mango.ID));
        }
        if (p.hasRelic(Waffle.ID)) {
            foodOnHand.add(p.getRelic(Waffle.ID));
        }
        if (!foodOnHand.isEmpty()) {
            hasFood = true;
            Collections.shuffle(foodOnHand, AbstractDungeon.relicRng.random);
            foodToGive = foodOnHand.get(0);
            this.imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(foodToGive.name, "r") + OPTIONS[4], new PerfectCherryBlossom());
        } else {
            ArrayList<Integer> fruit = new ArrayList<>();
            fruit.add(STRAWBERRY);
            fruit.add(PEAR);
            fruit.add(MANGO);
            Collections.shuffle(fruit, AbstractDungeon.relicRng.random);
            foodToGet = fruit.get(0);
            if (AbstractDungeon.ascensionLevel >= 15) {
                hpLossPercent = A15_HP_PERCENT;
            } else {
                hpLossPercent = HP_PERCENT;
            }
            if (foodToGet == STRAWBERRY) {
                hpLoss = (int)(STRAWBERRY * hpLossPercent);
                foodToGetRelic = new Strawberry();
            } else if (foodToGet == PEAR) {
                hpLoss = (int)(PEAR * hpLossPercent);
                foodToGetRelic = new Pear();
            } else {
                hpLoss = (int)(MANGO * hpLossPercent);
                foodToGetRelic = new Mango();
            }
            this.imageEventText.setDialogOption(OPTIONS[1] + hpLoss + OPTIONS[2] + FontHelper.colorString(foodToGetRelic.name, "g") + ".", foodToGetRelic);
        }

        //Leave
        this.imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Take action
                        if (hasFood) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            screenNum = 1;
                            this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.player.loseRelic(this.foodToGive.relicId);
                            AbstractRelic relic;
                            if (AbstractDungeon.player.hasRelic(PerfectCherryBlossom.ID)) {
                                relic = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                            } else {
                                relic = RelicLibrary.getRelic(PerfectCherryBlossom.ID).makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        } else {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                            screenNum = 2;
                            this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), foodToGetRelic.makeCopy());
                            CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
                            AbstractDungeon.player.damage(new DamageInfo(null, hpLoss));
                        }
                        break;
                    case 1: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }
}
