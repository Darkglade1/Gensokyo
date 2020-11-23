package Gensokyo.monsters.act3;

import Gensokyo.monsters.AbstractSpriterMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class YuyukoSoul extends AbstractSpriterMonster {
    protected static final byte NONE = 0;
    protected static final byte DEBUFF = 1;
    protected static final int HP = 10;
    protected static final int MAX_HP_REDUCTION = 5;
    private static final float HB_W = 50.0F;
    private static final float HB_H = 100.0f;
    public boolean active = false;
    protected int cooldown;
    protected Yuyuko master;

    public YuyukoSoul(String name, String id, int maxHealth, float hb_x, float hb_y, String imgUrl, float offsetX, float offsetY, Yuyuko master, int bonusHealth) {
        super(name, id, maxHealth, hb_x, hb_y, HB_W, HB_H, imgUrl, offsetX, offsetY);
        this.master = master;
        this.setHp(HP + bonusHealth);
        this.cooldown = 1;
    }

    @Override
    public void usePreBattleAction() {
        this.halfDead = true;
        this.currentHealth = 0;
        this.healthBarUpdatedEvent();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void realRender(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(false);
        if (this instanceof BlueSoul) {
            master.blueSouls.remove(this);
            master.nextBlueSoul();
        }
        if (this instanceof PurpleSoul) {
            master.purpleSouls.remove(this);
            master.nextPurpleSoul();
        }
    }

    @Override
    public void takeTurn() {
        if (active && cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    protected void getMove(int num) {
        if (!active) {
            this.setMove(NONE, Intent.NONE);
        } else if (cooldown > 0) {
            this.setMove(NONE, Intent.NONE);
        } else {
            this.setMove(DEBUFF, Intent.DEBUFF);
        }
    }
}
