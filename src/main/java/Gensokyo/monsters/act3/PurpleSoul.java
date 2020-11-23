package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.powers.act3.BorderOfDeath;
import Gensokyo.vfx.EmptyEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class PurpleSoul extends YuyukoSoul
{
    public static final String ID = GensokyoMod.makeID("PurpleSoul");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public PurpleSoul() {
        this(0.0f, 0.0f, null, 0);
    }

    public PurpleSoul(final float x, final float y, Yuyuko yuyuko, int bonusHealth) {
        super(NAME, ID, HP, 0.0F, 0, null, x, y, yuyuko, bonusHealth);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/PurpleSoul/Spriter/PurpleSoulAnimation.scml");
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        switch (this.nextMove) {
            case DEBUFF: {
                master.runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.player.decreaseMaxHealth(MAX_HP_REDUCTION);
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BorderOfDeath(AbstractDungeon.player, MAX_HP_REDUCTION), MAX_HP_REDUCTION));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
}