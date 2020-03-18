package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Gensokyo.GensokyoMod.makeEventPath;

public class FiresOfInvention extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("FiresOfInvention");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Nitori.png");

    private static final int COMMON_GOLD = 250;
    private static final int UNCOMMON_GOLD = 325;
    private static final int RARE_GOLD = 400;

    private static final int COMMON_REMOVAL = 2;
    private static final int UNCOMMON_REMOVAL = 2;
    private static final int RARE_REMOVAL = 3;

    private int gold;
    private int num_removal;
    private AbstractRelic relicToLose;
    private boolean hasEnoughCards;

    private int screenNum = 0;

    public FiresOfInvention() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                screenNum = 1;
                ArrayList<AbstractRelic> relics = new ArrayList<>();
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic.tier == AbstractRelic.RelicTier.COMMON || relic.tier == AbstractRelic.RelicTier.UNCOMMON || relic.tier == AbstractRelic.RelicTier.RARE) {
                        relics.add(relic);
                    }
                }
                Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
                if (!relics.isEmpty()) {
                    relicToLose = relics.get(0);
                    if (relicToLose.tier == AbstractRelic.RelicTier.COMMON) {
                        gold = COMMON_GOLD;
                        num_removal = COMMON_REMOVAL;
                    } else if (relicToLose.tier == AbstractRelic.RelicTier.UNCOMMON) {
                        gold = UNCOMMON_GOLD;
                        num_removal = UNCOMMON_REMOVAL;
                    } else {
                        gold = RARE_GOLD;
                        num_removal = RARE_REMOVAL;
                    }
                    if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() >= num_removal) {
                        hasEnoughCards = true;
                    }
                    this.imageEventText.setDialogOption(OPTIONS[1] + OPTIONS[3] + FontHelper.colorString(relicToLose.name, "r") + OPTIONS[4] + OPTIONS[5] + gold + OPTIONS[6]);
                    if (hasEnoughCards) {
                        this.imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[3] + FontHelper.colorString(relicToLose.name, "r") + OPTIONS[4] + OPTIONS[7] + num_removal + OPTIONS[8]);
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[14] + num_removal + OPTIONS[15], true);
                    }
                } else {
                    this.imageEventText.setDialogOption(OPTIONS[11], true);
                    this.imageEventText.setDialogOption(OPTIONS[11], true);
                }
                this.imageEventText.setDialogOption(OPTIONS[9]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        screenNum = 2;
                        AbstractDungeon.effectList.add(new RainingGoldEffect(gold));
                        AbstractDungeon.player.gainGold(gold);
                        AbstractDungeon.player.loseRelic(this.relicToLose.relicId);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        screenNum = 2;
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), num_removal, OPTIONS[12] + num_removal + OPTIONS[13], false);
                        AbstractDungeon.player.loseRelic(this.relicToLose.relicId);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        screenNum = 2;
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

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
                AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
            }
        }

    }

}
