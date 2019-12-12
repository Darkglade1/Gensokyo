package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class Grudge extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Grudge");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Grudge84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Grudge32.png"));

    public Grudge(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, StrengthPower.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, numberOfCursesInPlayerDeck()), numberOfCursesInPlayerDeck()));
    }

    private int numberOfCursesInPlayerDeck() {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
                count++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
                count++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];

    }
}
