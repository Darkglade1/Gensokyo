package Gensokyo.monsters.act1;

import Gensokyo.actions.SuicideActionAction;
import Gensokyo.actions.YinYangMoveAction;
import Gensokyo.minions.PetUtils;
import Gensokyo.powers.act1.MonsterPosition;
import Gensokyo.powers.act1.Position;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import kobting.friendlyminions.enums.MonsterIntentEnum;

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
    private float movement = Reimu.orbOffset * Settings.scale;
    private Reimu master;

    public YinYangOrb(float x, float y, int type, int position, int delay, Reimu master) {
        super(NAME, ID, HP, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/YinYangOrb/Spriter/YinYangOrb.scml");
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP * type);
        } else {
            this.setHp(HP * type);
        }
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, A9_HP * (4 - type)));
        } else {
            this.damage.add(new DamageInfo(this, HP * (4 - type)));
        }
        this.delay = delay;
        this.position = position;
        this.master = master;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MonsterPosition(this, delay, position)));
    }

    private void move() {
        master.orbs[delay - 1][position - 1].remove(this);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new YinYangMoveAction(this, this.drawX, this.drawX - movement)));
        delay--;
        if (delay > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, MonsterPosition.POWER_ID, 1));
            master.orbs[delay - 1][position - 1].add(this);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(false);
        if (delay > 0) {
            master.orbs[delay - 1][position - 1].remove(this);
        }
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
        case MOVE:
            move();
            break;
        case ATTACK:
            move();
            int playerPosition = 1;
            if (AbstractDungeon.player.hasPower(Position.POWER_ID)) {
                playerPosition = AbstractDungeon.player.getPower(Position.POWER_ID).amount;
            }
            if (playerPosition == this.position) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
            }
            addToBot(new SuicideActionAction(this));
            break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (delay > 1) {
            this.setMove(MOVE, Intent.NONE); //Setting intent to none makes their turn go by much faster
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
