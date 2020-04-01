package Gensokyo.events.act2;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.cards.blue.ReinforcedBody;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.green.Dash;
import com.megacrit.cardcrawl.cards.red.Carnage;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Gensokyo.GensokyoMod.makeEventPath;

public class ChildOfMiare extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("ChildOfMiare");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Akyuu.png");

    private int screenNum = 0;
    private boolean pickCard = false;
    private ArrayList<AbstractCard> cards;
    private int takenCardIndex = -1;
    private CardGroup initialDeck;

    private static final int NUM_CARDS = 3;

    public ChildOfMiare() {
        super(NAME, DESCRIPTIONS[0], IMG);
        initialDeck = new CardGroup(AbstractDungeon.player.masterDeck, CardGroup.CardGroupType.UNSPECIFIED);
        this.cards = getSavedItem();
        imageEventText.setDialogOption(OPTIONS[6]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                screenNum = 1;
                this.imageEventText.clearAllDialogs();
                for (AbstractCard card : cards) {
                    imageEventText.setDialogOption(OPTIONS[0] + FontHelper.colorString(card.name, "g") + OPTIONS[1], card);
                }
                imageEventText.setDialogOption(OPTIONS[2]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        takeCard(buttonPressed);
                        break;
                    case 1:
                        takeCard(buttonPressed);
                        break;
                    case 2:
                        takeCard(buttonPressed);
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 2:
                this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                screenNum = 3;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                AbstractDungeon.gridSelectScreen.open(initialDeck, 1, OPTIONS[5], false);
                pickCard = true;
                break;
            case 3:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void takeCard(int card) {
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
        this.imageEventText.clearRemainingOptions();
        takenCardIndex = card;
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(cards.get(card), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }

    private static String getSavePath() {
        return ConfigUtils.CONFIG_DIR + File.separator + "Gensokyo" + File.separator + "ChildOfMiare" + ".autosave" + (Settings.isBeta ? "BETA" : "");
    }

    public static ArrayList<AbstractCard> getSavedItem() {
        ArrayList<AbstractCard> result = new ArrayList<>();
        result.add(CardLibrary.getCopy(Carnage.ID));
        result.add(CardLibrary.getCopy(Dash.ID));
        result.add(CardLibrary.getCopy(ReinforcedBody.ID));
        try {
            if (Gdx.files.absolute(getSavePath()).exists()) {
                Gson gson = new Gson();
                String savestr = loadSaveString(getSavePath());
                Save save = gson.fromJson(savestr, Save.class);
                try {
                    if (save.childOfMiare.size() > 0) {
                        try {
                            result.set(0, CardLibrary.getCopy(save.childOfMiare.get(0).id, save.childOfMiare.get(0).upgrades, save.childOfMiare.get(0).misc));
                        } catch (Exception e) {
                            result.set(0, CardLibrary.getCopy(Madness.ID));
                        }
                    }
                    if (save.childOfMiare.size() > 1) {
                        try {
                            result.set(1, CardLibrary.getCopy(save.childOfMiare.get(1).id, save.childOfMiare.get(1).upgrades, save.childOfMiare.get(1).misc));
                        } catch (Exception e) {
                            result.set(1, CardLibrary.getCopy(Madness.ID));
                        }
                    }
                    if (save.childOfMiare.size() > 2) {
                        try {
                            result.set(2, CardLibrary.getCopy(save.childOfMiare.get(2).id, save.childOfMiare.get(2).upgrades, save.childOfMiare.get(2).misc));
                        } catch (Exception e) {
                            result.set(2, CardLibrary.getCopy(Madness.ID));
                        }
                    }
                    return result;
                } catch (Exception e) {
                    return result;
                }
            }
        } catch (JsonSyntaxException e) {
            deleteSave();
            return result;
        }

        return result;
    }

    public static void deleteSave() {
        Gdx.files.absolute(getSavePath()).delete();
    }

    private static String loadSaveString(String filePath) {
        FileHandle file = Gdx.files.absolute(filePath);
        String data = file.readString();
        if (SaveFileObfuscator.isObfuscated(data)) {
            return SaveFileObfuscator.decode(data, "key");
        }
        return data;
    }

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy();
            Save save = null;
            if (Gdx.files.absolute(getSavePath()).exists()) {
                Gson gson = new Gson();
                String savestr = loadSaveString(getSavePath());
                save = gson.fromJson(savestr, Save.class);
            }
            HashMap<Object, Object> params = new HashMap<>();
            List<CardSave> cardSaves;
            if (save != null) {
                cardSaves = save.childOfMiare;
                if (takenCardIndex >= 0 && cardSaves.size() == NUM_CARDS) {
                    cardSaves.remove(takenCardIndex); //Removes previously stored cards after player picks them
                }
                if (cardSaves.size() >= NUM_CARDS) {
                    cardSaves.remove(0);
                }
            } else {
                cardSaves = new ArrayList<>();
            }
            cardSaves.add(new CardSave(card.cardID, card.timesUpgraded, card.misc));
            params.put("childOfMiare", cardSaves);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = gson.toJson(params);
            try {
                Files.write(Paths.get(getSavePath()), data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private class Save {
        public ArrayList<CardSave> childOfMiare;

        public Save(ArrayList<CardSave> cardSave)
        {
            childOfMiare = cardSave;
        }
    }

}
