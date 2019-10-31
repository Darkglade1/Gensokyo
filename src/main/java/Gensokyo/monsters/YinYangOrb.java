package Gensokyo.monsters;

import Gensokyo.actions.YinYangAttackAction;
import Gensokyo.powers.MonsterPosition;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class YinYangOrb extends CustomMonster {
    public static final String ID = "Gensokyo:YinYangOrb";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final int HP = 10;
    private static final int A9_HP = 11;
    private static final byte MOVE = 1;
    private static final byte ATTACK = 2;
    private int delay;
    private int position;
    private float movement = Reimu.orbOffset;

    public YinYangOrb(float x, float y, int type, int position) {
        super(NAME, ID, HP, 0.0F, 0.0F, 160.0F, 160.0F, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/YinYangOrb/Spriter/YinYangOrb.scml");
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP * type);
            this.damage.add(new DamageInfo(this, A9_HP * type));
        } else {
            this.setHp(HP * type);
            this.damage.add(new DamageInfo(this, HP * type));
        }
        delay = type;
        this.position = position;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MonsterPosition(this, delay, position)));
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
        case MOVE:
            this.drawX -= movement;
            break;
        case ATTACK:
            this.drawX -= movement;
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
            AbstractDungeon.actionManager.addToBottom(new YinYangAttackAction(this.position, this.damage.get(0)));
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
            break;
        }
        delay--;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

//    @Override
//    public void update() {
//        super.update();
//        if (this.count % 2 == 0) {
//            this.animY = MathUtils.cosDeg((float)(System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
//        } else {
//            this.animY = -MathUtils.cosDeg((float)(System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
//        }
//
//    }

    @Override
    protected void getMove(int num) {
        if (delay > 1) {
            this.setMove(MOVE, Intent.UNKNOWN);
        } else {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:YinYangOrb");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
