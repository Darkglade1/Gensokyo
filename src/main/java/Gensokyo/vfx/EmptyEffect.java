package Gensokyo.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

//Exists solely so I can use it in a VFX action as a pseudo wait action since the actual wait action doesn't seem to do anything
public class EmptyEffect
        extends AbstractGameEffect {

    private static final float DURATION = 0.0f;

    public EmptyEffect() {
        this.duration = this.startingDuration = DURATION;
    }


    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {
    }
}


