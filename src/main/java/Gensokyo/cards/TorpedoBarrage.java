//package Gensokyo.cards;
//
//import Gensokyo.GensokyoMod;
//import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
//import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.cards.CardQueueItem;
//import com.megacrit.cardcrawl.cards.status.Burn;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import static Gensokyo.GensokyoMod.makeCardPath;
//
//public class TorpedoBarrage extends AbstractDefaultCard {
//
//    public static final String ID = GensokyoMod.makeID(TorpedoBarrage.class.getSimpleName());
//    public static final String IMG = makeCardPath("TorpedoBarrage.png");
//
//    private static final CardRarity RARITY = CardRarity.COMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.STATUS;
//    public static final CardColor COLOR = CardColor.COLORLESS;
//
//    private static final int COST = -2;
//    private static final int STATUS_AMOUNT = 3;
//    private static final AbstractCard STATUS = new OxygenTorpedo();
//
//    public TorpedoBarrage() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        magicNumber = baseMagicNumber = STATUS_AMOUNT;
//        exhaust = true;
//        this.cardsToPreview = STATUS;
//    }
//
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (this.dontTriggerOnUseCard) { AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(STATUS, magicNumber, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F)); }
//    }
//
//    @Override
//    public void upgrade() {
//    }
//
//    public void triggerOnEndOfTurnForPlayingCard() {
//        this.dontTriggerOnUseCard = true;
//        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
//    }
//}