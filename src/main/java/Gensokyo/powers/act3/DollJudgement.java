package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act3.Shinki.Alice;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class DollJudgement extends AbstractPower {
    public static final String POWER_ID = GensokyoMod.makeID("DollJudgement");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Doll84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Doll32.png"));

    private Alice alice;
    private int turns;

    public DollJudgement(AbstractCreature owner, int turns, Alice alice) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.turns = turns;
        this.amount = -1;
        this.alice = alice;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

//    @Override
//    public void onSpecificTrigger() {
//        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
//    }

    @Override
    public void atEndOfRound() {
        amount++;
        if (amount % turns == 0) {
            this.flash();
            alice.Summon();
            amount = 0;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + turns + DESCRIPTIONS[1];
    }
}
