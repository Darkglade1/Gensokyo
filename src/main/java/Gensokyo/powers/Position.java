package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Reimu;
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

    private static float movement = Reimu.orbOffset * Settings.scale;
    private float position1;
    private float position2;
    private float position3;

    public Position(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        //this.originalY = owner.drawY;
        this.position1 = owner.drawY;
        this.position2 = owner.drawY + movement;
        this.position3 = owner.drawY + movement * 2;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updatePosition();
        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount > 1) {
                this.flash();
                amount--;
            }
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            if (amount < 3) {
                this.flash();
                amount++;
            }
        }
        updatePosition();
        updateDescription();
    }

    private void updatePosition() {
        if (amount == 1) {
            owner.drawY = position1;
        } else if (amount == 2) {
            owner.drawY = position2;
        } else if (amount == 3) {
            owner.drawY = position3;
        }
        fixOrbPositioning();
    }

    private void fixOrbPositioning() {
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            (AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        //Don't stack this shit
    }

    @Override
    public void reducePower(int reduceAmount) {
        //Don't reduce this shit
    }

    @Override
    public void onRemove() {
        super.onRemove();
        owner.drawY = position1;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
