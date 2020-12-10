package Gensokyo.powers.act3;

import Gensokyo.CardMods.ObscureMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UnknownCurse extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("UnknownCurse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean alwaysObscure;

    public UnknownCurse(AbstractCreature owner, boolean alwaysObscure) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.alwaysObscure = alwaysObscure;
        this.loadRegion("confusion");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (owner == AbstractDungeon.player) {
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (AbstractDungeon.cardRandomRng.randomBoolean() || alwaysObscure) {
                    CardModifierManager.addModifier(card, new ObscureMod());
                }
            }
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (AbstractDungeon.cardRandomRng.randomBoolean() || alwaysObscure) {
                    CardModifierManager.addModifier(card, new ObscureMod());
                }
            }
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (AbstractDungeon.cardRandomRng.randomBoolean() || alwaysObscure) {
                    CardModifierManager.addModifier(card, new ObscureMod());
                }
            }
            for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                if (AbstractDungeon.cardRandomRng.randomBoolean() || alwaysObscure) {
                    CardModifierManager.addModifier(card, new ObscureMod());
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
