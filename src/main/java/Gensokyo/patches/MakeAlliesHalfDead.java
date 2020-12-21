package Gensokyo.patches;

import Gensokyo.monsters.act3.Remilia;
import Gensokyo.monsters.act3.Shinki.Shinki;
import Gensokyo.monsters.act3.Yuyuko;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"

)
// A patch to make allies halfdead at the start of the player's turn
public class MakeAlliesHalfDead {
    @SpireInsertPatch(locator = MakeAlliesHalfDead.Locator.class)
    public static void MakeAlliesHalfDeadReee(GameActionManager instance) {
        if (AbstractDungeon.getCurrRoom() != null) {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo.id.equals(Remilia.ID) || mo.id.equals(Shinki.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            mo.halfDead = true;
                            mo.healthBarUpdatedEvent();
                            AbstractDungeon.onModifyPower(); //I need this here to fucking make sure alice and sariel intents get updated akdjgkadjgaldjbfag
                            this.isDone = true;
                        }
                    });
                    break;
                }
                if (mo.id.equals(Yuyuko.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            AbstractDungeon.onModifyPower(); //I need this here in case the Spirits get fucking STRENGTH somehow
                            this.isDone = true;
                        }
                    });
                }
            }
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}