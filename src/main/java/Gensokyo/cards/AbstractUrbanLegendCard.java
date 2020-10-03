package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.util.AssetLoader;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Method;

import static Gensokyo.rooms.nitori.helpers.gensokyoCardHelper.addCard;
import static Gensokyo.rooms.nitori.helpers.gensokyoCardHelper.getUrbanLegendCards;

public abstract class AbstractUrbanLegendCard extends AbstractDefaultCard {
    public static TextureAtlas.AtlasRegion[] frames;
    public static final CardColor COLOR = GensokyoMod.Enums.URBAN_LEGEND;
    public AbstractUrbanLegendCard(final String id,
                                   final String img,
                                   final int cost,
                                   final CardType type,
                                   final CardRarity rarity,
                                   final CardTarget target) {
        super(id, img, cost, type, COLOR, rarity, target);
        this.setBannerTexture("GensokyoResources/images/512/banner_blacky.png", "GensokyoResources/images/1024/banner_blacky.png");
        if(frames == null) {
            frames = new TextureAtlas.AtlasRegion[6];
            frames[0] = regionFromTexture("GensokyoResources/images/512/attackframe_blacky.png");
            frames[1] = regionFromTexture("GensokyoResources/images/512/skillframe_blacky.png");
            frames[2] = regionFromTexture("GensokyoResources/images/512/powerframe_blacky.png");
            frames[3] = regionFromTexture("GensokyoResources/images/1024/attackframe_blacky.png");
            frames[4] = regionFromTexture("GensokyoResources/images/1024/skillframe_blacky.png");
            frames[5] = regionFromTexture("GensokyoResources/images/1024/powerframe_blacky.png");
        }
        if(!getUrbanLegendCards().contains(this)){ addCard(this); }
    }

    @SpireOverride
    protected void renderAttackPortrait(SpriteBatch sb, float x, float y) {
        try {
            Method renderHelper = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(this, sb,
                    ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"),
                    frames[0], x, y);
        }catch(Exception ex) {}
    }

    @SpireOverride
    protected void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        try {
            Method renderHelper = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(this, sb,
                    ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"),
                    frames[1], x, y);
        }catch(Exception ex) {}
    }

    @SpireOverride
    protected void renderPowerPortrait(SpriteBatch sb, float x, float y) {
        try {
            Method renderHelper = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(this, sb,
                    ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"),
                    frames[2], x, y);
        }catch(Exception ex) {}
    }

    private TextureAtlas.AtlasRegion regionFromTexture(String tex) {
        Texture texture = AssetLoader.loadImage(tex);
        return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }
}