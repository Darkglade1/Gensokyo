package Gensokyo.actions;

import Gensokyo.monsters.bossRush.Eiki;
import Gensokyo.powers.Guilt;
import Gensokyo.powers.Innocence;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BalanceShiftAction extends AbstractGameAction {
    Eiki eiki;
    private float graphicsAnimation;
    private float startAngle;
    private float endAngle;
    private boolean hasSetValues = false;
    public static final float MAX_ANGLE = 30.0F;

    public BalanceShiftAction(Eiki eiki) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.eiki = eiki;
    }

    @Override
    public void update() {
        if (!hasSetValues) {
            this.startAngle = eiki.angle;
            int guilt = 0;
            int innocence = 0;
            if (AbstractDungeon.player.hasPower(Guilt.POWER_ID)) {
                guilt = AbstractDungeon.player.getPower(Guilt.POWER_ID).amount;
            }
            if (AbstractDungeon.player.hasPower(Innocence.POWER_ID)) {
                innocence = AbstractDungeon.player.getPower(Innocence.POWER_ID).amount;
            }
            int difference = guilt - innocence;
            this.endAngle = difference * ((MAX_ANGLE) / Guilt.THRESHOLD);
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
                CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.2F, -0.3F));
            }
            this.isDone = true;
        }
    }
}


