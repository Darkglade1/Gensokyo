package Gensokyo.relics.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class TheCrow extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("TheCrow");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Crow.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Crow.png"));

    private static final int RITUAL = 1;

    public TheCrow() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onPlayerEndTurn() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RitualPower(AbstractDungeon.player, RITUAL, true), RITUAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower power = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
                if (power != null) {
                    int damage = power.amount;
                    addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), AttackEffect.POISON));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + RITUAL + DESCRIPTIONS[1];
    }

}
