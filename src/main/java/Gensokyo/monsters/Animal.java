package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;

//Dummy monster just so we can use it for its animations
public class Animal extends CustomMonster {

    public Animal() {
        this(0.0F, 0.0F, null);
    }

    public Animal(float x, float y, String animal) {
        super("", "", 1, 0.0F, 0.0F, 160.0F, 120.0F, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Animals/Spriter/AnimalAnimation.scml");
        if (animal != null) {
            setAnimal(animal);
        }
    }

    //Sets the image to a specific animal
    public void setAnimal(String animal) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animal);
    }

    @Override
    public void takeTurn() {

    }

    @Override
    public void getMove(int num) {

    }
}
