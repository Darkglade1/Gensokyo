package Gensokyo.events.act3;

import Gensokyo.CardMods.BlockMod;
import Gensokyo.CardMods.CostMod;
import Gensokyo.CardMods.DamageMod;
import Gensokyo.CardMods.DrawMod;
import Gensokyo.CardMods.InnateMod;
import Gensokyo.CardMods.PoisonMod;
import Gensokyo.CardMods.RetainMod;
import Gensokyo.CardMods.VulnerableMod;
import Gensokyo.CardMods.WeakMod;
import Gensokyo.GensokyoMod;
import Gensokyo.cards.CustomPotion;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Collections;

import static Gensokyo.GensokyoMod.makeEventPath;

public class MedicineSeller extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("MedicineSeller");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("MedicineSeller.png");

    public static final int INITIAL_GOLD_COST = 80;
    public static final int BONUS_GOLD_COST = 40;
    private static final int NUM_MODIFIERS = 3;

    private static final int BLOCK0 = 4;
    private static final int BLOCK1 = 8;
    private static final int BLOCK2 = 14;
    private int block;

    private static final int DAMAGE0 = 5;
    private static final int DAMAGE1 = 9;
    private static final int DAMAGE2 = 15;
    private int damage;

    private static final int DRAW0 = 1;
    private static final int DRAW1 = 2;
    private static final int DRAW2 = 3;
    private int draw;

    private static final int WEAK0 = 1;
    private static final int WEAK1 = 2;
    private static final int WEAK2 = 3;
    private int weak;

    private static final int VULNERABLE0 = 1;
    private static final int VULNERABLE1 = 2;
    private static final int VULNERABLE2 = 3;
    private int vulnerable;

    private static final int POISON0 = 3;
    private static final int POISON1 = 5;
    private static final int POISON2 = 8;
    private int poison;

    private ArrayList<AbstractCardModifier> possibleMods = new ArrayList<>();
    private ArrayList<AbstractCardModifier> chosenMods = new ArrayList<>();

    private AbstractCardModifier option1;
    private AbstractCardModifier option2;
    private AbstractCardModifier bonusMod;

    private AbstractCard potionPreview1;
    private AbstractCard potionPreview2;

    private AbstractCard currentPotion;

    private int screenNum = 0;

    public MedicineSeller() {
        super(NAME, DESCRIPTIONS[0], IMG);
        if (AbstractDungeon.player.gold >= INITIAL_GOLD_COST) {
            this.imageEventText.setDialogOption(OPTIONS[0] + INITIAL_GOLD_COST + OPTIONS[1] + OPTIONS[2]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[7], true);
        }
        this.imageEventText.setDialogOption(OPTIONS[6]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[8] + 0 + OPTIONS[4]);
                        this.imageEventText.setDialogOption(OPTIONS[8] + 1 + OPTIONS[4]);
                        this.imageEventText.setDialogOption(OPTIONS[8] + 2 + OPTIONS[4]);
                        screenNum = 1;
                        AbstractDungeon.player.loseGold(INITIAL_GOLD_COST);
                        currentPotion = new CustomPotion();
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        screenNum = 4;
                        break;
                }
                break;
            case 1:
                switch (buttonPressed) {
                    case 0:
                        setCost(0);
                        break;
                    case 1:
                        setCost(1);
                        break;
                    case 2:
                        setCost(2);
                        break;
                }
                break;
            case 2:
                switch (buttonPressed) {
                    case 0:
                        currentPotion = potionPreview1.makeStatEquivalentCopy();
                        possibleMods.remove(option1);
                        chosenMods.add(option1);
                        if (chosenMods.size() >= NUM_MODIFIERS) {
                            doneMakingPotion();
                        } else {
                            setModOptions();
                        }
                        break;
                    case 1:
                        currentPotion = potionPreview2.makeStatEquivalentCopy();
                        possibleMods.remove(option2);
                        chosenMods.add(option2);
                        if (chosenMods.size() >= NUM_MODIFIERS) {
                            doneMakingPotion();
                        } else {
                            setModOptions();
                        }
                        break;
                }
                break;
            case 3:
                switch (buttonPressed) {
                    case 0:
                        currentPotion = potionPreview1.makeStatEquivalentCopy();
                        AbstractDungeon.player.loseGold(BONUS_GOLD_COST);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        screenNum = 4;
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(currentPotion, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        screenNum = 4;
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(currentPotion, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }
    
    private void setCost(int cost) {
        if (cost == 0) {
            block = BLOCK0;
            damage = DAMAGE0;
            draw = DRAW0;
            weak = WEAK0;
            vulnerable = VULNERABLE0;
            poison = POISON0;
            CardModifierManager.addModifier(currentPotion, new CostMod(0));
        }
        if (cost == 1) {
            block = BLOCK1;
            damage = DAMAGE1;
            draw = DRAW1;
            weak = WEAK1;
            vulnerable = VULNERABLE1;
            poison = POISON1;
            CardModifierManager.addModifier(currentPotion, new CostMod(1));
        }
        if (cost == 2) {
            block = BLOCK2;
            damage = DAMAGE2;
            draw = DRAW2;
            weak = WEAK2;
            vulnerable = VULNERABLE2;
            poison = POISON2;
            CardModifierManager.addModifier(currentPotion, new CostMod(2));
        }
        screenNum = 2;
        possibleMods.add(new BlockMod(block));
        possibleMods.add(new DamageMod(damage));
        possibleMods.add(new DrawMod(draw));
        possibleMods.add(new WeakMod(weak));
        possibleMods.add(new VulnerableMod(vulnerable));
        possibleMods.add(new PoisonMod(poison));

        setModOptions();
    }

    private void doneMakingPotion() {
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        this.imageEventText.clearAllDialogs();
        potionPreview1 = currentPotion.makeStatEquivalentCopy();

        if (AbstractDungeon.player.gold >= BONUS_GOLD_COST) {
            if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                bonusMod = new RetainMod();
                CardModifierManager.addModifier(potionPreview1, bonusMod);
                this.imageEventText.setDialogOption(OPTIONS[0] + BONUS_GOLD_COST + OPTIONS[1] + OPTIONS[9], potionPreview1);
            } else {
                bonusMod = new InnateMod();
                CardModifierManager.addModifier(potionPreview1, bonusMod);
                this.imageEventText.setDialogOption(OPTIONS[0] + BONUS_GOLD_COST + OPTIONS[1] + OPTIONS[10], potionPreview1);
            }
        } else {
            this.imageEventText.setDialogOption(OPTIONS[7], true);
        }
        this.imageEventText.setDialogOption(OPTIONS[5], currentPotion);
        screenNum = 3;
    }

    private void setModOptions() {
        Collections.shuffle(possibleMods, AbstractDungeon.miscRng.random);
        option1 = possibleMods.get(0);
        option2 = possibleMods.get(1);
        potionPreview1 = currentPotion.makeStatEquivalentCopy();
        CardModifierManager.addModifier(potionPreview1, option1);
        String text1 = generateText(option1);
        text1 = FontHelper.colorString(text1, "g");

        potionPreview2 = currentPotion.makeStatEquivalentCopy();
        CardModifierManager.addModifier(potionPreview2, option2);
        String text2 = generateText(option2);
        text2 = FontHelper.colorString(text2, "g");

        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[3] + text1 + OPTIONS[4], potionPreview1);
        this.imageEventText.setDialogOption(OPTIONS[3] + text2 + OPTIONS[4], potionPreview2);
    }

    private String generateText(AbstractCardModifier mod) {
        String[] TEXT = CardCrawlGame.languagePack.getUIString(mod.identifier(null)).TEXT;
        int number = 0;
        if (mod.identifier(null).equals(BlockMod.ID)) {
            number = block;
        }
        if (mod.identifier(null).equals(DamageMod.ID)) {
            number = damage;
        }
        if (mod.identifier(null).equals(DrawMod.ID)) {
            number = draw;
        }
        if (mod.identifier(null).equals(WeakMod.ID)) {
            number = weak;
        }
        if (mod.identifier(null).equals(VulnerableMod.ID)) {
            number = vulnerable;
        }
        if (mod.identifier(null).equals(PoisonMod.ID)) {
            number = poison;
        }
        return TEXT[0] + number + TEXT[1];
    }

}
