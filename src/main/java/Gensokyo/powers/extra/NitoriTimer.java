//package Gensokyo.powers.extra;
//
//import Gensokyo.GensokyoMod;
//import Gensokyo.util.TextureLoader;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.localization.PowerStrings;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//
//public class NitoriTimer extends AbstractPower {
//
//    public static final String POWER_ID = GensokyoMod.makeID("NitoriTimer");
//    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
//    public static final String NAME = powerStrings.NAME;
//    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
//
//    private static final int LOSE_ENERGY_AMOUNT = 1;
//    public NitoriTimer(AbstractCreature owner, int amount) {
//        name = NAME;
//        ID = POWER_ID;
//        this.amount = amount;
//        this.owner = owner;
//
//        type = PowerType.BUFF;
//        isTurnBased = false;
//
//        loadRegion("time");
//        updateDescription();
//    }
//
//    @Override
//    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
//        if(!isPlayer){
//            if(this.amount - 1 <= 0 && this.amount != -1){ this.amount = -1; }
//            else { this.amount--; }
//            updateDescription();
//        }
//    }
//
//    @Override
//    public void updateDescription() { description = this.amount != -1 ? String.format(DESCRIPTIONS[0], this.amount, LOSE_ENERGY_AMOUNT) : DESCRIPTIONS[1]; }
//}
