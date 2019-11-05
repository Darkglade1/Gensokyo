package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.SpawnOrbAction;
import Gensokyo.monsters.Reimu;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class HakureiShrineMaidenPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("HakureiShrineMaidenPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Hakurei84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Hakurei32.png"));

    public HakureiShrineMaidenPower(AbstractCreature owner) {
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
    public void duringTurn() { //we use this instead of end of turn so the spawned orbs roll their moves for the next turn correctly
        Reimu reimu = (Reimu) owner;
        if (reimu.orbNum() == 0) {
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 2));
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 3));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 3));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
