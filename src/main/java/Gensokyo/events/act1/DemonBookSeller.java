package Gensokyo.events.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act1.Bombinomicon;
import Gensokyo.relics.act1.BookOfSpecters;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.relics.NilrysCodex;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;

public class DemonBookSeller extends AbstractImageEvent {


    public static final String ID = GensokyoMod.makeID("DemonBookSeller");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("DemonBookSeller.png");

    public static final int COST = 160;
    private static final int BOOKS_FOR_SALE = 2;
    private boolean hasEnoughMoney;
    ArrayList<AbstractRelic> bookList;

    private int screenNum = 0;


    public DemonBookSeller() {
        super(NAME, DESCRIPTIONS[0], IMG);
        AbstractPlayer p = AbstractDungeon.player;

        if (p.gold >= COST) {
            hasEnoughMoney = true;
        }

        bookList = setBooks();
        Collections.shuffle(bookList, AbstractDungeon.relicRng.random);

        if (this.hasEnoughMoney) {
            int booksOnSale = Math.min(bookList.size(), BOOKS_FOR_SALE);
            for (int i = 0; i < booksOnSale; i++) {
                this.imageEventText.setDialogOption(OPTIONS[0] + COST + OPTIONS[1] + FontHelper.colorString(bookList.get(i).name, "g") + ".", bookList.get(i));
            }
        } else {
            this.imageEventText.setDialogOption(OPTIONS[2] + COST + OPTIONS[3], true);
        }

        //Leave
        this.imageEventText.setDialogOption(OPTIONS[4]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Buy
                        if (bookList.size() == 0) {
                            Leave();
                        } else {
                            BuyBook(0);
                        }
                        break;
                    case 1: // Buy
                        if (bookList.size() == 1 || !hasEnoughMoney) {
                            Leave();
                        } else {
                            BuyBook(1);
                        }
                        break;
                    case 2: // Leave
                        Leave();
                        break;
                }
                break;
            case 1:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void BuyBook(int book) {
        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
        screenNum = 1;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        this.imageEventText.clearRemainingOptions();
        AbstractDungeon.player.loseGold(COST);
        AbstractRelic relic = bookList.get(book);
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
    }

    private void Leave() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        screenNum = 1;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        this.imageEventText.clearRemainingOptions();
    }

    private ArrayList<AbstractRelic> setBooks() {
        ArrayList<AbstractRelic> books = new ArrayList<>();
        if (!AbstractDungeon.player.hasRelic(Necronomicon.ID)) {
            books.add(new Necronomicon());
        }
        if (!AbstractDungeon.player.hasRelic(Enchiridion.ID)) {
            books.add(new Enchiridion());
        }
        if (!AbstractDungeon.player.hasRelic(NilrysCodex.ID)) {
            books.add(new NilrysCodex());
        }
        if (!AbstractDungeon.player.hasRelic(Bombinomicon.ID)) {
            books.add(new Bombinomicon());
        }
        if (!AbstractDungeon.player.hasRelic(BookOfSpecters.ID)) {
            books.add(new BookOfSpecters());
        }
        return books;
    }
}
