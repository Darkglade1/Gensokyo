package Gensokyo.cards.Item;

import Gensokyo.cards.AbstractShopSpecialCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractItemCard extends AbstractShopSpecialCard {

    public AbstractItemCard(String ID, String IMG, int COST, CardType TYPE, CardRarity RARITY, CardTarget TARGET) {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard card = super.makeCopy();
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            card.uuid = this.uuid; //copy uuid so copies still yeet the same card from master deck
            //we have a in-combat check so this only applies to copies made in combat
            //copies from dollys mirror and other out of combat sources should still get a new uuid
        }
        return card;
    }

}