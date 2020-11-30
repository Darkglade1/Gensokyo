package Gensokyo.RazIntent;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import actlikeit.RazIntent.CustomIntent;

import static Gensokyo.GensokyoMod.makeUIPath;

public class CurseAttackIntent extends CustomIntent {

    public static final String ID = GensokyoMod.makeID("CurseAttackIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public CurseAttackIntent() {
        super(IntentEnums.ATTACK_CURSE, TEXT[0],
                makeUIPath("curseIntent_L.png"),
                makeUIPath("curseIntent.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result += mo.getIntentDmg();
        result += TEXT[2];

        return result;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}