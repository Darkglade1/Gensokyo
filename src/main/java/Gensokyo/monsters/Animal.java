package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

//Dummy monster just so we can use it for its animations
public class Animal extends CustomMonster {

    private String animationsuffix;

    public Animal() {
        this(0.0F, 0.0F);
    }

    public Animal(float x, float y) {
        super("", "", 1, 0.0F, 0.0F, 160.0F, 120.0F, null, x, y);
        if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.IRONCLAD) {
            this.loadAnimation("GensokyoResources/images/monsters/Animals/Ironcluck/ironcluck.atlas", "GensokyoResources/images/monsters/Animals/Ironcluck/ironcluck.json", 6F);
            this.animationsuffix = "";
            if(Loader.isModLoaded("cowboy-ironclad")) {
                this.animationsuffix = "_Hat";
            }
            this.stateData.setMix("Hit" + this.animationsuffix, "Idle" + this.animationsuffix, 0.1F);
            this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
        } else {
            this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Animals/Spriter/AnimalAnimation.scml");
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
