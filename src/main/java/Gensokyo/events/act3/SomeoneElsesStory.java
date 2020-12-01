package Gensokyo.events.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.Lunar.BrilliantDragonBullet;
import Gensokyo.cards.Lunar.Dawn;
import Gensokyo.cards.Lunar.DreamlikeParadise;
import Gensokyo.cards.Lunar.EverlastingLife;
import Gensokyo.cards.Lunar.HouraiInAPot;
import Gensokyo.cards.Lunar.LifeSpringInfinity;
import Gensokyo.cards.Lunar.MorningMist;
import Gensokyo.cards.Lunar.MorningStar;
import Gensokyo.cards.Lunar.NewMoon;
import Gensokyo.cards.Lunar.RainbowDanmaku;
import Gensokyo.cards.Lunar.RisingWorld;
import Gensokyo.cards.Lunar.SalamanderShield;
import Gensokyo.cards.Lunar.UnhurriedMind;
import Gensokyo.cards.Lunar.UnlikelyAid;
import Gensokyo.monsters.act3.Mokou;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;
import static Gensokyo.monsters.act3.Mokou.KAGUYA_HP;

public class SomeoneElsesStory extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("SomeoneElsesStory");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Festival.png");

    private int screenNum = 0;
    private int playerOriginalCurrHP;
    private int playerOriginalMaxHP;
    private boolean winTriggered = false;

    public SomeoneElsesStory() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    public void mokouWins() {
        if (!winTriggered) {
            winTriggered = true;
            restoreHPAndOtherStuff();
            enterImageFromCombat();
            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
            screenNum = 1;
            this.imageEventText.updateDialogOption(0, OPTIONS[2]);
            this.imageEventText.clearRemainingOptions();
        }
    }

    public void kaguyaWins(boolean upgradeReward) {
        if (!winTriggered) {
            winTriggered = true;
            restoreHPAndOtherStuff();
            enterImageFromCombat();
            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
            screenNum = 1;
            this.imageEventText.updateDialogOption(0, OPTIONS[2]);
            this.imageEventText.clearRemainingOptions();

            RewardItem reward = new RewardItem();
            reward.cards = getLunarCardReward(true);
            if (upgradeReward) {
                for (AbstractCard card : reward.cards) {
                    card.upgrade();
                }
            }
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.getCurrRoom().addCardReward(reward);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.combatRewardScreen.open();
        }
    }

    private void restoreHPAndOtherStuff() {
        AbstractDungeon.player.currentHealth = playerOriginalCurrHP;
        AbstractDungeon.player.maxHealth = playerOriginalMaxHP;
        AbstractDungeon.actionManager.clear();
        AbstractDungeon.overlayMenu.hideCombatPanels();
    }

    public static ArrayList<AbstractCard> getLunarCardReward(boolean rareOnly) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        if (!rareOnly) {
            cards.add(new BrilliantDragonBullet());
            cards.add(new SalamanderShield());
            cards.add(new EverlastingLife());
            cards.add(new MorningMist());
            cards.add(new NewMoon());

            for (AbstractCard rare : getAllLunarRares()) {
                if (AbstractDungeon.rollRareOrUncommon(0.2f) == AbstractCard.CardRarity.RARE) {
                    cards.add(rare);
                }
            }

        } else {
            cards = getAllLunarRares();
        }

        Collections.shuffle(cards, AbstractDungeon.cardRandomRng.random);
        return new ArrayList<>(cards.subList(0, 3));
    }

    public static ArrayList<AbstractCard> getAllLunarRares() {
        ArrayList<AbstractCard> cards = new ArrayList<>();

        cards.add(new DreamlikeParadise());
        cards.add(new LifeSpringInfinity());
        cards.add(new RainbowDanmaku());
        cards.add(new UnlikelyAid());
        cards.add(new MorningStar());
        cards.add(new RisingWorld());
        cards.add(new HouraiInAPot());
        cards.add(new Dawn());
        cards.add(new UnhurriedMind());

        return cards;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Listen closely
                        playerOriginalCurrHP = AbstractDungeon.player.currentHealth;
                        playerOriginalMaxHP = AbstractDungeon.player.maxHealth;
                        AbstractDungeon.player.currentHealth = KAGUYA_HP;
                        AbstractDungeon.player.maxHealth = KAGUYA_HP;

                        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new Mokou(0.0F, 0.0F));
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        AbstractDungeon.scene.nextRoom(AbstractDungeon.getCurrRoom()); //switches bg
                        this.enterCombatFromImage();
                        break;
                    case 1: // Listen briefly
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();

                        RewardItem reward = new RewardItem();
                        reward.cards = getLunarCardReward(false);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addCardReward(reward);
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }
}
