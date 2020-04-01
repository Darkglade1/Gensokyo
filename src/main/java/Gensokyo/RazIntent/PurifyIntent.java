package Gensokyo.RazIntent;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeUIPath;

public class PurifyIntent extends CustomIntent {

    public static final String ID = GensokyoMod.makeID("PurifyIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public PurifyIntent() {
        super(IntentEnums.PURIFY, TEXT[0],
                makeUIPath("PurifyIntent_L.png"),
                makeUIPath("PurifyIntent.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return TEXT[1];
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}