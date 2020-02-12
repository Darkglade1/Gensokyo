package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.Animal;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class PolymorphPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("PolymorphPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean justApplied = false;
    public Animal animal = null;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Polymorph84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Polymorph32.png"));

    public PolymorphPower(AbstractCreature owner, int amount, boolean isSourceMonster) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = true;
        this.priority = 99;

        if (AbstractDungeon.actionManager.turnHasEnded && isSourceMonster) {
            this.justApplied = true;
        }

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        animal = new Animal();
        animal.isPlayer = true;
        animal.drawX = this.owner.drawX;
        animal.drawY = this.owner.drawY;
    }

    @Override
    public void onRemove() {
        animal = null;
    }

    @Override
    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return 0;
        } else {
            return damage;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(card.type == AbstractCard.CardType.ATTACK) {
            if (Settings.FAST_MODE) {
                animal.useFastAttackAnimation();
            } else {
                animal.useSlowAttackAnimation();
            }
        }
    }

    @Override
    public void onVictory() {
        this.onRemove();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
    }
}
