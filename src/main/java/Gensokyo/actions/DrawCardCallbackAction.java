package Gensokyo.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DrawCardCallbackAction extends AbstractGameAction {
    private int blockGain;
    private DamageInfo info;
    private AbstractMonster target;
    private AttackEffect effect;

    public DrawCardCallbackAction(int blockGain, DamageInfo info, AbstractMonster target, AttackEffect effect) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.blockGain = blockGain;
        this.info = info;
        this.target = target;
        this.effect = effect;
    }

    public DrawCardCallbackAction(DamageInfo info, AbstractMonster target, AttackEffect effect) {
        this(0, info, target, effect);
    }

    public DrawCardCallbackAction(int blockGain) {
        this(blockGain, null, null, null);
    }

    @Override
    public void update() {
        for (AbstractCard card : DrawCardAction.drawnCards) {
            if (card.type == CardType.SKILL && blockGain > 0) {
                addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.blockGain));
            }
            if (card.type == CardType.ATTACK && info != null && target != null) {
                addToTop(new DamageAction(target, info, effect));
            }
        }

        this.isDone = true;
    }
}
