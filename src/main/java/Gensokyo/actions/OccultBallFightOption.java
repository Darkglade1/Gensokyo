package Gensokyo.actions;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import static Gensokyo.GensokyoMod.makeUIPath;

public class OccultBallFightOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public OccultBallFightOption() {
        this.label = OccultBallFightOption.TEXT[0];
        this.description = TEXT[1];
        this.img = ImageMaster.loadImage(makeUIPath("Outside.png"));
    }
    
    @Override
    public void useOption() {
        AbstractDungeon.effectList.add(new OccultBallFightEffect());
    }
    
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Gensokyo:Occult Ball Option");
        TEXT = OccultBallFightOption.uiStrings.TEXT;
    }
}