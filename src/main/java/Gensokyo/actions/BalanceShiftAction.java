package Gensokyo.actions;

import Gensokyo.monsters.bossRush.Eiki;
import Gensokyo.powers.Guilt;
import Gensokyo.powers.Innocence;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DeathScreen;

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
            int guilt = 0;
            int innocence = 0;
            if (AbstractDungeon.player.hasPower(Guilt.POWER_ID)) {
                guilt = AbstractDungeon.player.getPower(Guilt.POWER_ID).amount;
            }
            if (AbstractDungeon.player.hasPower(Innocence.POWER_ID)) {
                innocence = AbstractDungeon.player.getPower(Innocence.POWER_ID).amount;
            }
            difference = guilt - innocence;
            this.endAngle = difference * ((MAX_ANGLE) / eiki.guiltThreshold);
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
            if (difference >= eiki.guiltThreshold) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(eiki, Eiki.DIALOG[2]));
                AbstractDungeon.player.isDead = true;
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
                AbstractDungeon.actionManager.clearPostCombatActions();
            } else if (difference < 0) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(eiki, Eiki.DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new SetFlipAction(eiki));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(eiki));
            }
            this.isDone = true;
        }
    }
}


