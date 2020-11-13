package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import Gensokyo.minions.AbstractPet;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public abstract class AbstractSummonPetCard extends AbstractDefaultCard {
    public static final String TEXT_ID = GensokyoMod.makeID("PetMisc");
    public static final String[] UI_TEXT = CardCrawlGame.languagePack.getUIString(TEXT_ID).TEXT;
    float scaleWidth = 1.0F * Settings.scale;
    float scaleHeight = Settings.scale;
    public final float PET_X_POSITION = -1300.0f * scaleWidth;
    public final float PET_Y_POSITION = 30.0f * scaleHeight;
    public static final int LARGE_HP = 30;
    public static final int MEDIUM_HP = 25;
    public static final int SMALL_HP = 20;

    public int max_hp;

    public AbstractSummonPetCard(String ID, String IMG, int COST, CardType TYPE, CardColor COLOR, CardRarity RARITY, CardTarget TARGET) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        purgeOnUse = true;
        isInnate = true;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(p);
        int minionCount = playerMinions.monsters.size();
        if (BasePlayerMinionHelper.getMaxMinions(p) <= minionCount) {
            BasePlayerMinionHelper.changeMaxMinionAmount(p, minionCount + 1);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (playerHasPet()) {
            this.cantUseMessage = UI_TEXT[0];
            return false;
        } else {
            return canUse;
        }
    }

    @Override
    public void update() {
        super.update();
        //because misc gets set after the card is constructed somewhere and I'm too lazy to track down where and patch there
        magicNumber = baseMagicNumber = misc;
    }

    public void updateHP (int new_hp) {
        magicNumber = baseMagicNumber = misc = new_hp;
    }

    @Override
    public void upgrade() {
    }

    public static boolean playerHasPet() {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(AbstractDungeon.player);
        for (AbstractMonster mo : playerMinions.monsters) {
            if (mo instanceof AbstractPet) {
                return true;
            }
        }
        return false;
    }
}