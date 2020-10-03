package Gensokyo.rooms.nitori;

import Gensokyo.events.extra.CandidFriend;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

import static Gensokyo.GensokyoMod.makeUIPath;

public class NitoriRoom extends AbstractRoom {

    public NitoriRoom() {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "COS";
        this.mapImg = new Texture(makeUIPath("nitori.png"));
        this.mapImgOutline = new Texture(makeUIPath("nitori_outline.png"));
    }

    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        NitoriStoreScreen.init();
        AbstractDungeon.topLevelEffects.clear();
        AbstractDungeon.effectList.clear();
        AbstractDungeon.currMapNode.room = new EventRoom();
        AbstractDungeon.currMapNode.room.setMapImg(new Texture(makeUIPath("nitori.png")), new Texture(makeUIPath("nitori_outline.png")));
        AbstractDungeon.getCurrRoom().event = new CandidFriend();
        AbstractDungeon.getCurrRoom().event.reopen();
        CardCrawlGame.fadeIn(1.5F);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
        AbstractDungeon.overlayMenu.hideCombatPanels();
    }
}
