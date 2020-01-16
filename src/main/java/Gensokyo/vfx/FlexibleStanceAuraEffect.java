package Gensokyo.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlexibleStanceAuraEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private AtlasRegion img;
    public static boolean switcher = true;

    public FlexibleStanceAuraEffect(String stanceId, AbstractCreature creature) {
        this.img = ImageMaster.EXHAUST_L;
        this.duration = 2.0F;
        this.scale = MathUtils.random(2.7F, 2.5F) * Settings.scale;
        if (stanceId.equals(WrathStance.STANCE_ID)) {
            this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.1F, 0.2F), 0.0F);
        } else if (stanceId.equals(CalmStance.STANCE_ID)) {
            this.color = new Color(MathUtils.random(0.5F, 0.55F), MathUtils.random(0.6F, 0.7F), 1.0F, 0.0F);
        } else {
            this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.6F, 0.7F), 0.0F);
        }

        this.x = creature.hb.cX + MathUtils.random(-creature.hb.width / 16.0F, creature.hb.width / 16.0F);
        this.y = creature.hb.cY + MathUtils.random(-creature.hb.height / 16.0F, creature.hb.height / 12.0F);
        this.x -= (float)this.img.packedWidth / 2.0F;
        this.y -= (float)this.img.packedHeight / 2.0F;
        switcher = !switcher;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }

    }

    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }

        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
