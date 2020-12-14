package Gensokyo.scenes;

import Gensokyo.dungeon.Gensokyo;
import Gensokyo.dungeon.Gensokyoer;
import Gensokyo.monsters.act1.Aya;
import Gensokyo.monsters.act1.Cirno;
import Gensokyo.monsters.act1.Kokoro;
import Gensokyo.monsters.act1.Mamizou;
import Gensokyo.monsters.act1.Reimu;
import Gensokyo.monsters.act1.Sumireko;
import Gensokyo.monsters.act1.Yukari;
import Gensokyo.monsters.act2.Byakuren;
import Gensokyo.monsters.act2.Eiki;
import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.monsters.act2.Koishi;
import Gensokyo.monsters.act2.Miko;
import Gensokyo.monsters.act2.NormalEnemies.AngelMirror;
import Gensokyo.monsters.act2.NormalEnemies.BigMudSlime;
import Gensokyo.monsters.act2.NormalEnemies.Chomper;
import Gensokyo.monsters.act2.NormalEnemies.CosmicMonolith;
import Gensokyo.monsters.act2.NormalEnemies.Gloop;
import Gensokyo.monsters.act2.NormalEnemies.SlimeBunny;
import Gensokyo.monsters.act2.NormalEnemies.Swordslinger;
import Gensokyo.monsters.act2.NormalEnemies.TanukiDog;
import Gensokyo.monsters.act2.NormalEnemies.Wraith;
import Gensokyo.monsters.act2.Reisen;
import Gensokyo.monsters.act2.Tenshi;
import Gensokyo.monsters.act3.Marisa;
import Gensokyo.monsters.act3.Mokou;
import Gensokyo.monsters.act3.NormalEnemies.AncientGuardian;
import Gensokyo.monsters.act3.NormalEnemies.AtlasGolem;
import Gensokyo.monsters.act3.NormalEnemies.Dawnsword;
import Gensokyo.monsters.act3.NormalEnemies.Duskaxe;
import Gensokyo.monsters.act3.NormalEnemies.FeralBat;
import Gensokyo.monsters.act3.NormalEnemies.LoudBat;
import Gensokyo.monsters.act3.NormalEnemies.Rafflesia;
import Gensokyo.monsters.act3.NormalEnemies.SeedOfUnknown;
import Gensokyo.monsters.act3.NormalEnemies.Sharpion;
import Gensokyo.monsters.act3.NormalEnemies.VampireBat;
import Gensokyo.monsters.act3.Shinki.Shinki;
import Gensokyo.monsters.act3.Yuyuko;
import Gensokyo.monsters.act1.marisaMonsters.Patchouli;
import Gensokyo.monsters.act3.Doremy;
import Gensokyo.monsters.act3.Flandre;
import Gensokyo.monsters.act3.Remilia;
import Gensokyo.rooms.nitori.Nitori;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;

public class GensokyoScene extends AbstractScene {

    private static Texture topBar;
    private TextureAtlas.AtlasRegion bg;
    private TextureAtlas.AtlasRegion fg;
    private TextureAtlas.AtlasRegion ceil;
    private TextureAtlas.AtlasRegion fgGlow;
    private TextureAtlas.AtlasRegion floor;
    private TextureAtlas.AtlasRegion mg1;
    private Texture campfirebg;
    private Texture campfire;
    private Texture fire;

