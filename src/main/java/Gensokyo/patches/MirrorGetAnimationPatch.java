package Gensokyo.patches;

import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpriterAnimation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;

public class MirrorGetAnimationPatch {

    public static String playerAtlasURL;
    public static String playerSkeletonURL;
    public static float playerScale;

    public static int playerTrackIndex;
    public static String playerAnimationName;
    public static boolean playerLoop;

    public static String playerFrom;
    public static String playerTo;
    public static float playerDuration;

    public static float playerTimeScale;

    public static boolean isPlayerTrackEntry = false;
    public static boolean isPlayerStateData = false;
    public static boolean isPlayerTimeScale = false;

    //public static boolean canSpriterAnimation = false;
    public static String spriterAnimation;
    public static String TempSpriterAnimation;

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CONSTRUCTOR
    )
    public static class NullEverything {
        @SpirePrefixPatch
        public static void nullEverything(AbstractPlayer _instance, String name, AbstractPlayer.PlayerClass className) {
            playerAtlasURL = null;
            playerSkeletonURL = null;
            playerScale = 0.0F;
            playerTrackIndex = 0;
            playerAnimationName = null;
            playerLoop = false;
            playerFrom = null;
            playerTo = null;
            playerDuration = 0.0F;
            playerTimeScale = 0.0F;
            isPlayerTrackEntry = false;
            isPlayerStateData = false;
            isPlayerTimeScale = false;
            ReversalEventPatches.clearLists(); //piggy-back this here too
        }
    }

    @SpirePatch(
            clz= CustomPlayer.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = { String.class, AbstractPlayer.PlayerClass.class, EnergyOrbInterface.class, AbstractAnimation.class }
    )
    public static class SpriterCheck {
        @SpirePrefixPatch
        public static void spriterPls(CustomPlayer _instance, String name, AbstractPlayer.PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, AbstractAnimation animation) {
            //This only works if a new spriterAnimation is instantiated right before the supercall so spriter-using mods that don't do this get mirrored properly
            if (animation instanceof SpriterAnimation) {
                spriterAnimation = TempSpriterAnimation;
            }
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="loadAnimation"
    )
    public static class GetAnimationStrings {
        @SpirePostfixPatch
        public static void renderMirror(AbstractCreature _instance, String atlasUrl, String skeletonUrl, float scale) {
            if (_instance instanceof AbstractPlayer) {
                playerAtlasURL = atlasUrl;
                playerSkeletonURL = skeletonUrl;
                playerScale = scale;
                isPlayerTrackEntry = true;
                isPlayerStateData = true;
                isPlayerTimeScale = true;
            }
        }
    }

    @SpirePatch(
            clz= AnimationState.class,
            method="setAnimation",
            paramtypez = { int.class, String.class, boolean.class }

    )
    public static class GetTrackEntry {
        @SpirePostfixPatch
        public static void getTrackEntry(AnimationState _instance, int trackIndex, String animationName, boolean loop) {
            if (isPlayerTrackEntry) {
                playerTrackIndex = trackIndex;
                playerAnimationName = animationName;
                playerLoop = loop;
                isPlayerTrackEntry = false;
            }
        }
    }

    @SpirePatch(
            clz= AnimationStateData.class,
            method="setMix",
            paramtypez = { String.class, String.class, float.class }

    )
    public static class GetStateData {
        @SpirePostfixPatch
        public static void getStateData(AnimationStateData _instance, String from, String To, float duration) {
            if (isPlayerStateData) {
                playerFrom = from;
                playerTo = To;
                playerDuration = duration;
                isPlayerStateData = false;
            }
        }
    }

    @SpirePatch(
            clz= AnimationState.class,
            method="setTimeScale"

    )
    public static class GetTimeScale {
        @SpirePostfixPatch
        public static void getTimeScale(AnimationState _instance, float timeScale) {
            if (isPlayerTimeScale) {
                playerTimeScale = timeScale;
                isPlayerTimeScale = false;
            }
        }
    }

    @SpirePatch(
            clz= SpriterAnimation.class,
            method=SpirePatch.CONSTRUCTOR

    )
    public static class GetSprite {
        @SpirePostfixPatch
        public static void getSprite(SpriterAnimation _instance, String filepath) {
            TempSpriterAnimation = filepath;
        }
    }
}