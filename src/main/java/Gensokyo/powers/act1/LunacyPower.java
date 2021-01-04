package Gensokyo.powers.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.HexPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class LunacyPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("LunacyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Lunacy84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Lunacy32.png"));

    //variable to try to make redirected damage and debuffs hit the same target
    public static AbstractMonster randomTarget = null;

    public LunacyPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (source instanceof AbstractMonster) {
            AbstractMonster mo = (AbstractMonster)source;
            if (mo.getIntentBaseDmg() >= 0 && source == owner && power.type == PowerType.DEBUFF) {
                AbstractMonster newTarget = randomTarget;
                if (newTarget == null) {
                    randomTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    newTarget = randomTarget;
                }
                power.owner = newTarget;
                AbstractDungeon.actionManager.currentAction.target = newTarget;
                if (power instanceof HexPower) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(newTarget, newTarget, power.ID));
                }
            }
        }
    }

    @Override
    public void duringTurn() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                randomTarget = null;
                this.isDone = true;
            }
        });
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
       description = DESCRIPTIONS[0];
    }
}
