package Gensokyo.CardMods;

import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class FleetiveModifier extends AbstractCardModifier {

    private int explodeTime;
    private int baseExplodeTime;

    public FleetiveModifier(int timer) {
        this.explodeTime = timer;
        this.baseExplodeTime = timer;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL Fleetive " + explodeTime + ".";
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        explodeTime--;
        if (explodeTime == 0) {
            //viasual effect!! :) make the card go BOOM wqith a bunch of exploding exploder effects centered on it...
            for (int i = 0; i < 10; i++) {
                AbstractDungeon.effectList.add(new ExplosionSmallEffect(AbstractDungeon.cardRandomRng.random(card.current_x - 20, card.current_x + 20), AbstractDungeon.cardRandomRng.random(card.current_y - 20, card.current_y + 20)));
            }
            //could probably be tuned in better
            if (StSLib.getMasterDeckEquivalent(card) != null) {
                AbstractDungeon.player.masterDeck.removeCard(StSLib.getMasterDeckEquivalent(card));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FleetiveModifier(baseExplodeTime);
    }
}