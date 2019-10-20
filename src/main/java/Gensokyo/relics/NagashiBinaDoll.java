package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class NagashiBinaDoll extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("NagashiBinaDoll");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NagashiBinaDoll.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NagashiBinaDoll.png"));

    public NagashiBinaDoll() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onPlayerEndTurn() {
        ArrayList<AbstractCard> curses = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE) {
                if (!card.isEthereal) { //ignore Ethereal curses since they exhaust anyway
                    curses.add(card);
                }
            }
        }
        if (curses.size() > 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.hand.moveToExhaustPile(curses.get(AbstractDungeon.cardRandomRng.random(curses.size() - 1)));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
