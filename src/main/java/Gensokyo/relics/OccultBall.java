package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.FinalOccultBallAction;
import Gensokyo.actions.FlexibleDiscoveryAction;
import Gensokyo.cards.Apocalypse;
import Gensokyo.cards.EightFeetTall;
import Gensokyo.cards.GapWoman;
import Gensokyo.cards.HAARP;
import Gensokyo.cards.Kunekune;
import Gensokyo.cards.LittleGreenMen;
import Gensokyo.cards.LochNessMonster;
import Gensokyo.cards.ManorOfTheDishes;
import Gensokyo.cards.MenInBlack;
import Gensokyo.cards.MissMary;
import Gensokyo.cards.MonkeysPaw;
import Gensokyo.cards.RedCapeBlueCape;
import Gensokyo.cards.SevenSchoolMysteries;
import Gensokyo.cards.SlitMouthedWoman;
import Gensokyo.cards.SpontaneousHumanCombustion;
import Gensokyo.cards.TekeTeke;
import Gensokyo.cards.TurboGranny;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class OccultBall extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("OccultBall");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OccultBall.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("OccultBall.png"));

    public static final int MAX_STACKS = 7;
    public static final int SPECIAL_STACKS = 8;
    private static final int NUM_CARDS = 3;

    public OccultBall() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
    }

    @Override
    public void atBattleStartPreDraw() {
        if (counter == SPECIAL_STACKS) {
            AbstractDungeon.actionManager.addToBottom(new FinalOccultBallAction(NUM_CARDS));
        } else {
            ArrayList<AbstractCard> chosenCards = return3TrulyRandomUrbanLegendInCombat(AbstractDungeon.cardRandomRng);
            if (counter == MAX_STACKS) {
                for (int i = 0; i < chosenCards.size(); i++) {
                    AbstractCard card = chosenCards.get(i);
                    card.upgrade();
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, false));
                }
            } else if (counter == 1) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(chosenCards.get(0).makeCopy(), false));
            } else if (counter >= 2) {
                int upgrades = counter - 3;
                if (upgrades > 0) {
                    for (int i = 0; i < upgrades; i++) {
                        chosenCards.get(i).upgrade();
                    }
                }
                if (counter == 2) {
                    chosenCards.remove(0);
                }
                AbstractDungeon.actionManager.addToBottom(new FlexibleDiscoveryAction(chosenCards));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public static ArrayList<AbstractCard> getAllUrbanLegends() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new MissMary());
        list.add(new SpontaneousHumanCombustion());
        list.add(new RedCapeBlueCape());
        list.add(new TekeTeke());
        list.add(new Kunekune());
        list.add(new HAARP());
        list.add(new MenInBlack());
        list.add(new LittleGreenMen());
        list.add(new TurboGranny());
        list.add(new MonkeysPaw());
        list.add(new ManorOfTheDishes());
        list.add(new LochNessMonster());
        list.add(new GapWoman());
        list.add(new EightFeetTall());
        list.add(new SevenSchoolMysteries());
        list.add(new Apocalypse());
        list.add(new SlitMouthedWoman());
        return list;
    }

    public static ArrayList<AbstractCard> return3TrulyRandomUrbanLegendInCombat(Random rng) {
        ArrayList<AbstractCard> list = getAllUrbanLegends();
        Collections.shuffle(list, rng.random);
        return new ArrayList<>(list.subList(0, 3));
    }

    public void increment() {
        if (this.counter < 0) {
            this.counter = 0;
        }
        this.counter++;
        if (this.counter > SPECIAL_STACKS) {
            this.counter = SPECIAL_STACKS;
        }
        fixDescription();
    }

    @Override
    public void onVictory() {
        if (this.counter < MAX_STACKS) {
            int roll = AbstractDungeon.relicRng.random(0, 99);
            if (roll < 15) {
                AbstractDungeon.getCurrRoom().addRelicToRewards(new OccultBall());
            }
        }
    }

    @Override
    public void instantObtain()
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            OccultBall occultBall = (OccultBall) AbstractDungeon.player.getRelic(ID);
            occultBall.increment();
            occultBall.flash();
        } else {
            super.instantObtain();
        }
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            OccultBall occultBall = (OccultBall) AbstractDungeon.player.getRelic(ID);
            occultBall.increment();
            occultBall.flash();

            isDone = true;
            isObtained = true;
            discarded = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            OccultBall occultBall = (OccultBall) AbstractDungeon.player.getRelic(ID);
            occultBall.increment();
            occultBall.flash();
        } else {
            super.obtain();
        }
    }

    private void fixDescription() {
        description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        fixDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (counter <= 1) {
            return DESCRIPTIONS[0];
        } else if (counter == 2) {
            return DESCRIPTIONS[1];
        } else if (counter < MAX_STACKS) {
            return DESCRIPTIONS[2];
        } else if (counter == MAX_STACKS){
            return DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[4];
        }
    }
}
