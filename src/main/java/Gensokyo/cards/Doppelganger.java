package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.act1.DoppelgangerPower;
import Gensokyo.tags.Tags;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class Doppelganger extends AbstractUrbanLegendCard {

    public static final String ID = GensokyoMod.makeID(Doppelganger.class.getSimpleName());
    public static final String IMG = makeCardPath("Doppelganger.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = -1;

    public Doppelganger() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        tags.add(Tags.URBAN_LEGEND);
        this.selfRetain = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.upgraded) {
            effect++;
        }

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (effect > 0) {
            AbstractDungeon.actionManager.addToBottom(new ScryAction(effect));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoppelgangerPower(p, effect), effect));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
