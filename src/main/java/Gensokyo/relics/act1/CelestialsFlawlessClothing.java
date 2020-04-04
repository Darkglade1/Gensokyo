package Gensokyo.relics.act1;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.monsters.act1.Komachi;
import Gensokyo.monsters.act2.Eiki;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class CelestialsFlawlessClothing extends CustomRelic implements OnPlayerDeathRelic {

    public static final String ID = GensokyoMod.makeID("CelestialsFlawlessClothing");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CelestialsFlawlessClothing.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CelestialsFlawlessClothing.png"));

    public AbstractMonster elite;

    public CelestialsFlawlessClothing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void onTrigger() {
        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        int healAmt = AbstractDungeon.player.maxHealth;
        if (healAmt < 1) {
            healAmt = 1;
        }

        AbstractDungeon.player.heal(healAmt, true);
        this.counter++;
        elite = new Komachi(-600.0f, 0);
        AbstractDungeon.actionManager.addToTop(new UsePreBattleActionAction(elite));
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(elite, false));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo instanceof Eiki) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(mo, Eiki.DIALOG[2]));
            }
        }
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer p, DamageInfo damageInfo) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!AbstractDungeon.getCurrRoom().monsters.monsters.contains(this.elite)) {
                this.onTrigger();
                return false;
            } else {
                if (this.elite.isDeadOrEscaped() || this.elite.isDying) {
                    this.onTrigger();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
