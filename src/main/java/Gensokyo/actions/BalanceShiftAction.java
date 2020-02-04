package Gensokyo.actions;

import Gensokyo.monsters.bossRush.Eiki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

//Uses the effect class instead of action so other things don't wait on this - speeding things up
public class BalanceShiftAction extends AbstractGameEffect {
    Eiki eiki;
    private float graphicsAnimation;
    private float startAngle;
    private float endAngle;

    public BalanceShiftAction(Eiki eiki, float startAngle, float endAngle) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.eiki = eiki;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    @Override
    public void update() {
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        float destination = Interpolation.linear.apply(startAngle, endAngle, this.graphicsAnimation * 4.0F);
        if (startAngle > endAngle) {
            if (destination > endAngle) {
                eiki.angle = destination;
            } else {
                eiki.angle = endAngle;
            }
        } else if (startAngle < endAngle) {
            if (destination < endAngle) {
                eiki.angle = destination;
            } else {
                eiki.angle = endAngle;
            }
        }

        if (eiki.angle == endAngle) {
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


