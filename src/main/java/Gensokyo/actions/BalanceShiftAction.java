package Gensokyo.actions;

import Gensokyo.monsters.act2.Eiki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

public class BalanceShiftAction extends AbstractGameAction {
    Eiki eiki;
    private float graphicsAnimation;
    private float startAngle;
    private float endAngle;
    private boolean hasSetValues = false;
    public static final float MAX_ANGLE = 30.0F;
    private int difference;

    public BalanceShiftAction(Eiki eiki) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.eiki = eiki;
    }

    @Override
    public void update() {
        if (!hasSetValues) {
            this.startAngle = eiki.angle;
            int guilt = eiki.guiltCount;
            int innocence = eiki.innocenceCount;
            difference = guilt - innocence;
            this.endAngle = difference * ((MAX_ANGLE) / 20);
            if (this.endAngle > MAX_ANGLE) {
                this.endAngle = MAX_ANGLE;
            }
            hasSetValues = true;
        }
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        float destination = Interpolation.linear.apply(startAngle, endAngle, this.graphicsAnimation * 2.0F);
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
            if (startAngle != endAngle) {
                CardCrawlGame.sound.playAV("BELL", MathUtils.random(-0.2F, -0.3F), 0.4F);
            }
            this.isDone = true;
        }
    }
}


