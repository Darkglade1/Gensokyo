package Gensokyo.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.PetalEffect;

public class CherryBlossomEffect extends AbstractGameEffect {
    private float timer = 0.1F;

    public CherryBlossomEffect() {
        this.duration = 2.0F;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            this.timer += 0.1F;
            AbstractDungeon.effectsQueue.add(new PetalEffect());
            AbstractDungeon.effectsQueue.add(new PetalEffect());
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
