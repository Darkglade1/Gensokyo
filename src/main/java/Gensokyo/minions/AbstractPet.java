package Gensokyo.minions;

import Gensokyo.BetterSpriterAnimation;

public class AbstractPet extends AbstractAnimatedFriendlyMonster {

    public AbstractPet(String name, String id, int health, float hb_x, float hb_y, float hb_w, float hb_h, String image, float x, float y) {
        super(name, id, health, hb_x,  hb_y, hb_w, hb_h, image, x, y);
    }

    //Sets the image to a specific animal
    protected void setAnimal(String animal) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animal);
    }
}