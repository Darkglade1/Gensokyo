package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.powers.LunacyPower;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class LunaticRedEyes extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("LunaticRedEyes");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LunaticRedEyes.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LunaticRedEyes.png"));

    private static final int DEBUFF = 1;

    public LunaticRedEyes() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.flash();
        
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while(var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var3.next();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, AbstractDungeon.player, new LunacyPower(mo, AbstractDungeon.player, DEBUFF), DEBUFF));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DEBUFF + DESCRIPTIONS[1];
    }
}
