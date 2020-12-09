
package Gensokyo.monsters.act3.Shinki;

import Gensokyo.monsters.AbstractSpriterMonster;

public abstract class AbstractShinkiDelusion extends AbstractSpriterMonster {

    public AbstractShinkiEvent event1;
    public AbstractShinkiEvent event2;
    public AbstractShinkiEvent event3;
    public Shinki shinki;

    public AbstractShinkiDelusion(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    @Override
    public void die(boolean triggerRelics) {
        shinki.onDelusionDeath();
        super.die(triggerRelics);
    }

    public abstract String eventDialog(int eventNum);


}
