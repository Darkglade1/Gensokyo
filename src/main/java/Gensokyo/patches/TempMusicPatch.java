package Gensokyo.patches;

import Gensokyo.dungeon.Gensokyo;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong")
public class TempMusicPatch {
    //Lets you start custom music from e.g. an elite fight.
    @SpirePostfixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        if(Gensokyo.tempmusic.containsKey(key)) {
            return SpireReturn.Return(MainMusic.newMusic(Gensokyo.tempmusic.get(key)));
        }
        return SpireReturn.Continue();
    }

}
