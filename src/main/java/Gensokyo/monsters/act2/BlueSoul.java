package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;

public class BlueSoul extends YuyukoSoul
{
    public static final String ID = "Gensokyo:BlueSoul";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public BlueSoul() {
        this(0.0f, 0.0f, null, 0);
    }

    public BlueSoul(final float x, final float y, Yuyuko yuyuko, int bonusHealth) {
        super(NAME, ID, HP, 0.0F, 0, null, x, y, yuyuko, bonusHealth);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/BlueSoul/Spriter/BlueSoulAnimation.scml");
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        switch (this.nextMove) {
            case DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMT, true), DEBUFF_AMT));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:BlueSoul");
        NAME = BlueSoul.monsterStrings.NAME;
        MOVES = BlueSoul.monsterStrings.MOVES;
        DIALOG = BlueSoul.monsterStrings.DIALOG;
    }
}