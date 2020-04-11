package Gensokyo.patches;

import Gensokyo.monsters.act1.Yukari;
import Gensokyo.powers.act1.FortitudePower;
import Gensokyo.powers.act1.SturdyPower;
import Gensokyo.powers.act1.VigorPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import javassist.CtBehavior;

public class HighLightInvertiblePowerPatches {

    @SpirePatch(clz = AbstractPower.class, method = "renderIcons")
    public static class HighLight {
        @SpireInsertPatch(locator = LocatorMulti.class)
        public static void patch(AbstractPower __instance, SpriteBatch sb, float x, float y, Color c) {
            if (fightingYukari() && isInvertible(__instance)) {
                Color tmp = c.cpy();
                tmp.a = 0.5f;
                tmp.mul(1.5f, 1.5f, 1.5f, 1.0f);
                if (__instance.img != null) {
                    sb.setColor(tmp);
                    sb.draw(__instance.img, x - 12.0F, y - 12.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale * 2.5F, Settings.scale * 2.5F, 0.0F, 0, 0, 32, 32, false, false);
                } else {
                    sb.setColor(tmp);
                    sb.draw(__instance.region48, x - __instance.region48.packedWidth / 2.0F, y - __instance.region48.packedHeight / 2.0F, __instance.region48.packedWidth / 2.0F, __instance.region48.packedHeight / 2.0F, __instance.region48.packedWidth, __instance.region48.packedHeight, Settings.scale*1.5f, Settings.scale*1.5f, 0.0F);
                }
            }
        }

        private static boolean isInvertible(AbstractPower power) {
            if (power.canGoNegative
                || power instanceof VulnerablePower || power instanceof WeakPower || power instanceof FrailPower
                || power instanceof FortitudePower || power instanceof VigorPower || power instanceof SturdyPower
                || power instanceof GainStrengthPower || power instanceof LoseStrengthPower) {
                return true;
            }
            return false;
        }

        private static boolean fightingYukari() {
            if (AbstractDungeon.getCurrRoom() != null) {
                for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (monster instanceof Yukari) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static class LocatorMulti extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}