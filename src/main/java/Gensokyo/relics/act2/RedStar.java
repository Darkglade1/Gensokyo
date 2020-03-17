package Gensokyo.relics.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.ShootingStar;
import Gensokyo.events.act2.AHistoryOfViolence;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class RedStar extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("RedStar");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RedStar.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RedStar.png"));

    public static final int COMBATS = 1;
    private static final float MAX_HP_LOSS = AHistoryOfViolence.MAX_HP_LOSS;
    private int maxHPLost;

    public RedStar() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = COMBATS;
    }

    @Override
    public void atBattleStartPreDraw() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            if (counter > 0) {
                this.flash();
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                maxHPLost = (int)(AbstractDungeon.player.maxHealth * MAX_HP_LOSS);
                CardCrawlGame.sound.play("BLUNT_FAST");
                AbstractDungeon.player.decreaseMaxHealth(maxHPLost);
            }
        }
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            if (this.counter > 0) {
                this.counter--;
                if (this.counter <= 0) {
                    AbstractDungeon.player.increaseMaxHp(maxHPLost, true);
                    counter = 0;
                    AbstractCard card = new ShootingStar();
                    RewardItem reward = new RewardItem();
                    reward.cards.clear();
                    reward.cards.add(card);
                    AbstractDungeon.getCurrRoom().addCardReward(reward);
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (int)(MAX_HP_LOSS * 100) + DESCRIPTIONS[1];
    }

}
