package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static Gensokyo.GensokyoMod.makeEventPath;

public class PrincessOfTheMoon extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("PrincessOfTheMoon");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Toyohime.png");

    private static final int ATTACK_REMOVAL = 2;
    private static final int SKILL_REMOVAL = 2;

    private CardGroup removableAttacks = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private CardGroup removableSkills = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    private int attackRemoval = ATTACK_REMOVAL;
    private int skillRemoval = SKILL_REMOVAL;

    private int screenNum = 0;

    public PrincessOfTheMoon() {
        super(NAME, DESCRIPTIONS[0], IMG);
        CardGroup removableCards = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
        int attackCount = 0;
        int skillCount = 0;
        for (AbstractCard card : removableCards.group) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                attackCount++;
                removableAttacks.addToTop(card);
            }
            if (card.type == AbstractCard.CardType.SKILL) {
                skillCount++;
                removableSkills.addToTop(card);
            }
        }

        if (attackCount < ATTACK_REMOVAL) {
            attackRemoval = attackCount;
        }
        if (skillCount < SKILL_REMOVAL) {
            skillRemoval = skillCount;
        }

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                screenNum = 1;

                if (skillRemoval == 0) {
                    this.imageEventText.setDialogOption(OPTIONS[5], true);
                } else {
                    imageEventText.setDialogOption(OPTIONS[1] + SKILL_REMOVAL + OPTIONS[2]);
                }

                if (attackRemoval == 0) {
                    this.imageEventText.setDialogOption(OPTIONS[6], true);
                } else {
                    imageEventText.setDialogOption(OPTIONS[3] + ATTACK_REMOVAL + OPTIONS[4]);
                }

                imageEventText.setDialogOption(OPTIONS[8]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Remove Skills
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 2;
                        AbstractDungeon.gridSelectScreen.open(removableSkills, skillRemoval, OPTIONS[7], false);
                        break;
                    case 1: // Remove Attacks
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 2;
                        AbstractDungeon.gridSelectScreen.open(removableAttacks, attackRemoval, OPTIONS[7], false);
                        break;
                    case 2: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8]);
                        screenNum = 2;
                        break;
                }
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
