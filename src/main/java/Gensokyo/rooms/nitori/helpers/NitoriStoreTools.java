package Gensokyo.rooms.nitori.helpers;

import Gensokyo.cards.Evolve.BlemishedSteel;
import Gensokyo.cards.Evolve.DepletedGenerator;
import Gensokyo.cards.Evolve.ExoticEgg;
import Gensokyo.cards.Evolve.LockedMedkit;
import Gensokyo.cards.Evolve.MysteriousEgg;
import Gensokyo.cards.Evolve.RustyChest;
import Gensokyo.cards.Evolve.ScrapIron;
import Gensokyo.cards.Evolve.Shovel;
import Gensokyo.cards.Evolve.TarnishedGold;
import Gensokyo.cards.Evolve.TrainingManual;
import Gensokyo.cards.UrbanLegend.AbstractUrbanLegendCard;
import Gensokyo.rooms.nitori.NitoriStoreScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.*;
import static Gensokyo.patches.NitoriShopPatches.NITORI_STORE;
import static Gensokyo.rooms.nitori.helpers.gensokyoCardHelper.getNitoriShopCards;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnRandomRelic;

public class NitoriStoreTools {

    protected static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    protected static int tmp = (int) (Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
    protected static float padX = (int) (tmp + AbstractCard.IMG_WIDTH_S);

    protected static final float TOP_TOP_ROW_Y = Settings.isFourByThree ? (971.5F * Settings.scale) : (921.5F * Settings.scale);
    protected static final float TOP_ROW_Y = Settings.isFourByThree ? (760.0F * Settings.scale) : (710.0F * Settings.scale);
    protected static final float TOP_MIDDLE_ROW_Y = Settings.isFourByThree ? (548.5F * Settings.scale) : (498.5F * Settings.scale);
    protected static final float MIDDLE_ROW_Y = Settings.isFourByThree ? (337.0F * Settings.scale) : (287.0F * Settings.scale);
    protected static final float BOTTOM_MIDDLE_ROW_Y = Settings.isFourByThree ? (125.5F * Settings.scale) : (75.5F * Settings.scale);
    protected static final float BOTTOM_ROW_Y = Settings.isFourByThree ? (-86F * Settings.scale) : (-136F * Settings.scale);
    protected static final float BOTTOMEST_ROW_Y = Settings.isFourByThree ? (-297.5F * Settings.scale) : (347.5F * Settings.scale);

    // Relic
    protected static final float RELIC_ROW1 = Settings.isFourByThree ? (760.0F * Settings.scale) : (710.0F * Settings.scale);
    protected static final float RELIC_ROW2 = Settings.isFourByThree ? (654.25F * Settings.scale) : (604.25f * Settings.scale);
    protected static final float RELIC_ROW3 = Settings.isFourByThree ? (548.5F * Settings.scale) : (498.5F * Settings.scale);
    protected static final float RELIC_ROW4 = Settings.isFourByThree ? (442.75F * Settings.scale) : (392.75F * Settings.scale);
    protected static final float RELIC_ROW5 = Settings.isFourByThree ? (337.0F * Settings.scale) : (287.0F * Settings.scale);

    public static class SpinningCardItems {
        protected ArrayList<CardItem> cardItems = new ArrayList<>();

        public SpinningCardItems() {

            ArrayList<AbstractCard> cards = getNitoriShopCards();

            int padding = 0;
            int row = 0;
            for(int i = 0; i <= cards.size() - 1; i++){
                if(i <= 4){
                    cardItems.add(new CardItem(cards.get(i), padding, row));
                    padding++;
                    if(i == 4){
                        padding = 0;
                        row++;
                    }
                }
                else if(i <= 9){
                    cardItems.add(new CardItem(cards.get(i), padding, row));
                    padding++;
                    if(i == 9){
                        padding = 0;
                        row++;
                    }
                }
            }
        }

        public void render(SpriteBatch sb) {
            for (CardItem cardItem : cardItems) {
                cardItem.render(sb);
            }
        }

        public void update() {
            for (CardItem cardItem : cardItems) {
                cardItem.update();
                if (cardItem.isHovered()) { cardItem.card.drawScale = MathUtils.lerp(0.4f, .85f, cardItem.card.drawScale);
                } else { cardItem.card.drawScale = MathUtils.lerp(0.85f, 0.4f, cardItem.card.drawScale); }
            }

            cardItems.removeIf((cardItem) -> {
                if (cardItem.isHovered() && InputHelper.justClickedLeft) {
                    if (cardItem.canBuy()) {
                        CardCrawlGame.sound.play("SHOP_PURCHASE");
                        AbstractDungeon.player.gold -= cardItem.price;
                        AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(cardItem.card, cardItem.card.current_x, cardItem.card.current_y));
                        return true;
                    }
                    CardCrawlGame.sound.play("UI_CLICK_2");
                }
                return false;
            });
        }
    }

