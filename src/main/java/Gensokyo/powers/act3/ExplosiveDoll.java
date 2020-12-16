package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.act3.Shinki.Shinki;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static Gensokyo.GensokyoMod.makePowerPath;

public class ExplosiveDoll extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("ExplosiveDoll");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ExplosiveDoll(AbstractCreature owner, int turns, int damage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = damage;
        this.amount2 = turns;
        this.type = PowerType.BUFF;
        this.loadRegion("explosive");
        updateDescription();
    }

    @Override
    public void duringTurn() {
        if (this.amount2 == 1 && !this.owner.isDying) {
            this.addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
            this.addToBot(new SuicideAction((AbstractMonster)this.owner));
            DamageInfo damageInfo = new DamageInfo(this.owner, amount, DamageInfo.DamageType.THORNS);
            this.addToBot(new DamageAction(AbstractDungeon.player, damageInfo, AbstractGameAction.AttackEffect.FIRE, true));
        } else {
            amount2--;
            this.updateDescription();
        }

    }

    @Override
    public void updateDescription() {
        if (amount2 == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + amount + DESCRIPTIONS[5];
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + amount2 + DESCRIPTIONS[3] + DESCRIPTIONS[4] + amount + DESCRIPTIONS[5];
        }
    }
}
