package Gensokyo.patches;

import Gensokyo.events.extra.CandidFailure;
import Gensokyo.events.extra.CandidFriend;
import Gensokyo.rooms.nitori.Nitori;
import Gensokyo.rooms.nitori.NitoriStoreScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.EventRoom;
import javassist.CtBehavior;

import static Gensokyo.GensokyoMod.makeUIPath;

public class NitoriBattlePatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class DeathPrevention {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info) {
            if (deathLogic()) { return SpireReturn.Return(null); }
            return SpireReturn.Continue();
        }


        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "deathScreen");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    public static boolean deathLogic() {
        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
            if(m instanceof Nitori && !m.isDying){
                AbstractDungeon.player.isDead = false;
                AbstractDungeon.player.currentHealth = 1;
                AbstractDungeon.player.healthBarUpdatedEvent();
                AbstractDungeon.overlayMenu.proceedButton.hide();
                NitoriStoreScreen.init();
                AbstractDungeon.topLevelEffects.clear();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.currMapNode.room = new EventRoom();
                AbstractDungeon.currMapNode.room.setMapImg(new Texture(makeUIPath("nitori.png")), new Texture(makeUIPath("nitori_outline.png")));
                AbstractDungeon.getCurrRoom().event = new CandidFailure();
                AbstractDungeon.getCurrRoom().event.reopen();
                CardCrawlGame.fadeIn(1.5F);
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
                AbstractDungeon.overlayMenu.hideCombatPanels();
                return true;
            }
        }
        return false;
    }


}