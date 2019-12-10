package Gensokyo.patches;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(cls = "com.megacrit.cardcrawl.audio.TempMusic", method = "getSong")
public class MusicPatch {
	
	public static Music Postfix(Music __result, TempMusic __instance, final String key) {
		switch (key) {
            case "Gensokyo/Necrofantasia.ogg": {
                return MainMusic.newMusic("audio/music/Gensokyo/Necrofantasia.ogg");
            }
			case "Gensokyo/TheLostEmotion.ogg": {
				return MainMusic.newMusic("audio/music/Gensokyo/TheLostEmotion.ogg");
			}
			case "Gensokyo/G Free.ogg": {
				return MainMusic.newMusic("audio/music/Gensokyo/G Free.ogg");
			}
			case "Gensokyo/Wind God Girl.ogg": {
				return MainMusic.newMusic("audio/music/Gensokyo/Wind God Girl.ogg");
			}
			case "Gensokyo/TomboyishGirl.ogg": {
				return MainMusic.newMusic("audio/music/Gensokyo/TomboyishGirl.ogg");
			}
			case "Gensokyo/Futatsuiwa from Gensokyo.ogg": {
				return MainMusic.newMusic("audio/music/Gensokyo/Futatsuiwa from Gensokyo.ogg");
			}
			default: {
				return __result;
			}
		}
	}
}