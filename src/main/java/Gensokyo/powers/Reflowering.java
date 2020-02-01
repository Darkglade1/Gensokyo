package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.Butterfly;
import Gensokyo.monsters.bossRush.Yuyuko;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class Reflowering extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Reflowering");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private Yuyuko yuyuko;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Reflowering84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Reflowering32.png"));

    public Reflowering(AbstractCreature owner, Yuyuko yuyuko) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.yuyuko = yuyuko;

        type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof Butterfly) {
            yuyuko.incrementFan(1);
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + Yuyuko.FAN_THRESHOLD + DESCRIPTIONS[1];
    }
}
