package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act2.DemonMask;
import Gensokyo.relics.act2.FoxMask;
import Gensokyo.relics.act2.LionMask;
import Gensokyo.relics.act2.MaskOfHope;
import Gensokyo.relics.act2.SpiderMask;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.relics.FaceOfCleric;
import com.megacrit.cardcrawl.relics.GremlinMask;
import com.megacrit.cardcrawl.relics.NlothsMask;
import com.megacrit.cardcrawl.relics.RedMask;
import com.megacrit.cardcrawl.relics.SsserpentHead;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;

public class NohDance extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("NohDance");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Kokoro.png");

    public static final int COST = 120;
    private static final int MASKS_FOR_SALE = 2;
    private boolean hasEnoughMoney;
    private boolean hasOtherMask;
    private AbstractRelic otherMask = null;
    ArrayList<AbstractRelic> maskList;

    private int screenNum = 0;


    public NohDance() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractPlayer p = AbstractDungeon.player;

        if (p.gold >= COST) {
            hasEnoughMoney = true;
        }
        hasOtherMask = hasOtherMask();
        maskList = setMasks();
        Collections.shuffle(maskList, AbstractDungeon.relicRng.random);

        this.imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.clearAllDialogs();
                if (this.hasOtherMask) {
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    int booksOnSale = Math.min(maskList.size(), MASKS_FOR_SALE);
                    for (int i = 0; i < booksOnSale; i++) {
                        this.imageEventText.setDialogOption(OPTIONS[6] + FontHelper.colorString(otherMask.name, "r") + OPTIONS[7] + FontHelper.colorString(maskList.get(i).name, "g") + ".", maskList.get(i));
                    }
                } else if (this.hasEnoughMoney) {
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    int booksOnSale = Math.min(maskList.size(), MASKS_FOR_SALE);
                    for (int i = 0; i < booksOnSale; i++) {
                        this.imageEventText.setDialogOption(OPTIONS[0] + COST + OPTIONS[1] + FontHelper.colorString(maskList.get(i).name, "g") + ".", maskList.get(i));
                    }
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[2] + COST + OPTIONS[3], true);
                }

                //Leave
                this.imageEventText.setDialogOption(OPTIONS[4]);
                screenNum = 1;
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Buy
                        if (maskList.size() == 0) {
                            Leave();
                        } else {
                            BuyMask(0);
                        }
                        break;
                    case 1: // Buy
                        if (maskList.size() == 1 || (!hasEnoughMoney && !hasOtherMask)) {
                            Leave();
                        } else {
                            BuyMask(1);
                        }
                        break;
                    case 2: // Leave
                        Leave();
                        break;
                }
                break;
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void BuyMask(int mask) {
        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        this.imageEventText.clearRemainingOptions();
        if (hasOtherMask) {
            AbstractDungeon.player.loseRelic(otherMask.relicId);
        } else {
            AbstractDungeon.player.loseGold(COST);
        }
        AbstractRelic relic = maskList.get(mask);
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
    }

    private void Leave() {
        if (this.hasOtherMask) {
            this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
        } else {
            this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
        }
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        this.imageEventText.clearRemainingOptions();
    }

    private ArrayList<AbstractRelic> setMasks() {
        ArrayList<AbstractRelic> masks = new ArrayList<>();
        if (!AbstractDungeon.player.hasRelic(FoxMask.ID)) {
            masks.add(new FoxMask());
        }
        if (!AbstractDungeon.player.hasRelic(SpiderMask.ID)) {
            masks.add(new SpiderMask());
        }
        if (!AbstractDungeon.player.hasRelic(MaskOfHope.ID)) {
            masks.add(new MaskOfHope());
        }
        if (!AbstractDungeon.player.hasRelic(DemonMask.ID)) {
            masks.add(new DemonMask());
        }
        if (!AbstractDungeon.player.hasRelic(LionMask.ID)) {
            masks.add(new LionMask());
        }
        return masks;
    }

    private boolean hasOtherMask() {
        ArrayList<AbstractRelic> otherMasks = new ArrayList<>();
        if (AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(CultistMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(FaceOfCleric.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(FaceOfCleric.ID));
        }
        if (AbstractDungeon.player.hasRelic(GremlinMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(GremlinMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(NlothsMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(NlothsMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(SsserpentHead.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(SsserpentHead.ID));
        }
        if (AbstractDungeon.player.hasRelic(RedMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(RedMask.ID));
        }
        if (!otherMasks.isEmpty()) {
            Collections.shuffle(otherMasks, AbstractDungeon.relicRng.random);
            otherMask = otherMasks.get(0);
            return true;
        }
        return false;
    }

    public static boolean staticHasOtherMask() {
        ArrayList<AbstractRelic> otherMasks = new ArrayList<>();
        if (AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(CultistMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(FaceOfCleric.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(FaceOfCleric.ID));
        }
        if (AbstractDungeon.player.hasRelic(GremlinMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(GremlinMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(NlothsMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(NlothsMask.ID));
        }
        if (AbstractDungeon.player.hasRelic(SsserpentHead.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(SsserpentHead.ID));
        }
        if (AbstractDungeon.player.hasRelic(RedMask.ID)) {
            otherMasks.add(AbstractDungeon.player.getRelic(RedMask.ID));
        }
        if (!otherMasks.isEmpty()) {
            return true;
        }
        return false;
    }
}
