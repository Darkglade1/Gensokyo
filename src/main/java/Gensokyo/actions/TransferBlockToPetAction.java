package Gensokyo.actions;

import Gensokyo.minions.AbstractPet;
import Gensokyo.minions.PetUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TransferBlockToPetAction extends AbstractGameAction {
    int maxBlock;

    public TransferBlockToPetAction(int maxBlock) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.maxBlock = maxBlock;
    }

    public void update() {
        AbstractPet pet = PetUtils.getPet();
        if (pet != null) {
            int blockToTransfer = maxBlock;
            if (AbstractDungeon.player.currentBlock < blockToTransfer) {
                blockToTransfer = AbstractDungeon.player.currentBlock;
            }
            if (blockToTransfer > 0) {
                int finalBlockToTransfer = blockToTransfer;
                addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractDungeon.player.loseBlock(finalBlockToTransfer);
                        this.isDone = true;
                    }
                });
                addToTop(new GainBlockAction(pet, blockToTransfer));
            }
        }

        this.isDone = true;
    }
}


