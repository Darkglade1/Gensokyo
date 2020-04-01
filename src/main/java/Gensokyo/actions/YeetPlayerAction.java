package Gensokyo.actions;

import Gensokyo.relics.act1.PerfectCherryBlossom;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.LizardTail;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;

public class YeetPlayerAction extends AbstractGameAction {
    private AbstractPlayer p;

    public YeetPlayerAction() {
        this.p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        p.currentHealth = 0;
        AbstractDungeon.effectList.add(new StrikeEffect(this.p, this.p.hb.cX, this.p.hb.cY, 9999));
        if (AbstractDungeon.player.hasRelic(PerfectCherryBlossom.ID)) {
            AbstractDungeon.player.getRelic(PerfectCherryBlossom.ID).onTrigger();
        }
        boolean uGonDie = true;
        if (p.hasPotion(FairyPotion.POTION_ID) && !p.hasRelic(MarkOfTheBloom.ID)) {
            for (AbstractPotion q : p.potions) {
                if (q.ID.equals(FairyPotion.POTION_ID)) {// 1834
                    q.flash();// 1835
                    p.currentHealth = 0;// 1836
                    q.use(p);// 1837
                    AbstractDungeon.topPanel.destroyPotion(q.slot);// 1838
                    uGonDie = false;
                    break;
                }
            }
        } else if (p.hasRelic(LizardTail.ID) && !p.hasRelic(MarkOfTheBloom.ID)) {
            AbstractRelic lizardTail = p.getRelic(LizardTail.ID);
            if (lizardTail.counter == -1) {
                p.getRelic(LizardTail.ID).onTrigger();
                uGonDie = false;
            }
        } else {
            if (uGonDie)
                for (AbstractPower q : p.powers) {
                    if (q instanceof OnPlayerDeathPower) {
                        if (!(((OnPlayerDeathPower) q).onPlayerDeath(p, new DamageInfo(p, Integer.MAX_VALUE, DamageInfo.DamageType.HP_LOSS)))) {
                            uGonDie = false;
                            break;
                        }
                    }
                }
            if (uGonDie)
                for (AbstractRelic r : p.relics) {
                    if (r instanceof OnPlayerDeathRelic) {
                        if (!((OnPlayerDeathRelic) r).onPlayerDeath(p, new DamageInfo(p, Integer.MAX_VALUE, DamageInfo.DamageType.HP_LOSS))) {
                            uGonDie = false;
                            break;
                        }
                    }
                }
        }
        if (uGonDie) {
            p.isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            AbstractDungeon.actionManager.clearPostCombatActions();
        }

        this.isDone = true;
    }

}