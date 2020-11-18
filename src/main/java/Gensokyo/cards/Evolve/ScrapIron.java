package Gensokyo.cards.Evolve;

import Gensokyo.CardMods.EvolveMod;
import Gensokyo.GensokyoMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Gensokyo.GensokyoMod.makeCardPath;

public class ScrapIron extends AbstractEvolveCard {
    public static final String ID = GensokyoMod.makeID(ScrapIron.class.getSimpleName());
    public static final String IMG = makeCardPath("MysteriousEgg.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 1;
    private static final int EVOLVE = 2;

    public ScrapIron() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        CardModifierManager.addModifier(this, new EvolveMod(EVOLVE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerEvolve() {
        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
    }
}