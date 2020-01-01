package Gensokyo.monsters.marisaMonsters;

import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class WoodOrb extends PatchyOrb {
    public static final String ID = "Gensokyo:WoodOrb";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public WoodOrb(float x, float y, Patchouli master) {
        super(NAME, ID, HP, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y, master);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/YinYangOrb/Spriter/YinYangOrb.scml");
        this.type = EnemyType.NORMAL;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:WoodOrb");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
