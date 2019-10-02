package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.KillAction;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class PerfectCherryBlossom extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("PerfectCherryBlossom");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PerfectCherryBlossom.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PerfectCherryBlossom.png"));

    public PerfectCherryBlossom() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onTrigger() {
        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        ArrayList<AbstractMonster> notDeadMonsters = new ArrayList<>();
        Iterator iterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (iterator.hasNext()) {
            AbstractMonster mo = (AbstractMonster) iterator.next();
            if (!mo.isDeadOrEscaped()) {
                notDeadMonsters.add(mo);
            }
        }

        for (int i = 0; i < notDeadMonsters.size(); i++) {
            AbstractMonster mo = notDeadMonsters.get(i);
            //makes the special effects appear all at once for multiple monsters instead of one-by-one
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
            if (i == notDeadMonsters.size() - 1) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(mo.hb.cX, mo.hb.cY), 2.0F));
            } else {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(mo.hb.cX, mo.hb.cY)));
            }
        }

        for (int i = 0; i < notDeadMonsters.size(); i++) {
            AbstractMonster mo = notDeadMonsters.get(i);
            AbstractDungeon.actionManager.addToBottom(new KillAction(mo));
        }

    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
