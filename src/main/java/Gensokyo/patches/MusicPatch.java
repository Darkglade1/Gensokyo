package Gensokyo.patches;
import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.audio.*;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.badlogic.gdx.audio.*;

@SpirePatch(cls = "com.megacrit.cardcrawl.audio.TempMusic", method = "getSong")
public class MusicPatch {
	
	public static Music Postfix(Music __result, TempMusic __instance, final String key) {
		if (key.contains("Gensokyo/")) {
			return MainMusic.newMusic(GensokyoMod.makeEffectPath(key));
		}
		switch (key) {
            case "Gensokyo/Necrofantasia.mp3": {
                return MainMusic.newMusic(GensokyoMod.makeEffectPath("Gensokyo/Necrofantasia.mp3"));
            }
			default: {
				return __result;
			}
		}
		
	}
	
}