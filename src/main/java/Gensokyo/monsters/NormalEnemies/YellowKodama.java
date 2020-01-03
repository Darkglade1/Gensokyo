package Gensokyo.monsters.NormalEnemies;

import Gensokyo.BetterSpriterAnimation;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class YellowKodama extends CustomMonster
{
    public static final String ID = "Gensokyo:YellowKodama";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte DEBUFF_ATTACK = 1;
    private static final byte BUFF_ATTACK = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 3;
    private static final int BUFF_ATTACK_DAMAGE = 4;
    private static final int A2_BUFF_ATTACK_DAMAGE = 5;
    private static final int BUFF = 2;
    private static final int A17_BUFF = 3;
    private static final int DEBUFF = 1;
    private static final int HP_MIN = 18;
    private static final int HP_MAX = 22;
    private static final int A7_HP_MIN = 19;
    private static final int A7_HP_MAX = 23;
    private int debuffDamage;
    private int buffAttackDamage;
    private int buff;
    private int debuff;

    public YellowKodama() {
        this(0.0f, 0.0f);
    }

    public YellowKodama(final float x, final float y) {
        super(YellowKodama.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 145.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Kodamas/YellowSpriter/KodamaAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.buffAttackDamage = A2_BUFF_ATTACK_DAMAGE;
        } else {
            this.buffAttackDamage = BUFF_ATTACK_DAMAGE;
        }

        this.debuff = DEBUFF;
        if (AbstractDungeon.ascensionLevel >= 17) {
           this.buff = A17_BUFF;
        } else {
           this.buff = BUFF;
        }
        this.damage.add(new DamageInfo(this, this.debuffDamage));
        this.damage.add(new DamageInfo(this, this.buffAttackDamage));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEBUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuff, true)));
                break;
            }
            case BUFF_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, buff), buff));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(BUFF_ATTACK)) {
            this.setMove(BUFF_ATTACK, Intent.ATTACK_BUFF, this.damage.get(1).base);
        } else if (!this.lastTwoMoves(DEBUFF_ATTACK)){
            this.setMove(DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(BUFF_ATTACK, Intent.ATTACK_BUFF, this.damage.get(1).base);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:YellowKodama");
        NAME = YellowKodama.monsterStrings.NAME;
        MOVES = YellowKodama.monsterStrings.MOVES;
        DIALOG = YellowKodama.monsterStrings.DIALOG;
    }
}