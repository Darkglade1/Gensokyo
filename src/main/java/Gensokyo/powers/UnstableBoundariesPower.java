package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.InvertPowersAction;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static Gensokyo.GensokyoMod.makePowerPath;


public class UnstableBoundariesPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("UnstableBoundariesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int threshold = 5;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("UnstableBoundaries84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("UnstableBoundaries32.png"));

    public UnstableBoundariesPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        ++this.amount;
        if (this.amount == threshold) {
            this.amount = 0;
            AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(AbstractDungeon.player, false));
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(var3.hasNext()) {
                AbstractMonster mo = (AbstractMonster)var3.next();
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(mo, false));
            }
        }
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0] + threshold + DESCRIPTIONS[1];
    }
}
