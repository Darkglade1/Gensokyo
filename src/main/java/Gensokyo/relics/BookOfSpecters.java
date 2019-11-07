package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.BookOfSpectersAction;
import Gensokyo.actions.BookOfSpectersFollowUpAction;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class BookOfSpecters extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("BookOfSpecters");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BookOfSpecters.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BookOfSpecters.png"));

    private static final int DRAW = 3;

    public BookOfSpecters() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new BookOfSpectersAction(AbstractDungeon.player, DRAW));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
        AbstractDungeon.actionManager.addToBottom(new BookOfSpectersFollowUpAction());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW + DESCRIPTIONS[1];
    }

}
