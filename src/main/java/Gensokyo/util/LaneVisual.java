package Gensokyo.util;

import Gensokyo.powers.act3.SistersPlayerPosition;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;

import static Gensokyo.GensokyoMod.makeUIPath;

public class LaneVisual {
    public static ShapeRenderer renderer = new ShapeRenderer();

    public static Color red = new Color(1.0F, 0.3F, 0.3F, 0.5F);
    private static final Texture attackerIcon = new Texture(makeUIPath("AttackIcon.png"));

    public static void drawArrow(SpriteBatch sb, Hitbox h1, Hitbox h2, float vDist) {
        sb.end();

        Vector2 start = new Vector2(h1.cX, h1.cY);
        Vector2 end = new Vector2(h2.cX, h2.cY);
        Vector2 control = new Vector2((h1.cX + h2.cX) / 2.0F, (h1.cY + h2.cY) / 2.0F + vDist);

        Vector2 nstart = new Vector2(start.y - control.y, control.x - start.x);
        Vector2 nend = new Vector2(control.y - end.y, end.x - control.x);

        Vector2 tmp = end.cpy();
        int segs = (int) (tmp.sub(start).len() / Settings.scale / 10.0F);
        if (segs < 2) segs = 2;

        Vector2 v1 = start.cpy();
        Vector2 v2 = new Vector2();
        Vector2 n1 = nstart.cpy().nor().scl(10.0F * Settings.scale);

        boolean blendDisabled = false;
        if (!Gdx.gl.glIsEnabled(3042)) {
            blendDisabled = true;
            Gdx.gl.glEnable(3042);
            Gdx.gl.glBlendFunc(770, 771);
        }
        renderer.setProjectionMatrix(sb.getProjectionMatrix().cpy());
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Color.WHITE.cpy());
        for (int i = 0; i < segs - 1; i++) {
            float t2 = (i + 1) / segs;
            Bezier.quadratic(v2, t2, start, control, end, tmp);
            tmp.set(MathUtils.lerp(nstart.x, nend.x, t2), MathUtils.lerp(nstart.y, nend.y, t2)).nor().scl(10.0F * Settings.scale);

            if (i == 0) {
                renderer.triangle(v1.x - n1.x, v1.y - n1.y, v2.x - tmp.x, v2.y - tmp.y, v2.x, v2.y, red, red, red);
                renderer.triangle(v1.x + n1.x, v1.y + n1.y, v2.x, v2.y, v2.x + tmp.x, v2.y + tmp.y, red, red, red);
            } else {
                renderer.triangle(v1.x - n1.x, v1.y - n1.y, v2.x - tmp.x, v2.y - tmp.y, v1.x + n1.x, v1.y + n1.y, red, red, red);
                renderer.triangle(v2.x - tmp.x, v2.y - tmp.y, v2.x + tmp.x, v2.y + tmp.y, v1.x + n1.x, v1.y + n1.y, red, red, red);
            }
            
            n1.set(tmp);
            v1.set(v2);
        }

        renderer.triangle(v2.x - n1.x, v2.y - n1.y, end.x, end.y, v2.x + n1.x, v2.y + n1.y, red, red, red);

        renderer.end();
        if (blendDisabled) {
            Gdx.gl.glDisable(3042);
        }

        Bezier.quadratic(v2, 0.5F, start, control, end, tmp);

        sb.begin();

        sb.setColor(Color.WHITE);
        if (((SistersPlayerPosition) AbstractDungeon.player.getPower(SistersPlayerPosition.POWER_ID)).isInUnsafeLane()) {
            sb.draw(attackerIcon, AbstractDungeon.player.hb.cX - 64.0F, AbstractDungeon.player.hb.cY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
        } else {
            sb.draw(attackerIcon, v2.x, v2.y - 100.0F * Settings.scale, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
        }
    }
}