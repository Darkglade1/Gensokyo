package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class NagashiBinaDoll extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("NagashiBinaDoll");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NagashiBinaDoll.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NagashiBinaDoll.png"));

    private static final int COMBATS = 15;
    private static final int TEMP_HP = 2;

    public NagashiBinaDoll() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = COMBATS;
    }

    @Override
    public void atBattleStartPreDraw() {
        ArrayList<AbstractCard> exhaustList = new ArrayList<>();
        AbstractPlayer p = AbstractDungeon.player;
        int cardsExhausted = 0;
        for (AbstractCard card : p.hand.group) {
            if (card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE) {
                exhaustList.add(card);
            }
        }
        cardsExhausted += exhaustList.size();
        for (AbstractCard card : exhaustList) {
            p.hand.moveToExhaustPile(card);
        }
        exhaustList.clear();

        for (AbstractCard card : p.drawPile.group) {
            if (card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE) {
                exhaustList.add(card);
            }
        }
        cardsExhausted += exhaustList.size();
        for (AbstractCard card : exhaustList) {
            p.drawPile.moveToExhaustPile(card);
        }
        exhaustList.clear();

        for (AbstractCard card : p.discardPile.group) {
            if (card.color == AbstractCard.CardColor.CURSE || card.type == AbstractCard.CardType.CURSE) {
                exhaustList.add(card);
            }
        }
        cardsExhausted += exhaustList.size();
        for (AbstractCard card : exhaustList) {
            p.discardPile.moveToExhaustPile(card);
        }
        exhaustList.clear();
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p,cardsExhausted * TEMP_HP));
        this.counter--;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + COMBATS + DESCRIPTIONS[1] + TEMP_HP + DESCRIPTIONS[2];
    }

}
