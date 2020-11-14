package Gensokyo.cards.Pets;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import Gensokyo.relics.Companionship;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import static Gensokyo.minions.PetUtils.playerHasPet;

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
        if (!AbstractDungeon.player.hasRelic(Companionship.ID)) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new Companionship());
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

    @Override
    public AbstractCard makeCopy() {
        AbstractCard card = super.makeCopy();
        card.uuid = this.uuid; //copy uuid so copies still yeet the same card from master deck
        return card;
    }

}