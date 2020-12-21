package Gensokyo.relics.act1.marisaRelics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.CallbackExhaustAction;
import Gensokyo.cards.UrbanLegend.AbstractUrbanLegendCard;
import Gensokyo.relics.act1.OccultBall;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class PhilosophersStone extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("MarisaPhilosophersStone");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Stone.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Stone.png"));

    private static final int EXHAUST = 1;

    public PhilosophersStone() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new CallbackExhaustAction(EXHAUST, false, false, false, cards -> cards.forEach(this::transmuteCard)));
    }

    private void transmuteCard(AbstractCard card) {
        AbstractCard c;
        if (card instanceof AbstractUrbanLegendCard) {
            c = OccultBall.return3TrulyRandomUrbanLegendInCombat(AbstractDungeon.cardRandomRng).get(0);
        } else if (card.rarity == AbstractCard.CardRarity.RARE || card.rarity == AbstractCard.CardRarity.UNCOMMON) {
            c =  AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
        } else if (card.rarity == AbstractCard.CardRarity.COMMON) {
            c =  AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).makeCopy();
        } else {
            c =  AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
