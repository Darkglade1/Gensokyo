package Gensokyo.patches;

import Gensokyo.cards.PrivateSquare;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractMonster.class, method = "damage")
public class PrivateSquarePatch {
	public static void Prefix(final AbstractMonster __instance, final DamageInfo info) {
		if (info.output > 0) {
			for (AbstractCard card : AbstractDungeon.player.hand.group) {
				if (card.cardID.equals(PrivateSquare.ID)) {
					AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
				}
			}
		}
	}
}