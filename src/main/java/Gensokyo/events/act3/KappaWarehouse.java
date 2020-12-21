package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.rooms.nitori.helpers.gensokyoCardHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;

public class KappaWarehouse extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("KappaWarehouse");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("KappaWarehouse.png");

    private int screenNum = 0;
    private AbstractCard item1;
    private AbstractCard item2;

    public KappaWarehouse() {
        super(NAME, DESCRIPTIONS[0], IMG);

        ArrayList<AbstractCard> itemCards = gensokyoCardHelper.getAllItemCards();
        Collections.shuffle(itemCards, AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> selectedCards = (new ArrayList<>(itemCards.subList(0, 2)));
        item1 = selectedCards.get(0);
        item2 = selectedCards.get(1);

        this.imageEventText.setDialogOption(OPTIONS[0] + FontHelper.colorString(item1.name, "g") + OPTIONS[1], item1);
        this.imageEventText.setDialogOption(OPTIONS[0] + FontHelper.colorString(item2.name, "g") + OPTIONS[1], item2);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[2]);
                        screenNum = 1;
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(item1, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[2]);
                        screenNum = 1;
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(item2, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[2]);
                        screenNum = 1;
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }

}
