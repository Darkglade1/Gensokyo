package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import Gensokyo.util.EventFilter;
import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(cls = "com.megacrit.cardcrawl.mod.replay.vfx.campfire.CampfireExploreEffect", method = "update", optional = true)
public class ReplayNeowRandomEventPatch {
	@SpireInsertPatch(locator = ReplayNeowRandomEventPatch.Locator.class)
	public static SpireReturn<Void> FixHardcodedEvents() {
		if (AbstractDungeon.id.equals(Gensokyo.ID)) {
			ArrayList<String> possibleEvents = new ArrayList<>(AbstractDungeon.eventList);
			ArrayList<String> eventsToRemove = EventFilter.FilterEvents(possibleEvents);
			for (String event : eventsToRemove) {
				possibleEvents.remove(event);
			}
			String evt = possibleEvents.get(AbstractDungeon.eventRng.random(possibleEvents.size() - 1));
			AbstractDungeon.eventList.remove(evt);
			CardCrawlGame.music.unsilenceBGM();
			AbstractDungeon.eventList.add(0, evt);
			final MapRoomNode cur = AbstractDungeon.currMapNode;
			final MapRoomNode node = new MapRoomNode(cur.x, cur.y);
			node.room = new CustomEventRoom();
			final ArrayList<MapEdge> curEdges = cur.getEdges();
			for (final MapEdge edge : curEdges) {
				node.addEdge(edge);
			}
			AbstractDungeon.previousScreen = null;
			AbstractDungeon.dynamicBanner.hide();
			AbstractDungeon.dungeonMapScreen.closeInstantly();
			AbstractDungeon.closeCurrentScreen();
			AbstractDungeon.topPanel.unhoverHitboxes();
			AbstractDungeon.fadeIn();
			AbstractDungeon.dungeonMapScreen.dismissable = true;
			AbstractDungeon.setCurrMapNode(AbstractDungeon.nextRoom = node);
			AbstractDungeon.getCurrRoom().onPlayerEntry();
			AbstractDungeon.scene.nextRoom(node.room);
			AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
			return SpireReturn.Return(null);
		} else {
			return SpireReturn.Continue();
		}

	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(MusicMaster.class, "unsilenceBGM");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}