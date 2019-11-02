package Gensokyo.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

//Uses the effect class instead of action so other things don't wait on this - speeding things up
public class YinYangMoveAction extends AbstractGameEffect {
    AbstractMonster mo;
    private float graphicsAnimation;
    private float start;
    private float end;

    public YinYangMoveAction(AbstractMonster mo, float start, float end) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
        this.start = start;
        this.end = end;
    }

    @Override
    public void update() {
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        float destination = Interpolation.linear.apply(start, end, this.graphicsAnimation * 4.0F);
        if (destination > end) {
            mo.drawX = destination;
        } else {
            mo.drawX = end;
        }
        if (mo.drawX <= end) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}


