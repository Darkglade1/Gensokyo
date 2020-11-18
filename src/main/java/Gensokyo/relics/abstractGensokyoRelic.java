package Gensokyo.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

abstract class abstractGensokyoRelic extends CustomRelic {
    public abstractGensokyoRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);

    }
}
