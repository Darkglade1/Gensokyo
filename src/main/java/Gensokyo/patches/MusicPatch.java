package Gensokyo.patches;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(cls = "com.megacrit.cardcrawl.audio.TempMusic", method = "getSong")
public class MusicPatch {
	
	public static Music Postfix(Music __result, TempMusic __instance, final String key) {
		switch (key) {
            case "Gensokyo/Necrofantasia.mp3": {
                return MainMusic.newMusic("audio/music/Gensokyo/Necrofantasia.mp3");
            }
			case "Gensokyo/TheLostEmotion.mp3": {
				return MainMusic.newMusic("audio/music/Gensokyo/TheLostEmotion.mp3");
			}
			case "Gensokyo/G Free.mp3": {
				return MainMusic.newMusic("audio/music/Gensokyo/G Free.mp3");
			}
			case "Gensokyo/Wind God Girl.mp3": {
				return MainMusic.newMusic("audio/music/Gensokyo/Wind God Girl.mp3");
			}
			case "Gensokyo/TomboyishGirl.mp3": {
				return MainMusic.newMusic("audio/music/Gensokyo/TomboyishGirl.mp3");
			}
			default: {
				return __result;
			}
		}
	}
}