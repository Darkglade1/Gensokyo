//package Gensokyo.powers;
//
//import Gensokyo.GensokyoMod;
//import Gensokyo.util.TextureLoader;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
//import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
//import com.megacrit.cardcrawl.actions.common.GainBlockAction;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.localization.PowerStrings;
//import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
//
//import static Gensokyo.GensokyoMod.makePowerPath;
//
//
//public class Resolve extends TwoAmountPower implements OnLoseBlockPower {
//
//    public static final String POWER_ID = GensokyoMod.makeID("Resolve");
//    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
//    public static final String NAME = powerStrings.NAME;
//    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
//
//    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Impartial84.png"));
//    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Impartial32.png"));
//
//    public Resolve(AbstractCreature owner, int amount) {
//        name = NAME;
//        ID = POWER_ID;
//
//        this.owner = owner;
//        this.amount = amount;
//        this.amount2 = 0;
//
//        type = PowerType.BUFF;
//        isTurnBased = false;
//
//        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
//
//        updateDescription();
//    }
//
//    @Override
//    public void onInitialApplication() {
//        this.addToBot(new GainBlockAction(this.owner, this.amount));
//    }
//
//    @Override
//    public int onLoseBlock(DamageInfo info, int amount) {
//        int gain = amount;
//        if (gain > this.owner.currentBlock) {
//            gain = this.owner.currentBlock;
//        }
//        amount2 += gain;
//        updateDescription();
//        return amount;
//    }
//
//    @Override
//    public void atStartOfTurn() {
//        if (amount2 > 0) {
//            this.owner.currentHealth -= amount2;
//            AbstractDungeon.effectList.add(new StrikeEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, amount2));
//            this.owner.healthBarUpdatedEvent();
//            if (this.owner.currentHealth <= 0) {
//                this.owner.currentHealth = 0;
//                this.owner.damage(new DamageInfo(null, 0, DamageInfo.DamageType.NORMAL));
//            }
//        }
//        amount2 = 0;
//        updateDescription();
//    }
//
//    @Override
//    public void atEndOfTurn(boolean isPlayer) {
//        this.addToBot(new GainBlockAction(this.owner, this.amount));
//    }
//
//    @Override
//    public void updateDescription() {
//        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
//    }
//}
