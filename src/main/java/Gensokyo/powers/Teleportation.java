package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Sumireko;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class Teleportation extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Teleportation");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;
    public static final int NUM_POSITIONS = 3;
    private static final float DAMAGE_MOD = 1.5F;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SpellCard84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SpellCard32.png"));

    public static float movement = 480.0F * Settings.scale;

    public Teleportation(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        amount++;
        if (amount > NUM_POSITIONS) {
            amount = LEFT;
        }
        this.flash();
        if (amount == LEFT) {
            owner.drawX -= movement * 2;
        } else {
            owner.drawX += movement;
        }
        if (amount == LEFT) {
            if (this.owner instanceof AbstractPlayer) {
                this.owner.flipHorizontal = false;
            }
            if (this.owner instanceof Sumireko) {
                ((Sumireko)this.owner).setFlip(true, false);
            }
        }
        if (amount == MIDDLE) {
            if (this.owner instanceof AbstractPlayer) {
                this.owner.flipHorizontal = false;
            }
            if (this.owner instanceof Sumireko) {
                int playerPosition;
                if (AbstractDungeon.player.hasPower(Teleportation.POWER_ID)) {
                    playerPosition = AbstractDungeon.player.getPower(Teleportation.POWER_ID).amount;
                } else {
                    playerPosition = 0;
                }
                if (playerPosition == LEFT) {
                    ((Sumireko)this.owner).setFlip(false, false);
                } else if (playerPosition == RIGHT) {
                    ((Sumireko)this.owner).setFlip(true, false);
                }
            }
        }
        if (amount == RIGHT) {
            if (this.owner instanceof AbstractPlayer) {
                this.owner.flipHorizontal = true;
            }
            if (this.owner instanceof Sumireko) {
                ((Sumireko)this.owner).setFlip(false, false);
            }
        }
        fixOrbPositioning();
        AbstractDungeon.onModifyPower();
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.amount == MIDDLE) {
            return damage * DAMAGE_MOD;
        } else {
            return damage;
        }
    }

    private void fixOrbPositioning() {
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            (AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
        }
    }

    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        if (amount == LEFT) {
            description = DESCRIPTIONS[0];
        } else if (amount == MIDDLE) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[2];
        }
        description += DESCRIPTIONS[3];
    }
}
