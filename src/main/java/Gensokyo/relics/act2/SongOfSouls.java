package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.ArrayList;
import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class SongOfSouls extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("SongOfSouls");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Piano.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Piano.png"));

    public SongOfSouls() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.removeStrikeTip();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE) || card.tags.contains(AbstractCard.CardTags.STARTER_DEFEND)) {
            this.flash();
            action.exhaustCard = true;
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    private void removeStrikeTip() {
        ArrayList<String> strikes = new ArrayList();
        String[] var2 = GameDictionary.STRIKE.NAMES;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            strikes.add(s.toLowerCase());
        }

        Iterator t = this.tips.iterator();

        while(t.hasNext()) {
            PowerTip derp = (PowerTip)t.next();
            String s = derp.header.toLowerCase();
            if (strikes.contains(s)) {
                t.remove();
                break;
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
