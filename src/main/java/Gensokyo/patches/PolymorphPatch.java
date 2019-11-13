package Gensokyo.patches;

import Gensokyo.monsters.Animal;
import Gensokyo.powers.PolymorphPower;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.Iterator;

public class PolymorphPatch {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="render"
    )
    public static class RenderPatch {
        public static SpireReturn Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if(AbstractDungeon.player.hasPower(PolymorphPower.POWER_ID)) {
                PolymorphPower power = (PolymorphPower)AbstractDungeon.player.getPower(PolymorphPower.POWER_ID);
                Animal animal = power.animal;
                if (animal != null) {
                    __instance.renderHealth(sb);
                    if (!__instance.orbs.isEmpty()) {
                        Iterator var2 = __instance.orbs.iterator();

                        while(var2.hasNext()) {
                            AbstractOrb o = (AbstractOrb)var2.next();
                            o.render(sb);
                        }
                    }
                    __instance.hb.render(sb);
                    __instance.healthHb.render(sb);

                    animal.render(sb);

                    return SpireReturn.Return(null);
                } else {
                    return SpireReturn.Continue();
                }
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="update"
    )
    public static class UpdatePatch {
        public static void Prefix(AbstractPlayer __instance) {
            if(AbstractDungeon.player.hasPower(PolymorphPower.POWER_ID)) {
                PolymorphPower power = (PolymorphPower)AbstractDungeon.player.getPower(PolymorphPower.POWER_ID);
                Animal animal = power.animal;
                if(animal != null) {
                    animal.update();
                }
            }
        }
    }
}