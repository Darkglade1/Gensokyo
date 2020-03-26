package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act2.Tenshi;
import Gensokyo.util.TextureLoader;
import Gensokyo.vfx.CherryBlossomEffect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class Weather extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Weather");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Philosophy84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Philosophy32.png"));

    private static final int ENERGY = 1;
    private static final int DRAW = 2;
    private static final int A18_DRAW = 1;
    private static final int ARTIFACT = 1;
    private static final int VULNERABLE = 1;

    private int HP_THRESHOLD_1;
    private int HP_THRESHOLD_2;
    private int weather = Tenshi.WEATHER_1;
    private Tenshi tenshi;
    private int draw;
    private InvisibleAdrenalinePower power;

    public Weather(AbstractCreature owner, int amount, Tenshi tenshi) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        HP_THRESHOLD_1 = amount;
        HP_THRESHOLD_2 = amount;
        this.tenshi = tenshi;

        if (AbstractDungeon.ascensionLevel >= 18) {
            draw = A18_DRAW;
        } else {
            draw = DRAW;
        }

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        this.addToBot(new VFXAction(new CherryBlossomEffect(), 0.7F));
        power = new InvisibleAdrenalinePower(AbstractDungeon.player, ENERGY, draw);
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            if (this.weather == Tenshi.WEATHER_1) {
                HP_THRESHOLD_1 -= damageAmount;
            }
            if (HP_THRESHOLD_1 < 0) {
                int overkillDamage = -HP_THRESHOLD_1;
                HP_THRESHOLD_2 -= overkillDamage;
                HP_THRESHOLD_1 = 0;
            }
            if (this.weather == Tenshi.WEATHER_2) {
                HP_THRESHOLD_2 -= damageAmount;
            }
            if (HP_THRESHOLD_2 < 0) {
                HP_THRESHOLD_2 = 0;
            }
            amount -= damageAmount;
            if (amount < 0) {
                amount = 0;
            }
            updateDescription();
        }
    }

    @Override
    public void atEndOfRound() {
        if (HP_THRESHOLD_2 == 0) {
            tenshi.weather = Tenshi.WEATHER_3;
            this.weather = Tenshi.WEATHER_3;
            HP_THRESHOLD_2 = -1;
            HP_THRESHOLD_1 = -1;
            updateDescription();
            power.enabled = false;
        } else if (HP_THRESHOLD_1 == 0) {
            tenshi.weather = Tenshi.WEATHER_2;
            this.weather = Tenshi.WEATHER_2;
            HP_THRESHOLD_1 = -1;
            this.amount = HP_THRESHOLD_2;
            updateDescription();
            power.enabled = false;
        }
        this.flash();
        if (this.weather == Tenshi.WEATHER_1) {
            this.addToBot(new VFXAction(new CherryBlossomEffect(), 0.7F));
        }
        if (this.weather == Tenshi.WEATHER_2) {
            if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, ARTIFACT)));
            }
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.hasPower(ArtifactPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, mo, new ArtifactPower(mo, ARTIFACT)));
                }
            }
        }
        if (this.weather == Tenshi.WEATHER_3) {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.XLONG, false);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VulnerablePower(AbstractDungeon.player, VULNERABLE, true), VULNERABLE));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, mo, new VulnerablePower(mo, VULNERABLE, true), VULNERABLE));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (weather == Tenshi.WEATHER_1) {
            this.name = DESCRIPTIONS[0];
            description = DESCRIPTIONS[6];
            for (int i = 0; i < ENERGY; i++) {
                description += " [E]";
            }
            description += DESCRIPTIONS[7] + draw;
            if (draw == 1) {
                description += DESCRIPTIONS[8];
            } else {
                description += DESCRIPTIONS[9];
            }
            description += DESCRIPTIONS[10];
            if (amount <= 0) {
                description += DESCRIPTIONS[5];
            } else {
                description += DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
            }
        }
        if (weather == Tenshi.WEATHER_2) {
            this.name = DESCRIPTIONS[1];
            description = DESCRIPTIONS[11] + ARTIFACT + DESCRIPTIONS[12];
            if (amount <= 0) {
                description += DESCRIPTIONS[5];
            } else {
                description += DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
            }
        }
        if (weather == Tenshi.WEATHER_3) {
            this.name = DESCRIPTIONS[2];
            description = DESCRIPTIONS[13] + VULNERABLE + DESCRIPTIONS[14];
        }
    }
}
