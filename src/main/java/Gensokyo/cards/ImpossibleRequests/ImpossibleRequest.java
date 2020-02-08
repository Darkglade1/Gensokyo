package Gensokyo.cards.ImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class ImpossibleRequest extends AbstractDefaultCard {

    public AbstractDefaultCard buddhaStoneBowel = new BuddhaStoneBowel();
    public AbstractDefaultCard bulletBranchOfHourai = new BulletBranchOfHourai();
    public AbstractDefaultCard fireRatsRobe = new FireRatsRobe();
    public AbstractDefaultCard jewelFromDragon = new JewelFromTheDragonsNeck();
    public AbstractDefaultCard swallowsCowrieShell = new SwallowsCowrieShell();
    public AbstractDefaultCard cardToTransform;

    public AbstractDefaultCard cardToPreviewBuddaStoneBowel;
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

    public static final String ID = GensokyoMod.makeID(ImpossibleRequest.class.getSimpleName());
    public static final String IMG = makeCardPath("CrescentMoonSlash.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;
    private static final int COST = -2;


    public ImpossibleRequest() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    public void transform() {
        if (requestCounter == BUDDHA_BOWL) {
            cardToTransform = buddhaStoneBowel;
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
        }
        System.out.println("Request is now: " + requestCounter);
        if (cardToTransform != null) {
            System.out.println("Not null");
            this.rawDescription = languagePack.getCardStrings(cardToTransform.cardID).DESCRIPTION;
            System.out.println(rawDescription);
            if (this.upgraded) {
                cardToTransform.upgrade();
                //Sets the card description to the upgraded version, if it exists.
                if (languagePack.getCardStrings(cardToTransform.cardID).UPGRADE_DESCRIPTION != null) {
                    this.rawDescription = languagePack.getCardStrings(cardToTransform.cardID).UPGRADE_DESCRIPTION;
                }
            }

            this.name = cardToTransform.name;
            this.target = cardToTransform.target;
            this.cost = cardToTransform.cost;
            this.costForTurn = cardToTransform.costForTurn;
            this.isCostModified = false;
            this.isCostModifiedForTurn = false;
            this.energyOnUse = cardToTransform.energyOnUse;
            this.freeToPlayOnce = cardToTransform.freeToPlayOnce;
            this.exhaust = cardToTransform.exhaust;
            this.retain = cardToTransform.retain;
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
        if (cardToTransform != null) {
            return super.canUse(p, m);
        }
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
            transform();
        }
    }

    @Override
    public void hover() {
        try {
            //Sets up these variables to indicate that a preview should be shown
            cardToPreviewBuddaStoneBowel = buddhaStoneBowel;
            cardToPreviewBulletBranch = bulletBranchOfHourai;
            cardToPreviewFireRat = fireRatsRobe;
            cardToPreviewJewelFromDragon = jewelFromDragon;
            cardToPreviewSwallowShell = swallowsCowrieShell;
            if (upgraded) {
                cardToPreviewBuddaStoneBowel.upgrade();
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
        cardToPreviewBuddaStoneBowel = null;
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

        if (cardToPreviewBuddaStoneBowel != null) {
            AbstractCard card = cardToPreviewBuddaStoneBowel.makeStatEquivalentCopy();
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
    public void triggerOnExhaust() {
        this.addToBot(new MakeTempCardInHandAction(this.makeCopy()));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        ImpossibleRequest request = new ImpossibleRequest();
        request.requestCounter = this.requestCounter;
        request.transform();
        return request;
    }

    @Override
    public AbstractCard makeCopy() {
        ImpossibleRequest request = new ImpossibleRequest();
        request.requestCounter = this.requestCounter;
        request.transform();
        return request;
    }
}