package Gensokyo.actions;

import Gensokyo.patches.ExhaustCardHook;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CallbackExhaustAction extends ExhaustAction {
    private Consumer<ArrayList<AbstractCard>> callback;

    public CallbackExhaustAction(int numCards, boolean isRandom, boolean anyNumber, boolean canPickZero, Consumer<ArrayList<AbstractCard>> callback) {
        super(AbstractDungeon.player, AbstractDungeon.player, numCards, isRandom, anyNumber, canPickZero);
        this.callback = callback;
    }

    @Override
    public void update() {
        ExhaustCardHook.callback = callback;
        super.update();
        ExhaustCardHook.callback = null;
    }
}