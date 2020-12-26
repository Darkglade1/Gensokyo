package Gensokyo.cards.ImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import Gensokyo.powers.act2.LunaticPrincess;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class ImpossibleRequest extends AbstractDefaultCard {

    public AbstractDefaultCard buddhaStoneBowl = new BuddhaStoneBowl();
    public AbstractDefaultCard bulletBranchOfHourai = new BulletBranchOfHourai();
    public AbstractDefaultCard fireRatsRobe = new FireRatsRobe();
    public AbstractDefaultCard jewelFromDragon = new JewelFromTheDragonsNeck();
    public AbstractDefaultCard swallowsCowrieShell = new SwallowsCowrieShell();
    public AbstractDefaultCard cardToTransform;

    public AbstractDefaultCard cardToPreviewBuddaStoneBowl;
    public AbstractDefaultCard cardToPreviewBulletBranch;
    public AbstractDefaultCard cardToPreviewFireRat;
    public AbstractDefaultCard cardToPreviewJewelFromDragon;
    public AbstractDefaultCard cardToPreviewSwallowShell;

    public static final int BUDDHA_BOWL = 0;
    public static final int BULLET_BRANCH = 1;
    public static final int FIRE_RAT = 2;
    public static final int JEWEL_FROM_DRAGON = 3;
    public static final int SWALLOW_SHELL = 4;
    public int requestCounter = 0;
    public boolean completed = false;

    public static final String ID = GensokyoMod.makeID(ImpossibleRequest.class.getSimpleName());
    public static final String IMG = makeCardPath("BuddhaStoneBowl.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;
    private static final int COST = -2;


    public ImpossibleRequest() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.selfRetain = true;
    }

    public void transform() {
        if (requestCounter == BUDDHA_BOWL) {
            cardToTransform = buddhaStoneBowl;
        }
        else if (requestCounter == BULLET_BRANCH) {
            cardToTransform = bulletBranchOfHourai;
        }
        else if (requestCounter == FIRE_RAT) {
            cardToTransform = fireRatsRobe;
        }
        else if (requestCounter == JEWEL_FROM_DRAGON) {
            cardToTransform = jewelFromDragon;
        }
        else if (requestCounter == SWALLOW_SHELL){
            cardToTransform = swallowsCowrieShell;
        } else {
            CardGroup group;
            if (AbstractDungeon.player.hand.contains(this)) {
                group = AbstractDungeon.player.hand;
            }
            else if (AbstractDungeon.player.drawPile.contains(this)) {
                group = AbstractDungeon.player.drawPile;
            }
            else {
                group = AbstractDungeon.player.discardPile;
            }
            this.name = languagePack.getCardStrings(ID).NAME;
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, group));
        }
        if (cardToTransform != null) {
            this.rawDescription = languagePack.getCardStrings(cardToTransform.cardID).DESCRIPTION;

            this.name = cardToTransform.name;
            this.target = cardToTransform.target;
            this.cost = cardToTransform.cost;
            this.costForTurn = cardToTransform.costForTurn;
            this.isCostModified = false;
            this.isCostModifiedForTurn = false;
            this.energyOnUse = cardToTransform.energyOnUse;
            this.freeToPlayOnce = cardToTransform.freeToPlayOnce;
            this.exhaust = cardToTransform.exhaust;
            this.purgeOnUse = cardToTransform.purgeOnUse;
            this.baseDamage = cardToTransform.baseDamage;
            this.baseBlock = cardToTransform.baseBlock;
            this.baseDraw = cardToTransform.baseDraw;
            this.baseMagicNumber = cardToTransform.baseMagicNumber;
            this.defaultBaseSecondMagicNumber = cardToTransform.defaultBaseSecondMagicNumber;
            this.baseHeal = cardToTransform.baseHeal;
            this.baseDiscard = cardToTransform.baseDiscard;
            this.isLocked = cardToTransform.isLocked;
            this.misc = cardToTransform.misc;
            loadCardImage(cardToTransform.textureImg);

            initializeDescription();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (cardToTransform != null) {
            cardToTransform.calculateCardDamage(mo);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (cardToTransform != null) {
            cardToTransform.applyPowers();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        cardToTransform.use(p, m);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            buddhaStoneBowl.upgrade();
            bulletBranchOfHourai.upgrade();
            fireRatsRobe.upgrade();
            jewelFromDragon.upgrade();
            swallowsCowrieShell.upgrade();
        }
    }

    @Override
    public void hover() {
        try {
            //Sets up these variables to indicate that a preview should be shown
            cardToPreviewBuddaStoneBowl = buddhaStoneBowl;
            cardToPreviewBulletBranch = bulletBranchOfHourai;
            cardToPreviewFireRat = fireRatsRobe;
            cardToPreviewJewelFromDragon = jewelFromDragon;
            cardToPreviewSwallowShell = swallowsCowrieShell;
            if (upgraded) {
                cardToPreviewBuddaStoneBowl.upgrade();
                cardToPreviewBulletBranch.upgrade();
                cardToPreviewFireRat.upgrade();
                cardToPreviewJewelFromDragon.upgrade();
                cardToPreviewSwallowShell.upgrade();
            }
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
        super.hover();
    }

    @Override
    public void unhover() {
        super.unhover();
        //remove the preview when the user stops hovering over the card
        cardToPreviewBuddaStoneBowl = null;
        cardToPreviewBulletBranch = null;
        cardToPreviewFireRat = null;
        cardToPreviewJewelFromDragon = null;
        cardToPreviewSwallowShell = null;
    }

    //Piggybacks off the keyword tooltip popup to also show the previews
    @Override
    public void renderCardTip(SpriteBatch sb) {
        super.renderCardTip(sb);

        //Removes the preview when the player is manipulating the card
        if (AbstractDungeon.player != null && (AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode)) {
            return;
        }

        float drawScale = 0.5f;
        float yPosition1 = this.current_y + this.hb.height * 0.75f;
        float yPosition2 = this.current_y + this.hb.height * 0.25f;
        float yPosition3 = this.current_y - this.hb.height * 0.25f;

        float xPosition1;
        float xPosition2;
        float xPosition3;
        float xOffset1 = -this.hb.width * 0.75f;
        float xOffset2 = -this.hb.width * 0.25f;
        float xOffset3 = this.hb.width * 0.25f;

        //inverts the x position if the card is a certain amount to the right to prevent clipping issues
        if (this.current_x > Settings.WIDTH * 0.75F) {
            xOffset1 = -xOffset1;
            xOffset2 = -xOffset2;
            xOffset3 = -xOffset3;
        }

        xPosition1 = this.current_x + xOffset1;
        xPosition2 = this.current_x + xOffset2;
        xPosition3 = this.current_x + xOffset3;

        if (cardToPreviewBuddaStoneBowl != null) {
            AbstractCard card = cardToPreviewBuddaStoneBowl.makeStatEquivalentCopy();
            if (card != null) {
                card.drawScale = drawScale;
                card.current_x = xPosition1;
                card.current_y = yPosition3;
                card.render(sb);
            }
        }
        if (cardToPreviewBulletBranch != null) {
            AbstractCard card = cardToPreviewBulletBranch.makeStatEquivalentCopy();
            if (card != null) {
                card.drawScale = drawScale;
                card.current_x = xPosition1;
                card.current_y = yPosition2;
                card.render(sb);
            }
        }
        if (cardToPreviewFireRat != null) {
            AbstractCard card = cardToPreviewFireRat.makeStatEquivalentCopy();
            if (card != null) {
                card.drawScale = drawScale;
                card.current_x = xPosition1;
                card.current_y = yPosition1;
                card.render(sb);
            }
        }
        if (cardToPreviewJewelFromDragon != null) {
            AbstractCard card = cardToPreviewJewelFromDragon.makeStatEquivalentCopy();
            if (card != null) {
                card.drawScale = drawScale;
                card.current_x = xPosition2;
                card.current_y = yPosition1;
                card.render(sb);
            }
        }
        if (cardToPreviewSwallowShell != null) {
            AbstractCard card = cardToPreviewSwallowShell.makeStatEquivalentCopy();
            if (card != null) {
                card.drawScale = drawScale;
                card.current_x = xPosition3;
                card.current_y = yPosition1;
                card.render(sb);
            }
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (cardToTransform != null) {
            cardToTransform.atTurnStart();
        }
    }

    @Override
    public float getTitleFontSize() {
        if (cardToTransform != null) {
            return cardToTransform.getTitleFontSize();
        } else {
            return super.getTitleFontSize();
        }
    }

    @Override
    public void triggerOnExhaust() {
        //Create a new Impossible Request and make it identical to the old one then give it to the player and remove the old one
        //Have to do it like this cause otherwise shit like Nagashi-bina Doll that exhausts at end of turn causes a visual glitch
        ImpossibleRequest oldRequest = this;
        if (this.requestCounter <= SWALLOW_SHELL) {
            this.addToBot(new MakeTempCardInHandAction(this));
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    ImpossibleRequest newRequest = null;
                    for (AbstractCard card : AbstractDungeon.player.hand.group) {
                        if (card instanceof ImpossibleRequest) {
                            newRequest = (ImpossibleRequest)card;
                        }
                    }
                    if (newRequest != null) {
                        newRequest.requestCounter = oldRequest.requestCounter;
                        newRequest.completed = oldRequest.completed;
                        newRequest.transform();
                        if (AbstractDungeon.player.hasPower(LunaticPrincess.POWER_ID)) {
                            LunaticPrincess power = (LunaticPrincess)AbstractDungeon.player.getPower(LunaticPrincess.POWER_ID);
                            power.pointToNewRequest(newRequest);
                        }
                    }
                    AbstractDungeon.player.exhaustPile.removeCard(oldRequest);
                    this.isDone = true;
                }
            });
        }
    }
}