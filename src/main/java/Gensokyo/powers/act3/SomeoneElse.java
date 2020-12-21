package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.events.act3.SomeoneElsesStory;
import Gensokyo.monsters.act3.Mokou;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.EventRoom;

import static Gensokyo.GensokyoMod.makePowerPath;

public class SomeoneElse extends AbstractPower implements OnPlayerDeathPower {

    public static final String POWER_ID = GensokyoMod.makeID("SomeoneElse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Mokou mokou;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Moon84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Moon32.png"));

    public SomeoneElse(AbstractCreature owner, int amount, Mokou mokou) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.mokou = mokou;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
        if (AbstractDungeon.getCurrRoom() instanceof EventRoom) {
            EventRoom event = (EventRoom) AbstractDungeon.getCurrRoom();
            if (event.event instanceof SomeoneElsesStory) {
                ((SomeoneElsesStory) event.event).mokouWins();
            }
        }
        return false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
