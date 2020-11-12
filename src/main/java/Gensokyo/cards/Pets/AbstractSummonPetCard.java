package Gensokyo.cards.Pets;

import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractSummonPetCard extends AbstractDefaultCard {
    float scaleWidth = 1.0F * Settings.scale;
    float scaleHeight = Settings.scale;
    public final float PET_X_POSITION = -1300.0f * scaleWidth;
    public final float PET_Y_POSITION = 30.0f * scaleHeight;
    public static final int LARGE_HP = 30;
    public static final int MEDIUM_HP = 25;
    public static final int SMALL_HP = 20;

    public AbstractSummonPetCard(String ID, String IMG, int COST, CardType TYPE, CardColor COLOR, CardRarity RARITY, CardTarget TARGET) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        isInnate = true;
        selfRetain = true;
    }

    public void upgrade() {
    }
}