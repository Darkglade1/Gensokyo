package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act3.Doremy;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class DreamEater extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("DreamEater");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DreamEater84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DreamEater32.png"));

    public static final int BASIC_STRENGTH = 2;
    public static final int COMMON_STRENGTH = 0;
    public static final int UNCOMMON_STRENGTH = -2;
    public static final int RARE_STRENGTH = -4;

    public static final int A18_BASIC_STRENGTH = 3;
    public static final int A18_COMMON_STRENGTH = 1;
    public static final int A18_UNCOMMON_STRENGTH = -1;
    public static final int A18_RARE_STRENGTH = -3;

    private int basicStrength;
    private int commonStrength;
    private int uncommonStrength;
    private int rareStrength;

    public boolean triggered = false;

    private Doremy doremy;

    public DreamEater(AbstractCreature owner, int duration, boolean stronger, Doremy doremy) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = duration;
        this.type = PowerType.BUFF;
        this.doremy = doremy;

        if (!stronger) {
            basicStrength = BASIC_STRENGTH;
            commonStrength = COMMON_STRENGTH;
            uncommonStrength = UNCOMMON_STRENGTH;
            rareStrength = RARE_STRENGTH;
        } else {
            basicStrength = A18_BASIC_STRENGTH;
            commonStrength = A18_COMMON_STRENGTH;
            uncommonStrength = A18_UNCOMMON_STRENGTH;
            rareStrength = A18_RARE_STRENGTH;
        }

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!triggered) {
            this.flash();
            card.purgeOnUse = true;
            triggered = true;
            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
                gainStrengthBasedOnCard(card);
            }
        }
    }

    private void gainStrengthBasedOnCard(AbstractCard card) {
        int strength = 0;
        if (card.rarity == AbstractCard.CardRarity.RARE) {
            strength = rareStrength;
        } else if (card.rarity == AbstractCard.CardRarity.UNCOMMON) {
            strength = uncommonStrength;
        } else if (card.rarity == AbstractCard.CardRarity.COMMON) {
            strength = commonStrength;
        } else if (card.rarity == AbstractCard.CardRarity.BASIC) {
            strength = basicStrength;
        }
        if (strength != 0) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strength), strength));
        }
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public void atEndOfRound() {
        amount--;
        updateDescription();
        if (amount == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            doremy.transitionToNightmare();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        String basicString;
        if (basicStrength < 0) {
            basicString = DESCRIPTIONS[2] + DESCRIPTIONS[7] + Math.abs(basicStrength) + DESCRIPTIONS[8];
        } else {
            basicString = DESCRIPTIONS[2] + DESCRIPTIONS[6] + basicStrength + DESCRIPTIONS[8];
        }
        if (basicStrength != 0) {
            description += basicString;
        }
        
        String commonString;
        if (commonStrength < 0) {
            commonString = DESCRIPTIONS[3] + DESCRIPTIONS[7] + Math.abs(commonStrength) + DESCRIPTIONS[8];
        } else {
            commonString = DESCRIPTIONS[3] + DESCRIPTIONS[6] + commonStrength + DESCRIPTIONS[8];
        }
        if (commonStrength != 0) {
            description += commonString;
        }

        String uncommonString;
        if (uncommonStrength < 0) {
            uncommonString = DESCRIPTIONS[4] + DESCRIPTIONS[7] + Math.abs(uncommonStrength) + DESCRIPTIONS[8];
        } else {
            uncommonString = DESCRIPTIONS[4] + DESCRIPTIONS[6] + uncommonStrength + DESCRIPTIONS[8];
        }
        if (uncommonStrength != 0) {
            description += uncommonString;
        }

        String rareString;
        if (rareStrength < 0) {
            rareString = DESCRIPTIONS[5] + DESCRIPTIONS[7] + Math.abs(rareStrength) + DESCRIPTIONS[8];
        } else {
            rareString = DESCRIPTIONS[5] + DESCRIPTIONS[6] + rareStrength + DESCRIPTIONS[8];
        }
        if (rareStrength != 0) {
            description += rareString;
        }
        
    }
}
