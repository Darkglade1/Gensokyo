package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.monsters.act3.Shinki.Alice;
import Gensokyo.monsters.act3.Shinki.Doll;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DollMagnet extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("DollMagnet");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int turns;
    Alice alice;

    public DollMagnet(AbstractCreature owner, int amount, int turns, Alice alice) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.turns = turns;
        this.alice = alice;
        this.amount2 = -1;
        this.type = PowerType.BUFF;
        this.loadRegion("magnet");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        spawn();
    }

    @Override
    public void atEndOfRound() {
        amount2++;
        if (amount2 % turns == 0 && amount2 > 0) {
            amount2 = 0;
            this.flash();
            spawn();
        }
    }

    private void spawn() {
        if (this.amount >= 1) {
            Doll minion1 = new Doll(-720.0F, 200.0F, alice, true);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion1, true));
            AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(minion1));
        }
        if (this.amount >= 2) {
            Doll minion2 = new Doll(-240.0F, 200.0F, alice, true);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion2, true));
            AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(minion2));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
