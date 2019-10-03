package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class Justice extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("Justice");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Justice.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Justice.png"));

    private boolean dealDamage = false;
    private static int DAMAGE = 7;

    public Justice() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atPreBattle() {
        this.flash();
        this.dealDamage = true;
        if (!this.pulse) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.beginPulse();
        this.pulse = true;
        if (this.dealDamage) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        this.dealDamage = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.dealDamage = false;
            this.pulse = false;
        }
    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE + DESCRIPTIONS[1];
    }

}
