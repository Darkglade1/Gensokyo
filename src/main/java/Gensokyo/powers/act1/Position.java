package Gensokyo.powers.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act1.Reimu;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class Position extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("PositionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SpellCard84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SpellCard32.png"));

    private float originalY;
    private static float movement = Reimu.orbOffset * Settings.scale;

    public Position(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.originalY = owner.drawY;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount > 1) {
                this.flash();
                amount--;
                owner.drawY -= movement;
                fixOrbPositioning();
            }
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            if (amount < 3) {
                this.flash();
                amount++;
                owner.drawY += movement;
                fixOrbPositioning();
            }
        }
        updateDescription();
    }

    private void fixOrbPositioning() {
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            (AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        owner.drawY = originalY;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
