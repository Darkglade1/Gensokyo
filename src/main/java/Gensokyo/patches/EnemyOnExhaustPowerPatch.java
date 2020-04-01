package Gensokyo.patches;

import Gensokyo.powers.act2.LunaticRedEyes;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz = CardGroup.class,
        method = "moveToExhaustPile"
)

// A patch that notifies enemy powers when the player exhausts a card so Lunatic Red Eyes works
public class EnemyOnExhaustPowerPatch {
    @SpirePrefixPatch
    public static void triggerOnExhaust(CardGroup instance, AbstractCard card) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower power : mo.powers) {
                if (power instanceof LunaticRedEyes) {
                    power.onExhaust(card);
                }
            }
        }
    }
}
