package Gensokyo.relics.act1;

import Gensokyo.GensokyoMod;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class YoukaiFlower extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("YoukaiFlower");

    //private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("YoukaiFlower.png"));
   // private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("YoukaiFlower.png"));

    private static final int NUM_TURNS = 3;
    private static final int ENERGY_AMT = 2;

    public YoukaiFlower() {
        super(ID, "sunflower.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return AbstractDungeon.player != null ? this.setDescription(AbstractDungeon.player.chosenClass) : this.setDescription(null);
    }

    private String setDescription(AbstractPlayer.PlayerClass c) {
        return this.DESCRIPTIONS[0] + NUM_TURNS + this.DESCRIPTIONS[1];
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = this.setDescription(c);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            ++this.counter;
        }

        if (this.counter == NUM_TURNS) {
            this.counter = 0;
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_AMT));
        }
    }
}
