package Gensokyo.monsters.marisaMonsters;

import Gensokyo.actions.YinYangAttackAction;
import Gensokyo.actions.YinYangMoveAction;
import Gensokyo.monsters.Reimu;
import Gensokyo.powers.MonsterPosition;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class FireOrb extends PatchyOrb {
    public static final String ID = "Gensokyo:FireOrb";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public FireOrb(float x, float y, Patchouli master) {
        super(NAME, ID, HP, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y, master);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/YinYangOrb/Spriter/YinYangOrb.scml");
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void usePreBattleAction() {
        this.halfDead = false;
        this.currentHealth = maxHealth;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:FireOrb");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
