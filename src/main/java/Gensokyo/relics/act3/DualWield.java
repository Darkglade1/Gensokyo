package Gensokyo.relics.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class DualWield extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("DualWield");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DualWield.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DualWield.png"));

    private AbstractCard usedCard = null;
    private boolean active = true;

    public DualWield() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                usedCard = card;
            } else {
                usedCard = null;
            }
        } else {
            usedCard = null;
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractPlayer p = AbstractDungeon.player;
        int[] newMultiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
        if (usedCard != null && info.owner == p && info.type == DamageInfo.DamageType.NORMAL) {
            if(active) {
                active = false;
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        active = true;
                    }
                });
                this.flash();
                for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                    AbstractMonster mo = AbstractDungeon.getMonsters().monsters.get(i);
                    if (target != mo && !mo.isDeadOrEscaped()) {
                        usedCard.calculateCardDamage(mo);
                        int newDamage = usedCard.damage;
                        newMultiDamage[i] = newDamage;
                    } else if (target == mo) {
                        newMultiDamage[i] = 0;
                    }
                }
                AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, newMultiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
