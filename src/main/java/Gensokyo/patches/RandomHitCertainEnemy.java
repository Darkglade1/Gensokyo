package Gensokyo.patches;

import Gensokyo.monsters.act1.Reimu;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;

// A patch to make random effects always hit certain enemies in certain encounters
public class RandomHitCertainEnemy {

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "getRandomMonster",
            paramtypez = {
                    AbstractMonster.class,
                    boolean.class,
                    Random.class
            }
    )
    public static class HitThisGuyPatch1 {
        @SpirePostfixPatch()
        public static AbstractMonster HitThisGuy(AbstractMonster original, MonsterGroup instance, AbstractMonster exception, boolean aliveOnly, Random rng) {
            for (AbstractMonster mo : instance.monsters) {
                if (mo instanceof Reimu) {
                    if (!mo.isDeadOrEscaped()) {
                        return mo;
                    }
                }
            }
            return original;
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "getRandomMonster",
            paramtypez = {
                    AbstractMonster.class,
                    boolean.class
            }
    )
    public static class HitThisGuyPatch2 {
        @SpirePostfixPatch()
        public static AbstractMonster HitThisGuy(AbstractMonster original, MonsterGroup instance, AbstractMonster exception, boolean aliveOnly) {
            for (AbstractMonster mo : instance.monsters) {
                if (mo instanceof Reimu) {
                    if (!mo.isDeadOrEscaped()) {
                        return mo;
                    }
                }
            }
            return original;
        }
    }


}