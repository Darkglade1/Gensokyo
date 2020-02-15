package Gensokyo.patches;

import Gensokyo.monsters.act2.Kaguya;
import Gensokyo.monsters.act1.Kokoro;
import Gensokyo.monsters.act1.Reimu;
import Gensokyo.monsters.act1.Yukari;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.CustomBosses;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import javassist.CtBehavior;
import org.apache.logging.log4j.Logger;

@SpirePatch(
        clz = CustomBosses.SetBossIcon.class,
        method = "Prefix"
)
// A patch to make basemod reload my boss icons so they stop showing up as black boxes
public class BossIconBlackBoxPatch {
    @SpireInsertPatch(locator = BossIconBlackBoxPatch.Locator.class)
    public static void StopTheBlackBox(AbstractDungeon _instance, String key) {
        if (key.equals(Reimu.ID)) {
            DungeonMap.boss = ImageMaster.loadImage("GensokyoResources/images/monsters/Reimu/Reimu.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("GensokyoResources/images/monsters/Reimu/ReimuOutline.png");
        } else if (key.equals(Yukari.ID)) {
            DungeonMap.boss = ImageMaster.loadImage("GensokyoResources/images/monsters/Yukari/Yukari.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("GensokyoResources/images/monsters/Yukari/YukariOutline.png");
        } else if (key.equals(Kokoro.ID)) {
            DungeonMap.boss = ImageMaster.loadImage("GensokyoResources/images/monsters/Kokoro/Kokoro.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("GensokyoResources/images/monsters/Kokoro/KokoroOutline.png");
        } else if (key.equals(Kaguya.ID)) {
            DungeonMap.boss = ImageMaster.loadImage("GensokyoResources/images/monsters/Kaguya/Kaguya.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("GensokyoResources/images/monsters/Kaguya/KaguyaOutline.png");
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Logger.class, "info");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}