    public static class CardItem {

        protected Color lockAlpha = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        protected int price = -1;
        protected AbstractCard card;
        protected Hitbox hb;
        protected int row;

        public CardItem(AbstractCard card, int padding, int row) {
            this.card = card;
            this.row = row;
            setPrice(card);
            price = MathUtils.round(price * AbstractDungeon.merchantRng.random(0.90F, 1.10F));
            //if(AbstractDungeon.player.hasRelic(NitoriTicket.ID)){ price /= 2; }
            card.targetDrawScale = 0.75F;
            card.current_x = DRAW_START_X + card.IMG_WIDTH_S / 2F + padX * padding;
            card.target_x = card.current_x;
            card.current_y = this.row == 0 ? NitoriStoreScreen.getPullY() + TOP_ROW_Y : (this.row == 1 ? NitoriStoreScreen.getPullY() + MIDDLE_ROW_Y : NitoriStoreScreen.getPullY() + BOTTOM_ROW_Y);
            card.target_y = card.current_y;
            hb = new Hitbox(card.current_x, card.current_y, card.hb.width, card.hb.height);
        }
        public void setPrice(AbstractCard card) {
            if (card instanceof AbstractUrbanLegendCard) {
                price = 200;
            } else if (card.cardID.equals(Shovel.ID)) {
                price = 50;
            } else if (card.cardID.equals(BlemishedSteel.ID)) {
                price = 80;
            } else if (card.cardID.equals(DepletedGenerator.ID)) {
                price = 45;
            } else if (card.cardID.equals(ExoticEgg.ID)) {
                price = 100;
            } else if (card.cardID.equals(LockedMedkit.ID)) {
                price = 70;
            } else if (card.cardID.equals(MysteriousEgg.ID)) {
                price = 60;
            } else if (card.cardID.equals(RustyChest.ID)) {
                price = 20;
            } else if (card.cardID.equals(ScrapIron.ID)) {
                price = 40;
            } else if (card.cardID.equals(TarnishedGold.ID)) {
                price = 30;
            } else if (card.cardID.equals(TrainingManual.ID)) {
                price = 35;
            } else {
                price = 99;
            }
        }
        public void render(SpriteBatch sb) {
            sb.setColor(WHITE.cpy());
            card.current_y = row == 0 ? NitoriStoreScreen.getPullY() + TOP_ROW_Y : (row == 1 ? NitoriStoreScreen.getPullY() + MIDDLE_ROW_Y : NitoriStoreScreen.getPullY() + BOTTOM_ROW_Y);
            card.target_y = card.current_y;
            hb.move(card.current_x, card.current_y);
            card.render(sb);
            renderPrice(sb);
            this.hb.render(sb);
        }
        public void renderPrice(SpriteBatch sb) {
            float scale = Settings.scale;
            float goldCX = ImageMaster.UI_GOLD.getWidth() / 2f;
            float goldCY = ImageMaster.UI_GOLD.getHeight() / 2f;
            float width = ImageMaster.UI_GOLD.getWidth();
            float height = ImageMaster.UI_GOLD.getHeight();
            Color fontColor = WHITE.cpy();

            sb.setColor(lockAlpha);
            sb.draw(ImageMaster.UI_GOLD,
                    card.current_x - goldCX,
                    card.current_y - goldCY,
                    width/2,
                    height/2,
                    width,
                    height,
                    scale,
                    scale,
                    1.0f,
                    0,
                    0,
                    ImageMaster.UI_GOLD.getWidth(),
                    ImageMaster.UI_GOLD.getHeight(),
                    false,
                    false);

            if(!this.canBuy()) { fontColor = Color.RED.cpy(); }

            fontColor.a = lockAlpha.a;

            FontHelper.cardTitleFont.getData().setScale(1.0f);
            FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + this.price, card.current_x, card.current_y - (16f * Settings.scale), fontColor);

        }
        public void update() {
            card.update();
            this.hb.update();
            if(hb.hovered) {
                lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 0.2f);
                if(InputHelper.justClickedRight) { CardCrawlGame.cardPopup.open(this.card); }
            }else {
                lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 1.0f);
            }
        }

        public AbstractCard getCard() { return card.makeStatEquivalentCopy(); }

        public boolean isHovered() { return hb.hovered; }

        public boolean canBuy() { return AbstractDungeon.player.gold >= this.price; }
    }

    public static ArrayList<AbstractRelic> getRandomRelics(AbstractRelic.RelicTier rarity, int size) {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        for(int i = 1; i <= size; i++){ relics.add(returnRandomRelic(rarity)); }
        return relics;
    }

    public static class SpinningRelicItems {
        protected ArrayList<RelicItem> relicItems = new ArrayList<>();

        public SpinningRelicItems() {

            ArrayList<AbstractRelic> relics = new ArrayList<>();
            ArrayList<AbstractRelic> cosmoRelics;
            ArrayList<AbstractRelic> commonRelics;
            ArrayList<AbstractRelic> uncommonRelics;
            ArrayList<AbstractRelic> shopRelics;

            commonRelics = getRandomRelics(AbstractRelic.RelicTier.COMMON, 5);
            uncommonRelics = getRandomRelics(AbstractRelic.RelicTier.UNCOMMON, 5);
            cosmoRelics = getRandomRelics(AbstractRelic.RelicTier.RARE, 5);
            shopRelics = getRandomRelics(AbstractRelic.RelicTier.SHOP, 5);

            relics.addAll(commonRelics);
            relics.addAll(uncommonRelics);
            relics.addAll(cosmoRelics);
            relics.addAll(shopRelics);

            int padding = 0;
            int row = 0;
            for(AbstractRelic relic : relics){
                relicItems.add(new RelicItem(relic, padding++, row));
                if(padding % 5 == 0){ padding = 0; row++; }
            }

        }

        public void render(SpriteBatch sb) {
            for (RelicItem relicItem : relicItems) { relicItem.render(sb); }
        }

        public void update() {
            for(RelicItem relicItem : relicItems) { relicItem.update(); }
            relicItems.removeIf((relicItem) -> {
                if (relicItem.isHovered() && InputHelper.justClickedLeft) {
                    if (relicItem.canBuy()) {
                        CardCrawlGame.sound.play("SHOP_PURCHASE");
                        AbstractDungeon.getCurrRoom().relics.add(relicItem.relic);
                        relicItem.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
                        relicItem.relic.flash();
                        AbstractDungeon.player.loseGold(relicItem.price);
                        return true;
                    }
                    CardCrawlGame.sound.play("UI_CLICK_2");
                }
                return false;
            });
        }
    }
    public static class RelicItem {
        protected int price;
        protected AbstractRelic relic;
        protected Hitbox hb;
        protected int row;
        protected boolean shopBypass = false;

        public RelicItem(AbstractRelic relic, int padding, int row) {
            this.relic = relic;
            this.row = row;
            switch (this.relic.tier){
                case SHOP:
                case COMMON:
                    price = 150;
                    break;
                case UNCOMMON:
                    price = 250;
                    break;
                case RARE:
                    price = 300;
                case SPECIAL:
                case BOSS:
                    price = 325;
                    break;
            }
            price = MathUtils.round(price * AbstractDungeon.merchantRng.random(0.95F, 1.05F));
            //if(AbstractDungeon.player.hasRelic(NitoriTicket.ID)){ price /= 2; }
            relic.currentX = DRAW_START_X + relic.img.getWidth() / 2F + padX * padding;
            relic.targetX = relic.currentX;
            relic.currentY = row == 0 ? NitoriStoreScreen.getPullY() + TOP_TOP_ROW_Y : (row == 1 ? NitoriStoreScreen.getPullY() + TOP_MIDDLE_ROW_Y : NitoriStoreScreen.getPullY() + BOTTOM_ROW_Y);
            relic.targetY = relic.currentY;
            hb = new Hitbox(relic.currentX, relic.currentY, relic.hb.width, relic.hb.height);
        }

        public void render(SpriteBatch sb) {
            sb.setColor(WHITE.cpy());
            switch (row){
                case 0:
                    relic.currentY = NitoriStoreScreen.getPullY() + RELIC_ROW1;
                    break;
                case 1:
                    relic.currentY = NitoriStoreScreen.getPullY() + RELIC_ROW2;
                    break;
                case 2:
                    relic.currentY = NitoriStoreScreen.getPullY() + RELIC_ROW3;
                    break;
                case 3:
                    relic.currentY = NitoriStoreScreen.getPullY() + RELIC_ROW4;
                    break;
                default:
                    relic.currentY = NitoriStoreScreen.getPullY() + RELIC_ROW5;
                    break;

            }
            relic.targetY = relic.currentY;
            relic.renderOutline(sb, false);
            relic.renderWithoutAmount(sb, new Color(0.0f, 0.0f, 0.0f, 0.25f));
            renderPrice(sb);

            if(this.isHovered()) {
                relic.renderTip(sb);
            }

            hb.move(relic.currentX, relic.currentY);
            this.hb.render(sb);
        }

        private void renderPrice(SpriteBatch sb) {

            float width = ImageMaster.UI_GOLD.getWidth();
            float height = ImageMaster.UI_GOLD.getHeight();
            float xPos = this.relic.currentX - (width / 2f);
            float yPos = this.relic.currentY - height - (10f * Settings.scale);

            TextureRegion region = new TextureRegion(ImageMaster.UI_GOLD);

            sb.draw(region,
                    xPos,
                    yPos,
                    width / 2f,
                    height / 2f,
                    width,
                    height,
                    Settings.scale, Settings.scale,
                    1.0f);

            Color fontColor = WHITE.cpy();
            if(!this.canBuy()) { fontColor = Color.RED.cpy(); }
            FontHelper.cardTitleFont.getData().setScale(1.0f);
            FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + this.price,
                    relic.currentX,
                    yPos + (16f * Settings.scale * Settings.scale), fontColor);
        }

        public void update() {
            this.hb.update();
            //hover logic
        }

        public boolean isHovered() {
            return this.hb.hovered;
        }

        public boolean canBuy() {
            return AbstractDungeon.player.gold >= this.price;
        }

    }

    public static class CosmoBanners {
        protected ArrayList<CosmoBanner> banners = new ArrayList<>();
        protected static float yScale = 0f;

        public CosmoBanners() {
            for(int i = 0; i <= 2; i++){ banners.add(new CosmoBanner(i)); }
        }

        public void render(SpriteBatch sb) {
            sb.setColor(WHITE.cpy());
            yScale = MathHelper.scaleLerpSnap(yScale, 1.0f);
            for (CosmoBanner c : banners) {
                if(c.type != 0) { c.render(sb, c.type); }
            }
        }

        public void update() {
            for(CosmoBanner c : banners) {
                if(c.type != 0) { c.update(); }
            }
            for(CosmoBanner c : banners){
                if(c.isHovered() && InputHelper.justClickedLeft){
                    NitoriStoreScreen.setRenderSwitch(c.type);
                    NitoriStoreScreen.setPullY(Settings.HEIGHT);
                }
            }
        }

        public static float returnYscale(){ return yScale; }

        public ArrayList<CosmoBanner> getBanners() {
            return banners;
        }
    }
    public static class CosmoBanner {

        private Texture currentBanner;
        private int type;
        private Hitbox hb;
        private float PANEL_POSITION;
        protected BobEffect bobEffect;

        public CosmoBanner(int type) {
            switch (type){
                case 0:
                    currentBanner = new Texture(makeUIPath("ErrorPanel.png"));
                    PANEL_POSITION = TOP_ROW_Y;
                    break;
                case 1:
                    currentBanner = new Texture(makeUIPath("CardsPanel.png"));
                    PANEL_POSITION = TOP_MIDDLE_ROW_Y;
                    break;
                case 2:
                    currentBanner = new Texture(makeUIPath("RelicPanel.png"));
                    PANEL_POSITION = MIDDLE_ROW_Y;
                    break;
                default:
                    currentBanner = new Texture(makeUIPath("ErrorPanel.png"));
                    PANEL_POSITION = BOTTOMEST_ROW_Y;
                    break;
            }
            this.type = type;
            hb = new Hitbox(DRAW_START_X + currentBanner.getWidth() / 2F, NitoriStoreScreen.getPullY() + TOP_ROW_Y, currentBanner.getWidth(), currentBanner.getHeight());

            hb.x = DRAW_START_X + currentBanner.getWidth() / 2F;
            hb.cX = hb.x;

            hb.y = NitoriStoreScreen.getPullY() + PANEL_POSITION;
            hb.cY = hb.y;

            hb.move(hb.x, hb.y);
            this.bobEffect = new BobEffect(5.0F * Settings.scale, 3.0F);
        }

        public void render(SpriteBatch sb, int index) {
            float xOffset = 1;

            float width = 500f * Settings.scale;
            float xPos = ((Settings.WIDTH / 4f) * xOffset) - (width / 1.5f);
            float yPos = Settings.HEIGHT - (400f * Settings.scale) + NitoriStoreScreen.getRugY();

            yPos -= (100 * Settings.scale) * index * CosmoBanners.returnYscale();

            hb.update(xPos, yPos + (15f * Settings.scale));

            sb.setColor(WHITE.cpy());
            if(isHovered()){ sb.draw(currentBanner, xPos, yPos + this.bobEffect.y, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false); }
            else { sb.draw(currentBanner, xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false); }
            if(hb.justHovered) { CardCrawlGame.sound.play("UI_HOVER"); }
            hb.render(sb);
        }

        public void update() {
            this.hb.update();
            this.bobEffect.update();
        }

        public boolean isHovered() {
            return this.hb.hovered;
        }

    }
}