    public GensokyoScene() {
        super("GensokyoResources/images/scene/atlas.atlas");
        //topBar = ImageMaster.loadImage("GensokyoResources/images/scene/topbar.png");

        this.bg = this.atlas.findRegion("mod/TanukiForest");
        //this.fg = this.atlas.findRegion("mod/fg");
        //this.ceil = this.atlas.findRegion("mod/ceiling");
        //this.fgGlow = this.atlas.findRegion("mod/fgGlow");
        //this.floor = this.atlas.findRegion("mod/floor");
        //this.mg1 = this.atlas.findRegion("mod/mg1");

        this.ambianceName = "AMBIANCE_CITY";
        this.fadeInAmbiance();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void randomizeScene() {
    }

    @Override
    public void nextRoom(AbstractRoom room) {
        super.nextRoom(room);
        this.randomizeScene();
        if (room instanceof MonsterRoomBoss) {
            CardCrawlGame.music.silenceBGM();
        }
        if (room.monsters != null) {
            for (AbstractMonster mo : room.monsters.monsters) {
                if (mo instanceof Nitori) { this.bg = this.atlas.findRegion("mod/GenbuRavineCloudy"); }
                else if (mo instanceof Mamizou) {
                    this.bg = this.atlas.findRegion("mod/TanukiForestNight");
                } else if (mo instanceof Aya) {
                    this.bg = this.atlas.findRegion("mod/FancyPlace");
                } else if (mo instanceof Cirno) {
                    this.bg = this.atlas.findRegion("mod/GenbuRavineCloudy");
                } else if (mo instanceof Yukari) {
                    this.bg = this.atlas.findRegion("mod/ConcertStage");
                } else if (mo instanceof Kokoro) {
                    this.bg = this.atlas.findRegion("mod/HumanVillage");
                } else if (mo instanceof Reimu) {
                    this.bg = this.atlas.findRegion("mod/HakureiShrine");
                } else if (mo instanceof Patchouli) {
                    this.bg = this.atlas.findRegion("mod/Palace");
                } else if (mo instanceof Sumireko) {
                    this.bg = this.atlas.findRegion("mod/OutsideWorld");
                } else if (mo instanceof Eiki) {
                    this.bg = this.atlas.findRegion("mod/ConcertStage");
                } else if (mo instanceof Kaguya) {
                    this.bg = this.atlas.findRegion("mod/Eientei");
                } else if (mo instanceof Byakuren || mo instanceof Miko) {
                    this.bg = this.atlas.findRegion("mod/FancyPlaceNight");
                } else if (mo instanceof Reisen) {
                    this.bg = this.atlas.findRegion("mod/ElegantPlace");
                } else if (mo instanceof Koishi) {
                    this.bg = this.atlas.findRegion("mod/PalaceBright");
                } else if (mo instanceof Yuyuko) {
                    this.bg = this.atlas.findRegion("mod/ElegantPlace");
                } else if (mo instanceof Tenshi) {
                    this.bg = this.atlas.findRegion("mod/GenbuRavineCloudy");
                } else if (mo instanceof AngelMirror) {
                    this.bg = this.atlas.findRegion("mod/Ruins");
                    break; //override the Tanuki's Dog bg when they are grouped together
                } else if (mo instanceof Chomper) {
                    this.bg = this.atlas.findRegion("mod/Forest");
                } else if (mo instanceof CosmicMonolith) {
                    this.bg = this.atlas.findRegion("mod/Cave");
                } else if (mo instanceof TanukiDog) {
                    this.bg = this.atlas.findRegion("mod/Forest");
                } else if (mo instanceof Gloop) {
                    this.bg = this.atlas.findRegion("mod/Island");
                } else if (mo instanceof Wraith) {
                    this.bg = this.atlas.findRegion("mod/Cave");
                } else if (mo instanceof Swordslinger) {
                    this.bg = this.atlas.findRegion("mod/Desert");
                } else if (mo instanceof BigMudSlime || mo instanceof SlimeBunny) {
                    this.bg = this.atlas.findRegion("mod/Desert");
                } else if (mo instanceof Doremy) {
                    this.bg = this.atlas.findRegion("mod/DreamWorld");
                } else if (mo instanceof Flandre || mo instanceof Remilia) {
                    this.bg = this.atlas.findRegion("mod/Palace");
                } else if (mo instanceof Mokou) {
                    this.bg = this.atlas.findRegion("mod/BambooForest");
                } else if (mo instanceof Marisa) {
                    this.bg = this.atlas.findRegion("mod/HakureiShrine");
                } else if (mo instanceof Shinki) {
                    this.bg = this.atlas.findRegion("mod/FancyPlaceNight");
                } else if (mo instanceof SeedOfUnknown) {
                    this.bg = this.atlas.findRegion("mod/Cave");
                } else if (mo instanceof AncientGuardian) {
                    this.bg = this.atlas.findRegion("mod/Desert");
                } else if (mo instanceof Rafflesia) {
                    this.bg = this.atlas.findRegion("mod/Forest");
                } else if (mo instanceof AtlasGolem) {
                    this.bg = this.atlas.findRegion("mod/Cave");
                } else if (mo instanceof Duskaxe || mo instanceof Dawnsword) {
                    this.bg = this.atlas.findRegion("mod/Ruins");
                } else if (mo instanceof VampireBat || mo instanceof FeralBat || mo instanceof LoudBat) {
                    this.bg = this.atlas.findRegion("mod/Forest");
                } else if (mo instanceof Sharpion) {
                    this.bg = this.atlas.findRegion("mod/Desert");
                } else {
                    if (CardCrawlGame.dungeon instanceof Gensokyoer) {
                        this.bg = this.atlas.findRegion("mod/TanukiForestNight");
                    } else {
                        this.bg = this.atlas.findRegion("mod/TanukiForest");
                    }
                }
            }
        } else if (room instanceof ShopRoom) {
            if (CardCrawlGame.dungeon instanceof Gensokyoer) {
                this.bg = this.atlas.findRegion("mod/HumanVillage");
            } else {
                this.bg = this.atlas.findRegion("mod/Shop");
            }
        } else {
            if (CardCrawlGame.dungeon instanceof Gensokyoer) {
                this.bg = this.atlas.findRegion("mod/TanukiForestNight");
            } else {
                this.bg = this.atlas.findRegion("mod/TanukiForest");
            }
        }
        this.fadeInAmbiance();
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        this.renderAtlasRegionIf(sb, bg, true);
        sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        //this.renderAtlasRegionIf(sb, this.floor, true);
        // this.renderAtlasRegionIf(sb, this.ceil, true);
        //this.renderAtlasRegionIf(sb, this.mg1, true);
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.setColor(Color.WHITE.cpy());
        // this.renderAtlasRegionIf(sb, this.fg, true);
        // sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
        // this.renderAtlasRegionIf(sb, this.fgGlow, true);
        // sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireBg, true);
        sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, MathUtils.cosDeg(System.currentTimeMillis() / 3L % 360L) / 10.0f + 0.8f));
        this.renderQuadrupleSize(sb, this.campfireGlow, !CampfireUI.hidden);
        sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireKindling, true);
    }
}