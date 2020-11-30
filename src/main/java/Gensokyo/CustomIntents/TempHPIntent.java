package Gensokyo.CustomIntents;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import actlikeit.RazIntent.CustomIntent;

import static Gensokyo.GensokyoMod.makeUIPath;

public class TempHPIntent extends CustomIntent {

    public static final String ID = GensokyoMod.makeID(TempHPIntent.class.getSimpleName());

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public TempHPIntent() {
        super(IntentEnums.TEMPHP, TEXT[0],
                makeUIPath("tempHp_L.png"),
                makeUIPath("tempHp.png"));
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