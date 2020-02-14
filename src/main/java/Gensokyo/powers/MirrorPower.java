package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.bossRush.Mirror;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class MirrorPower extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("MirrorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Impartial84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Impartial32.png"));

    private Mirror mirror;
    private int attackCount = 0;
    private int skillCount = 0;
    private int powerCount = 0;

    public MirrorPower(AbstractCreature owner, Mirror mirror) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.mirror = mirror;

        type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            attackCount++;
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            skillCount++;
        }
        if (card.type == AbstractCard.CardType.POWER) {
            powerCount++;
        }
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (powerCount > 0) {
            mirror.setMoveShortcut(Mirror.JUSTICE);
        } else if (skillCount > attackCount) {
            mirror.setMoveShortcut(Mirror.INNOCENCE);
        } else {
            mirror.setMoveShortcut(Mirror.GUILT);
        }
        attackCount = 0;
        skillCount = 0;
        powerCount = 0;
        mirror.createIntent();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
        description += DESCRIPTIONS[1];
        if (attackCount == 1) {
            description += attackCount + DESCRIPTIONS[2];
        } else {
            description += attackCount + DESCRIPTIONS[3];
        }
        if (skillCount == 1) {
            description += skillCount + DESCRIPTIONS[4];
        } else {
            description += skillCount + DESCRIPTIONS[5];
        }
        if (powerCount == 1) {
            description += powerCount + DESCRIPTIONS[6];
        } else {
            description += powerCount + DESCRIPTIONS[7];
        }
    }
}
