package Gensokyo.rooms.nitori;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class NitoriTicket extends CustomRelic {

    public static final String ID = GensokyoMod.makeID(NitoriTicket.class.getSimpleName());
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NitoriTicket.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NitoriTicket.png"));
    private static final int GOLD_PICKUP_AMOUNT = 300;
    private static final int GOLD_FLOOR_AMOUNT = 12;
    private static final int GOLD_CARD_AMOUNT = 9;
    private static final int SHOP_REDUCTION = 50;
    public NitoriTicket() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() { return String.format(DESCRIPTIONS[0], SHOP_REDUCTION, GOLD_PICKUP_AMOUNT, GOLD_FLOOR_AMOUNT, GOLD_CARD_AMOUNT); }
    public void onEquip(){ AbstractDungeon.player.gainGold(GOLD_PICKUP_AMOUNT); }
    public void onEnterRoom(AbstractRoom room) {
        flash();
        AbstractDungeon.player.gainGold(GOLD_FLOOR_AMOUNT);
    }
    public void onObtainCard(AbstractCard c) {
        /* 32 */     AbstractDungeon.player.gainGold(GOLD_CARD_AMOUNT);
        /*    */   }
}