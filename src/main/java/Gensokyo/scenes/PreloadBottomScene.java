//package Gensokyo.scenes;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.math.MathUtils;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.helpers.ImageMaster;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//import com.megacrit.cardcrawl.rooms.EventRoom;
//import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
//import com.megacrit.cardcrawl.rooms.RestRoom;
//import com.megacrit.cardcrawl.scenes.AbstractScene;
//import com.megacrit.cardcrawl.vfx.scene.*;
//import Gensokyo.util.AssetLoader;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class PreloadBottomScene extends AbstractScene {
//    public static String atlasUrl = "bottomScene/scene.atlas";
//    private boolean renderLeftWall;
//    private boolean renderSolidMid;
//    private boolean renderHollowMid;
//    private TextureAtlas.AtlasRegion fg;
//    private TextureAtlas.AtlasRegion mg;
//    private TextureAtlas.AtlasRegion leftWall;
//    private TextureAtlas.AtlasRegion hollowWall;
//    private TextureAtlas.AtlasRegion solidWall;
//    private boolean renderCeilingMod1;
//    private boolean renderCeilingMod2;
//    private boolean renderCeilingMod3;
//    private boolean renderCeilingMod4;
//    private boolean renderCeilingMod5;
//    private boolean renderCeilingMod6;
//    private TextureAtlas.AtlasRegion ceiling;
//    private TextureAtlas.AtlasRegion ceilingMod1;
//    private TextureAtlas.AtlasRegion ceilingMod2;
//    private TextureAtlas.AtlasRegion ceilingMod3;
//    private TextureAtlas.AtlasRegion ceilingMod4;
//    private TextureAtlas.AtlasRegion ceilingMod5;
//    private TextureAtlas.AtlasRegion ceilingMod6;
//    private Color overlayColor;
//    private ArrayList<DustEffect> dust;
//    private ArrayList<BottomFogEffect> fog;
//    private ArrayList<InteractableTorchEffect> torches;
//    private static final int DUST_AMT = 24;
//
//    protected final TextureAtlas atlas;
//    protected final TextureAtlas.AtlasRegion bg;
//    protected final TextureAtlas.AtlasRegion campfireBg;
//    protected final TextureAtlas.AtlasRegion campfireGlow;
//    protected final TextureAtlas.AtlasRegion campfireKindling;
//    protected final TextureAtlas.AtlasRegion event;
//
//    public PreloadBottomScene() {
//        super(null);
//
//        this.atlas = AssetLoader.loadAtlas(atlasUrl);
//        this.bg = this.atlas.findRegion("bg");
//        this.campfireBg = this.atlas.findRegion("campfire");
//        this.campfireGlow = this.atlas.findRegion("mod/campfireGlow");
//        this.campfireKindling = this.atlas.findRegion("mod/campfireKindling");
//        this.event = this.atlas.findRegion("event");
//
//        this.overlayColor = Color.WHITE.cpy();
//        this.dust = new ArrayList();
//        this.fog = new ArrayList();
//        this.torches = new ArrayList();
//        this.fg = this.atlas.findRegion("mod/fg");
//        this.mg = this.atlas.findRegion("mod/mg");
//        this.leftWall = this.atlas.findRegion("mod/mod1");
//        this.hollowWall = this.atlas.findRegion("mod/mod2");
//        this.solidWall = this.atlas.findRegion("mod/midWall");
//        this.ceiling = this.atlas.findRegion("mod/ceiling");
//        this.ceilingMod1 = this.atlas.findRegion("mod/ceilingMod1");
//        this.ceilingMod2 = this.atlas.findRegion("mod/ceilingMod2");
//        this.ceilingMod3 = this.atlas.findRegion("mod/ceilingMod3");
//        this.ceilingMod4 = this.atlas.findRegion("mod/ceilingMod4");
//        this.ceilingMod5 = this.atlas.findRegion("mod/ceilingMod5");
//        this.ceilingMod6 = this.atlas.findRegion("mod/ceilingMod6");
//        this.ambianceName = "AMBIANCE_BOTTOM";
//        this.fadeInAmbiance();
//    }
//
//    public void update() {
//        super.update();
//        this.updateDust();
//        this.updateFog();
//        this.updateTorches();
//    }
//
//    private void updateDust() {
//        Iterator e = this.dust.iterator();
//
//        while(e.hasNext()) {
//            DustEffect effect = (DustEffect)e.next();
//            effect.update();
//            if (effect.isDone) {
//                e.remove();
//            }
//        }
//
//        if (this.dust.size() < 96 && !Settings.DISABLE_EFFECTS) {
//            this.dust.add(new DustEffect());
//        }
//
//    }
//
//    private void updateFog() {
//        if (this.fog.size() < 50 && !Settings.DISABLE_EFFECTS) {
//            this.fog.add(new BottomFogEffect(true));
//        }
//
//        Iterator e = this.fog.iterator();
//
//        while(e.hasNext()) {
//            BottomFogEffect effect = (BottomFogEffect)e.next();
//            effect.update();
//            if (effect.isDone) {
//                e.remove();
//            }
//        }
//
//    }
//
//    private void updateTorches() {
//        Iterator e = this.torches.iterator();
//
//        while(e.hasNext()) {
//            InteractableTorchEffect effect = (InteractableTorchEffect)e.next();
//            effect.update();
//            if (effect.isDone) {
//                e.remove();
//            }
//        }
//
//    }
//
//    public void nextRoom(AbstractRoom room) {
//        super.nextRoom(room);
//        this.randomizeScene();
//        if (room instanceof MonsterRoomBoss) {
//            CardCrawlGame.music.silenceBGM();
//        }
//
//        if (room instanceof EventRoom || room instanceof RestRoom) {
//            this.torches.clear();
//        }
//
//        this.fadeInAmbiance();
//    }
//
//    public void randomizeScene() {
//        if (MathUtils.randomBoolean()) {
//            this.renderSolidMid = false;
//            this.renderLeftWall = false;
//            this.renderHollowMid = true;
//            if (MathUtils.randomBoolean()) {
//                this.renderSolidMid = true;
//                if (MathUtils.randomBoolean()) {
//                    this.renderLeftWall = true;
//                }
//            }
//        } else {
//            this.renderLeftWall = false;
//            this.renderHollowMid = false;
//            this.renderSolidMid = true;
//            if (MathUtils.randomBoolean()) {
//                this.renderLeftWall = true;
//            }
//        }
//
//        this.renderCeilingMod1 = MathUtils.randomBoolean();
//        this.renderCeilingMod2 = MathUtils.randomBoolean();
//        this.renderCeilingMod3 = MathUtils.randomBoolean();
//        this.renderCeilingMod4 = MathUtils.randomBoolean();
//        this.renderCeilingMod5 = MathUtils.randomBoolean();
//        this.renderCeilingMod6 = MathUtils.randomBoolean();
//        this.randomizeTorch();
//        this.overlayColor.r = MathUtils.random(0.0F, 0.05F);
//        this.overlayColor.g = MathUtils.random(0.0F, 0.2F);
//        this.overlayColor.b = MathUtils.random(0.0F, 0.2F);
//    }
//
//    public void renderCombatRoomBg(SpriteBatch sb) {
//        sb.setColor(Color.WHITE);
//        this.renderAtlasRegionIf(sb, this.bg, true);
//        Iterator var2;
//        if (!this.isCamp) {
//            var2 = this.fog.iterator();
//
//            while(var2.hasNext()) {
//                BottomFogEffect e = (BottomFogEffect)var2.next();
//                e.render(sb);
//            }
//        }
//
//        sb.setColor(Color.WHITE);
//        this.renderAtlasRegionIf(sb, this.mg, true);
//        if (this.renderHollowMid && (this.renderSolidMid || this.renderLeftWall)) {
//            sb.setColor(Color.GRAY);
//        }
//
//        this.renderAtlasRegionIf(sb, this.solidWall, this.renderSolidMid);
//        sb.setColor(Color.WHITE);
//        this.renderAtlasRegionIf(sb, this.hollowWall, this.renderHollowMid);
//        this.renderAtlasRegionIf(sb, this.leftWall, this.renderLeftWall);
//        this.renderAtlasRegionIf(sb, this.ceiling, true);
//        this.renderAtlasRegionIf(sb, this.ceilingMod1, this.renderCeilingMod1);
//        this.renderAtlasRegionIf(sb, this.ceilingMod2, this.renderCeilingMod2);
//        this.renderAtlasRegionIf(sb, this.ceilingMod3, this.renderCeilingMod3);
//        this.renderAtlasRegionIf(sb, this.ceilingMod4, this.renderCeilingMod4);
//        this.renderAtlasRegionIf(sb, this.ceilingMod5, this.renderCeilingMod5);
//        this.renderAtlasRegionIf(sb, this.ceilingMod6, this.renderCeilingMod6);
//        var2 = this.torches.iterator();
//
//        while(var2.hasNext()) {
//            InteractableTorchEffect e = (InteractableTorchEffect)var2.next();
//            e.render(sb);
//        }
//
//        sb.setBlendFunction(768, 1);
//        sb.setColor(this.overlayColor);
//        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
//        sb.setBlendFunction(770, 771);
//    }
//
//    private void randomizeTorch() {
//        this.torches.clear();
//        if (MathUtils.randomBoolean(0.1F)) {
//            this.torches.add(new InteractableTorchEffect(1790.0F * Settings.scale, 850.0F * Settings.scale, InteractableTorchEffect.TorchSize.S));
//        }
//
//        int roll;
//        if (this.renderHollowMid && !this.renderSolidMid) {
//            roll = MathUtils.random(2);
//            if (roll == 0) {
//                this.torches.add(new InteractableTorchEffect(800.0F * Settings.scale, 768.0F * Settings.scale));
//                this.torches.add(new InteractableTorchEffect(1206.0F * Settings.scale, 768.0F * Settings.scale));
//            } else if (roll == 1) {
//                this.torches.add(new InteractableTorchEffect(328.0F * Settings.scale, 865.0F * Settings.scale, InteractableTorchEffect.TorchSize.S));
//            }
//        } else if (!this.renderLeftWall && !this.renderHollowMid) {
//            if (MathUtils.randomBoolean(0.75F)) {
//                this.torches.add(new InteractableTorchEffect(613.0F * Settings.scale, 860.0F * Settings.scale));
//                this.torches.add(new InteractableTorchEffect(613.0F * Settings.scale, 672.0F * Settings.scale));
//                if (MathUtils.randomBoolean(0.3F)) {
//                    this.torches.add(new InteractableTorchEffect(1482.0F * Settings.scale, 860.0F * Settings.scale));
//                    this.torches.add(new InteractableTorchEffect(1482.0F * Settings.scale, 672.0F * Settings.scale));
//                }
//            }
//        } else if (this.renderSolidMid && this.renderHollowMid) {
//            if (!this.renderLeftWall) {
//                roll = MathUtils.random(3);
//                if (roll == 0) {
//                    this.torches.add(new InteractableTorchEffect(912.0F * Settings.scale, 790.0F * Settings.scale));
//                    this.torches.add(new InteractableTorchEffect(912.0F * Settings.scale, 526.0F * Settings.scale));
//                    this.torches.add(new InteractableTorchEffect(844.0F * Settings.scale, 658.0F * Settings.scale, InteractableTorchEffect.TorchSize.S));
//                    this.torches.add(new InteractableTorchEffect(980.0F * Settings.scale, 658.0F * Settings.scale, InteractableTorchEffect.TorchSize.S));
//                } else if (roll == 1 || roll == 2) {
//                    this.torches.add(new InteractableTorchEffect(1828.0F * Settings.scale, 720.0F * Settings.scale));
//                }
//            } else if (MathUtils.randomBoolean(0.75F)) {
//                this.torches.add(new InteractableTorchEffect(970.0F * Settings.scale, 874.0F * Settings.scale, InteractableTorchEffect.TorchSize.L));
//            }
//        } else if (this.renderLeftWall && !this.renderHollowMid && MathUtils.randomBoolean(0.75F)) {
//            this.torches.add(new InteractableTorchEffect(970.0F * Settings.scale, 873.0F * Settings.scale, InteractableTorchEffect.TorchSize.L));
//            this.torches.add(new InteractableTorchEffect(616.0F * Settings.scale, 813.0F * Settings.scale));
//            this.torches.add(new InteractableTorchEffect(1266.0F * Settings.scale, 708.0F * Settings.scale));
//        }
//
//        LightFlareMEffect.renderGreen = MathUtils.randomBoolean();
//        TorchParticleMEffect.renderGreen = LightFlareMEffect.renderGreen;
//    }
//
//    public void renderCombatRoomFg(SpriteBatch sb) {
//        if (!this.isCamp) {
//            Iterator var2 = this.dust.iterator();
//
//            while(var2.hasNext()) {
//                DustEffect e = (DustEffect)var2.next();
//                e.render(sb);
//            }
//        }
//
//        sb.setColor(Color.WHITE);
//        this.renderAtlasRegionIf(sb, this.fg, true);
//    }
//
//    public void renderCampfireRoom(SpriteBatch sb) {}
//
//    @Override
//    public void renderEventRoom(SpriteBatch sb) {
//        sb.setColor(Color.WHITE);
//        sb.draw(this.event.getTexture(), this.event.offsetX * Settings.scale, this.event.offsetY * Settings.scale, 0.0F, 0.0F, (float)this.event.packedWidth, (float)this.event.packedHeight, Settings.scale, Settings.scale, 0.0F, this.event.getRegionX(), this.event.getRegionY(), this.event.getRegionWidth(), this.event.getRegionHeight(), false, false);
//    }
//
//    @Override
//    public void dispose() {
//        AssetLoader.unLoad(atlasUrl);
//    }
//}