package Gensokyo.actions;

import Gensokyo.relics.PerfectCherryBlossom;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBubbleEffect;

import static Gensokyo.GensokyoMod.makeUIPath;

public class IngredientListBrewOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    
    public IngredientListBrewOption() {
        this.label = IngredientListBrewOption.TEXT[0];
        this.description = TEXT[1];
        this.img = ImageMaster.loadImage(makeUIPath("brew.png"));
    }
    
    @Override
    public void useOption() {
        AbstractDungeon.effectList.add(new CampfireBubbleEffect(true));
        AbstractRelic relic = RelicLibrary.getRelic(PerfectCherryBlossom.ID).makeCopy();
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
    }
    
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Gensokyo:Brew Option");
        TEXT = IngredientListBrewOption.uiStrings.TEXT;
    }
}