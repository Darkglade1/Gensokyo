package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.KillAction;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
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

    private static final float HEAL_PERCENT = 0.10F;

    public PerfectCherryBlossom() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
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

    @Override
    public void onTrigger(AbstractCreature target) {
        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        int healAmt = (int)(AbstractDungeon.player.maxHealth * HEAL_PERCENT);
        if (healAmt < 1) {
            healAmt = 1;
        }

        AbstractDungeon.player.heal(healAmt, true);
        this.counter = 0;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
