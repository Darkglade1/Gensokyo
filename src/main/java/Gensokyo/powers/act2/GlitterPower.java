package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.GlitterDamageAction;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class GlitterPower extends TwoAmountPower implements OnReceivePowerPower {

    public static final String POWER_ID = GensokyoMod.makeID("GlitterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Glitter84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Glitter32.png"));

    public GlitterPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new UpdateDescriptionAction(this));
        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new GlitterDamageAction(this.owner, this.amount));
        AbstractDungeon.actionManager.addToBottom(new UpdateDescriptionAction(this));
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new UpdateDescriptionAction(this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + calcDamage() + DESCRIPTIONS[2];
    }

    private int calcDamage() {
        int debuffs = 0;
        for (AbstractPower power : this.owner.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs++;
            }
        }
        this.amount2 = debuffs * this.amount;
        return debuffs * this.amount;
    }

    private class UpdateDescriptionAction extends AbstractGameAction {
        AbstractPower power;

        public UpdateDescriptionAction(AbstractPower power) {
            this.actionType = ActionType.SPECIAL;
            this.duration = Settings.ACTION_DUR_FAST;
            this.power = power;
        }

        public void update() {
            this.isDone = false;

            power.updateDescription();

            this.isDone = true;
        }
    }
}
