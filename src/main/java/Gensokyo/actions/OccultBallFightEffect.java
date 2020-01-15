package Gensokyo.actions;

import Gensokyo.monsters.Sumireko;
import Gensokyo.relics.OccultBall;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class OccultBallFightEffect extends AbstractGameEffect {

    public OccultBallFightEffect() {
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
            ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
        }
        CardCrawlGame.music.unsilenceBGM();
        final MapRoomNode cur2 = AbstractDungeon.currMapNode;
        final MapRoomNode node2 = new MapRoomNode(cur2.x, cur2.y);
        node2.room = new MonsterRoom();
        AbstractMonster mo = new Sumireko();
        node2.room.monsters = new MonsterGroup(mo);
        mo.rollMove();
        mo.createIntent();
        mo.healthBarUpdatedEvent();
        node2.room.rewards.clear();
        node2.room.addRelicToRewards(RelicLibrary.getRelic(OccultBall.ID).makeCopy());
        node2.room.eliteTrigger = true;
        final ArrayList<MapEdge> curEdges2 = cur2.getEdges();
        for (final MapEdge edge : curEdges2) {
            node2.addEdge(edge);
        }
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.fadeIn();
        AbstractDungeon.dungeonMapScreen.dismissable = true;
        AbstractDungeon.setCurrMapNode(AbstractDungeon.nextRoom = node2);
        AbstractDungeon.getCurrRoom().onPlayerEntry();
        AbstractDungeon.player.preBattlePrep();
        AbstractDungeon.scene.nextRoom(node2.room);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        cur2.taken = true;

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
