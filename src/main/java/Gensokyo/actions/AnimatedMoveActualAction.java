package Gensokyo.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class AnimatedMoveActualAction extends AbstractGameAction {
    AbstractCreature mo;
    private float graphicsAnimation;
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public AnimatedMoveActualAction(AbstractCreature mo, float startX, float startY, float endX, float endY) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.mo = mo;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public void update() {
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        float destinationX = Interpolation.linear.apply(startX, endX, this.graphicsAnimation * 1.0F);
        float destinationY = Interpolation.linear.apply(startY, endY, this.graphicsAnimation * 1.0F);
        if (startX > endX) {
            if (destinationX > endX) {
                mo.drawX = destinationX;
            } else {
                mo.drawX = endX;
            }
        } else if (startX < endX) {
            if (destinationX < endX) {
                mo.drawX = destinationX;
            } else {
                mo.drawX = endX;
            }
        }
        if (startY > endY) {
            if (destinationY > endY) {
                mo.drawY = destinationY;
            } else {
                mo.drawY = endY;
            }
        } else if (startY < endY) {
            if (destinationY < endY) {
                mo.drawY = destinationY;
            } else {
                mo.drawY = endY;
            }
        }
        if (mo.drawX == endX && mo.drawY == endY) {
            this.isDone = true;
        }
    }
}


