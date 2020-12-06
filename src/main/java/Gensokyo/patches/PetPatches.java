package Gensokyo.patches;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.Pets.AbstractSummonPetCard;
import Gensokyo.minions.AbstractPet;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CtBehavior;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

public class PetPatches {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="onVictory"
    )
    public static class OnVictoryPatch {
        public static void Prefix(AbstractPlayer __instance) {
            if (GensokyoMod.hasFriendlyMinions) {
                MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(__instance);
                for (AbstractMonster mo : playerMinions.monsters) {
                    if (mo instanceof AbstractPet) {
                        AbstractPet pet = (AbstractPet)mo;
                        pet.onVictoryUpdateHP();
                    }
                }
            }
        }
    }

    @SpirePatch(clz = CampfireSleepEffect.class, method = "update")
    public static class CampfirePetHeal {
        @SpireInsertPatch(locator = PlayerHealLocator.class)
        public static void Insert(CampfireSleepEffect __instance) {
            if (GensokyoMod.hasFriendlyMinions) {
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card instanceof AbstractSummonPetCard) {
                        AbstractSummonPetCard summonPetCard = (AbstractSummonPetCard)card;
                        summonPetCard.updateHP(summonPetCard.max_hp);
                    }
                }
            }
        }
    }

    private static class PlayerHealLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "heal");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class DungeonTransitionHeal {
        @SpirePostfixPatch
        public static void Postfix() {
            if (GensokyoMod.hasFriendlyMinions) {
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card instanceof AbstractSummonPetCard) {
                        AbstractSummonPetCard summonPetCard = (AbstractSummonPetCard)card;
                        summonPetCard.updateHP(summonPetCard.max_hp);
                    }
                }
            }
        }
    }

}