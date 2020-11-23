package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.TemporaryMaxHPLossAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class BlueSoul extends YuyukoSoul
{
    public static final String ID = GensokyoMod.makeID("BlueSoul");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

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
                if (AbstractDungeon.player.maxHealth > 1 || TempHPField.tempHp.get(AbstractDungeon.player) > 0) {
                    Yuyuko.playGhostEffect(master);
                    AbstractDungeon.actionManager.addToBottom(new TemporaryMaxHPLossAction(MAX_HP_REDUCTION));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
}