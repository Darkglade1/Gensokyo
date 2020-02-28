package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class DayTheSeaSplit extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("DayTheSeaSplit");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Sea.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Sea.png"));

    public static final int COMBATS = 2;

    public DayTheSeaSplit() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = COMBATS;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (counter > 0) {
            if (card.type == AbstractCard.CardType.SKILL) {
                this.flash();
                action.exhaustCard = true;
            }
        }
    }

    @Override
    public void onVictory() {
        this.counter--;
        if (this.counter <= 0) {
            counter = 0;
            AbstractRelic relic = RelicLibrary.getRelic(MosesMiracle.ID).makeCopy();
            AbstractDungeon.getCurrRoom().addRelicToRewards(relic);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + COMBATS + DESCRIPTIONS[1];
    }

}
