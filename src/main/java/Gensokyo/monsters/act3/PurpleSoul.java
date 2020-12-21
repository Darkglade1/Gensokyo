package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
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
}