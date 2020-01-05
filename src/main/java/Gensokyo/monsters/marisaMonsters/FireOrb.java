package Gensokyo.monsters.marisaMonsters;

import Gensokyo.BetterSpriterAnimation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class FireOrb extends PatchyOrb {
    public static final String ID = "Gensokyo:FireOrb";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public FireOrb(float x, float y, Patchouli master) {
        super(NAME, ID, HP, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y, master);
        this.type = EnemyType.NORMAL;
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Fire");
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
