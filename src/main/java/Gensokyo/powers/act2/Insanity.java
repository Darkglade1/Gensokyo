package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class Insanity extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Insanity");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int THRESHOLD_1 = 10;
    private static final int THRESHOLD_2 = 20;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Insanity84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Insanity32.png"));

    public Insanity(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount >= THRESHOLD_2) {
            description = DESCRIPTIONS[2];
        } else if (amount >= THRESHOLD_1) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }
}
