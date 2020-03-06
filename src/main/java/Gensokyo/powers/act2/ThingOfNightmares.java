package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.MarkHandAction;
import Gensokyo.monsters.act2.Reisen;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class ThingOfNightmares extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("ThingOfNightmares");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Reisen reisen;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Nightmare84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Nightmare32.png"));

    public ThingOfNightmares(AbstractCreature owner, Reisen reisen) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.reisen = reisen;

        type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onDeath() {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.flashWithoutSound();
            if (reisen != null) {
                this.addToTop(new MarkHandAction(reisen));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
