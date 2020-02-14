package Gensokyo.patches;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

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
}