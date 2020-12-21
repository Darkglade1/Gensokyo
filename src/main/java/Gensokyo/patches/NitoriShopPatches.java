package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import Gensokyo.dungeon.Gensokyoer;
import Gensokyo.dungeon.Gensokyoest;
import Gensokyo.rooms.nitori.NitoriRoom;
import Gensokyo.rooms.nitori.NitoriStoreScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class NitoriShopPatches {

    @SpireEnum
    public static AbstractDungeon.CurrentScreen NITORI_STORE;
    public static final String CLS = "com.megacrit.cardcrawl.dungeons.AbstractDungeon";
    public static final String CLS2 = "com.megacrit.cardcrawl.dungeons.AbstractDungeon";

    @SpirePatch(cls = CLS, method="closeCurrentScreen")
    public static class CloseCurrentScreen {
        public static void Prefix() {
            if(AbstractDungeon.screen == NITORI_STORE) {
                try {
                    Method overlayReset = AbstractDungeon.class.getDeclaredMethod("genericScreenOverlayReset");
                    overlayReset.setAccessible(true);
                    overlayReset.invoke(AbstractDungeon.class);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                AbstractDungeon.overlayMenu.hideBlackScreen();
                NitoriStoreScreen.close();
            }
        }
    }

    //TODO: Move to postfix to allow rendering on top of other screens
    @SpirePatch(cls = CLS, method = "render")
    public static class Render {

        @SpireInsertPatch(locator = RenderLocator.class)
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
            if(AbstractDungeon.screen == NITORI_STORE) NitoriStoreScreen.render(sb);
        }

        private static class RenderLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                        "com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");

                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(cls = CLS, method = "update")
    public static class Update {

        @SpireInsertPatch(locator = UpdateLocator.class)
        public static void Insert(AbstractDungeon __instance) {
            if(AbstractDungeon.screen == NITORI_STORE) NitoriStoreScreen.update();
        }

        private static class UpdateLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(
                        AbstractDungeon.class, "turnPhaseEffectActive"
                );

                int[] line = LineFinder.findInOrder(ctBehavior, matcher);

                return line;
            }
        }
    }

    //TODO: Replace with Locator
    @SpirePatch(cls = CLS2, method = "generateMap")
    public static class GenerateMap {
        //SL:637 = 36 right now
        @SpireInsertPatch(rloc = 37)// after AbstractDungeon.map = (ArrayList<ArrayList<MapRoomNode>>)RoomTypeAssigner.distributeRoomsAcrossMap(AbstractDungeon.mapRng, (ArrayList)AbstractDungeon.map, (ArrayList)roomList);
        public static void Insert() {
            insertNitori();
        }

        private static void insertNitori() {
            int rand;
            int rand2;
            ArrayList<MapRoomNode> shopNodes = new ArrayList<>();
            ArrayList<MapRoomNode> editableNodes = new ArrayList<>();
            if(CardCrawlGame.dungeon == null){ return; }
            for(ArrayList<MapRoomNode> rows : AbstractDungeon.map) {
                for(MapRoomNode node : rows) {
                    if(CardCrawlGame.dungeon instanceof Gensokyo || CardCrawlGame.dungeon instanceof Gensokyoer || CardCrawlGame.dungeon instanceof Gensokyoest) {
                        if (node != null && node.room instanceof ShopRoom) { shopNodes.add(node); }
                        if (node != null && !(node.room instanceof MonsterRoomBoss ||
                                node.room instanceof RestRoom ||
                                node.room instanceof ShopRoom ||
                                node.room instanceof MonsterRoomElite) && node.y > 2) { editableNodes.add(node); }
                    }
                }
            }

            if(shopNodes.size() > 1) {
                rand = AbstractDungeon.mapRng.random(shopNodes.size() - 1);
                shopNodes.get(rand).setRoom(new NitoriRoom());
                //shopNodes.get(rand).hasEmeraldKey = true;
            }
            else{
                try{
                    rand2 = AbstractDungeon.mapRng.random(editableNodes.size() - 1);
                    editableNodes.get(rand2).setRoom(new NitoriRoom());
                    //editableNodes.get(rand2).hasEmeraldKey = true;
                }
                catch (Exception e){}
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "updateMapButtonLogic")
    public static class AllowMapButton {
        @SpireInsertPatch(locator = Locator.class, localvars = {"mapButtonDisabled", "mapHb"})
        public static void interceptWrongScreen(TopPanel __instance, @ByRef boolean[] mapButtonDisabled, Hitbox mapHb) {
            if (AbstractDungeon.screen == NITORI_STORE) {
                mapButtonDisabled[0] = false;
                mapHb.update();

            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "updateDeckViewButtonLogic")
    public static class AllowDeckButton {
        @SpireInsertPatch(locator = Locator.class)
        public static void interceptWrongScreen(TopPanel __instance, @ByRef boolean[] ___deckButtonDisabled, Hitbox ___deckHb) {
            if (AbstractDungeon.screen == NITORI_STORE) {
                ___deckButtonDisabled[0] = false;
                ___deckHb.update();

            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}


