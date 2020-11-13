package Gensokyo.minions;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.cards.Pets.AbstractSummonPetCard;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractPet extends AbstractAnimatedFriendlyMonster {

    protected AbstractSummonPetCard associatedCard;

    public AbstractPet(String name, String id, int max_health, int current_hp, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        super(name, id, max_health, hb_x,  hb_y, hb_w, hb_h, "GensokyoResources/images/monsters/Animals/Intents/blank.png", x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Animals/Spriter/AnimalAnimation.scml");
        this.currentHealth = current_hp;
        System.out.println("current health " + currentHealth);
        System.out.println("passed in current_hp " + current_hp);
    }

    public void setAssociatedCard(AbstractSummonPetCard summonCard) {
        AbstractCard card = StSLib.getMasterDeckEquivalent(summonCard);
        if (card instanceof AbstractSummonPetCard)
            this.associatedCard = (AbstractSummonPetCard) card;
    }

    public void onVictoryUpdateHP() {
        if (associatedCard != null) {
            associatedCard.updateHP(this.currentHealth);
        }
    }

    //Sets the image to a specific animal
    protected void setAnimal(String animal) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animal);
    }
}