package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Komachi;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class CelestialsFlawlessClothing extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("CelestialsFlawlessClothing");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CelestialsFlawlessClothing.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CelestialsFlawlessClothing.png"));

    public AbstractMonster elite;
    public int triggerCount = 0;

    public CelestialsFlawlessClothing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onTrigger() {
        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        int healAmt = AbstractDungeon.player.maxHealth;
        if (healAmt < 1) {
            healAmt = 1;
        }

        AbstractDungeon.player.heal(healAmt, true);
        triggerCount++;
        elite = new Komachi(-600.0f, 0);
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(elite, false));
        elite.usePreBattleAction();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
