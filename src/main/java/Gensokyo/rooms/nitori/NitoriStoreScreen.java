package Gensokyo.rooms.nitori;

import Gensokyo.rooms.nitori.helpers.NitoriStoreTools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.BobEffect;

import static Gensokyo.GensokyoMod.makeUIPath;
import static Gensokyo.patches.NitoriShopPatches.NITORI_STORE;

public class NitoriStoreScreen {

    protected static NitoriStoreTools.SpinningCardItems cards;
    protected static NitoriStoreTools.SpinningRelicItems relics;
    protected static NitoriStoreTools.CosmoBanners banners;
    protected static NitoriStoreTools.purgeRelicsPage purgeRelics;
    protected static NitoriStoreTools.randomRelicsPage randomRelics;

    protected static float yScale;
    protected static int currentRenderSwitch;
    protected static float rugY = Settings.HEIGHT / 2.0F + 540.0F * Settings.scale;
    protected static float pullY = Settings.HEIGHT / 2.0F + 540.0F * Settings.scale;
    protected static final String woodImagePath = makeUIPath("wood.png");
    protected static final String seaImagePath = makeUIPath("sea.png");

    protected static Texture woodImg;
    protected static Texture seaImg;
    protected static BobEffect weakBobEffect = new BobEffect(2.5F * Settings.scale, 3.0F);
    protected static BobEffect strongBobEffect = new BobEffect(5F * Settings.scale, 3.0F);

    public static void init() {
        currentRenderSwitch = -1;
        woodImg = new Texture(woodImagePath);
        seaImg = new Texture(seaImagePath);
        cards = new NitoriStoreTools.SpinningCardItems();
        relics = new NitoriStoreTools.SpinningRelicItems();
        purgeRelics = new NitoriStoreTools.purgeRelicsPage();
        randomRelics = new NitoriStoreTools.randomRelicsPage();
        banners = new NitoriStoreTools.CosmoBanners();
    }

    public static void open() {
        rugY = Settings.HEIGHT;
        pullY = Settings.HEIGHT;
        //if (questLogStrings == null) questLogStrings = CardCrawlGame.languagePack.getUIString("QuestLog");
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.screen = NITORI_STORE;
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show("Return");
        AbstractDungeon.isScreenUp = true;

        if (MathUtils.randomBoolean()) { CardCrawlGame.sound.play("MAP_OPEN", 0.1f);
        } else { CardCrawlGame.sound.play("MAP_OPEN_2", 0.1f); }

        yScale = 0.0f;
    }

    public static void close() {
        AbstractDungeon.overlayMenu.cancelButton.hide();
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.play("MAP_OPEN", 0.1f);
        } else {
            CardCrawlGame.sound.play("MAP_OPEN_2", 0.1f);
        }
    }

    public static void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        if(rugY == 0){
            sb.draw(seaImg, 0.0F, rugY + strongBobEffect.y, Settings.WIDTH, Settings.HEIGHT);
            sb.draw(woodImg, 0.0F, rugY + weakBobEffect.y, Settings.WIDTH, Settings.HEIGHT);
        }
        else {
            sb.draw(seaImg, 0.0F, rugY, Settings.WIDTH, Settings.HEIGHT);
            sb.draw(woodImg, 0.0F, rugY, Settings.WIDTH, Settings.HEIGHT);
        }


        banners.render(sb);
        switch (currentRenderSwitch){
            case 1:
                cards.render(sb);
                break;
            case 2:
                relics.render(sb);
                break;
            case 3:
                randomRelics.render(sb);
                break;
            case 4:
                purgeRelics.render(sb);
                break;
            default:
                break;
        }
        yScale = MathHelper.scaleLerpSnap(yScale, 1.0f);
    }

    public static void update() {
        purgeRelics.update();
        switch (currentRenderSwitch){
            case 1:
                cards.update();
                break;
            case 2:
                relics.update();
                break;
            case 3:
                randomRelics.update();
                break;
            case 4:
                purgeRelics.update();
                break;
            default:
                break;
        }
        banners.update();
        weakBobEffect.update();
        strongBobEffect.update();
        updateRug();
    }

    protected static void updateRug() {
        if (rugY != 0.0F || pullY != 0.0F) {
            if(pullY != 0.0F){
                pullY = MathUtils.lerp(pullY, Settings.HEIGHT / 2.0F - 540.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                if (Math.abs(pullY - 0.0F) < 0.5F) {
                    pullY = 0.0F;
                }
            }
            if(rugY != 0.0F){
                //rugY = MathHelper.scaleLerpSnap(rugY, 0F);
                rugY = MathUtils.lerp(rugY, Settings.HEIGHT / 2.0F - 540.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                if (Math.abs(rugY - 0.0F) < 0.5F) { rugY = 0.0F; }
            }
        }
    }

    public static float getRugY(){ return rugY; }
    public static float getPullY(){ return  pullY; }
    public static void setPullY(float amount){ pullY = amount; }
    public static float getRenderSwitch(){ return  currentRenderSwitch; }
    public static void setRenderSwitch(int amount){ currentRenderSwitch = amount; }
}