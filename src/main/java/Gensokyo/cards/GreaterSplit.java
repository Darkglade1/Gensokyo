package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import static Gensokyo.GensokyoMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class GreaterSplit extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(GreaterSplit.class.getSimpleName());
    public static final String IMG = makeCardPath("GreaterSplitVertical.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 5;
    private static final int DAMAGE = 39;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int STUN = 1;

    public GreaterSplit() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = STUN;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            this.addToBot(new SFXAction("ATTACK_HEAVY"));
            this.addToBot(new VFXAction(p, new CleaveEffect(), 0.1F));
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new ApplyPowerAction(mo, p, new StunMonsterPower(mo, magicNumber), magicNumber));
            }
        } else {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            this.addToBot(new ApplyPowerAction(m, p, new StunMonsterPower(m, magicNumber), magicNumber));
        }
    }

    @Override
    public float getTitleFontSize()
    {
        return 16;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            name = languagePack.getCardStrings(cardID).EXTENDED_DESCRIPTION[0];
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            this.isMultiDamage = true;
            this.target = CardTarget.ALL_ENEMY;
            loadCardImage(makeCardPath("GreaterSplitHorizontal.png"));
            rawDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.upgraded) {
            return TextureLoader.getTexture(makeCardPath("GreaterSplitHorizontal_p.png"));
        } else {
            return super.getPortraitImage();
        }
    }
}
