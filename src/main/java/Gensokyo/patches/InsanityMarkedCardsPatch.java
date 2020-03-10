package Gensokyo.patches;

import Gensokyo.powers.act2.LunaticRedEyes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class InsanityMarkedCardsPatch {

    private static AbstractRelic redEyes = new Gensokyo.relics.act1.LunaticRedEyes();

    @SpirePatch(clz = AbstractCard.class, method = "renderCard")
    public static class RenderPatch {
        public static void Postfix(AbstractCard card, SpriteBatch sb, boolean b1, boolean b2) {
            if (AbstractDungeon.player != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (mo.hasPower(LunaticRedEyes.POWER_ID)) {
                        LunaticRedEyes eyes = (LunaticRedEyes)mo.getPower(LunaticRedEyes.POWER_ID);
                        if (eyes.isCardMarked(card)) {
                            redEyes.currentX = card.current_x + 25.0F * card.drawScale / 3.0f * Settings.scale;
                            redEyes.currentY = card.current_y + 696.0f * card.drawScale / 3.0f * Settings.scale;
                            redEyes.scale = card.drawScale;
                            redEyes.renderOutline(sb, false);
                            redEyes.render(sb);
                        }
                    }
                }
            }
        }
    }
}