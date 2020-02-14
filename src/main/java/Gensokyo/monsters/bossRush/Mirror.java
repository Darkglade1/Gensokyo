package Gensokyo.monsters.bossRush;

import Gensokyo.patches.MirrorGetAnimationPatch;
import Gensokyo.powers.FortitudePower;
import Gensokyo.powers.VigorPower;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;

public class Mirror extends CustomMonster
{
    public static final String ID = "Gensokyo:Mirror";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte BUFF = 1;
    private static final byte CHARGE = 2;
    private static final byte BIG_ATTACK = 3;
    private static final byte ATTACK = 4;
    private static final int BIG_ATTACK_DAMAGE = 24;
    private static final int A2_BIG_ATTACK_DAMAGE = 26;
    private static final int ATTACK_DAMAGE = 15;
    private static final int A2_ATTACK_DAMAGE = 17;
    private static final int VIGOR_BUFF = 3;
    private static final int FORTITUDE_BUFF = 1;
    private static final int HP_MIN = 47;
    private static final int HP_MAX = 51;
    private static final int A7_HP_MIN = 49;
    private static final int A7_HP_MAX = 53;
    private int bigAttackdamage;
    private int attackDamage;

    public Mirror() {
        this(0.0f, 0.0f);
    }

    public Mirror(final float x, final float y) {
        super(NAME, ID, HP_MAX, 0.0F, 0, 200.0F, AbstractDungeon.player.hb_h, null, x, y);
        this.name = AbstractDungeon.player.title;
        if (MirrorGetAnimationPatch.playerAtlasURL != null && MirrorGetAnimationPatch.playerSkeletonURL != null) {
            loadAnimation(MirrorGetAnimationPatch.playerAtlasURL, MirrorGetAnimationPatch.playerSkeletonURL, MirrorGetAnimationPatch.playerScale);
            try {
                AnimationState.TrackEntry e = this.state.setAnimation(MirrorGetAnimationPatch.playerTrackIndex, MirrorGetAnimationPatch.playerAnimationName, MirrorGetAnimationPatch.playerLoop);
                this.stateData.setMix(MirrorGetAnimationPatch.playerFrom, MirrorGetAnimationPatch.playerTo, MirrorGetAnimationPatch.playerDuration);
                e.setTimeScale(MirrorGetAnimationPatch.playerTimeScale);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Gensokyo: Someone decided to change up their animation");
            }

        } else if (AbstractDungeon.player.img != null) {
            this.img = AbstractDungeon.player.img;
        } else if (MirrorGetAnimationPatch.spriterAnimation != null) {
            try {
                this.animation = new SpriterAnimation(MirrorGetAnimationPatch.spriterAnimation);
                this.animation.setFlip(true, false);
            } catch (Exception ex) {
                System.out.println(ex);
                System.out.println("The mirror is fucking broken.");
                this.name = Ironclad.NAMES[0];
                this.loadAnimation("images/characters/ironclad/idle/skeleton.atlas", "images/characters/ironclad/idle/skeleton.json", 1.0F);
                AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
                this.stateData.setMix("Hit", "Idle", 0.1F);
                e.setTimeScale(0.6F);
            }

        } else {
            this.name = Ironclad.NAMES[0];
            this.loadAnimation("images/characters/ironclad/idle/skeleton.atlas", "images/characters/ironclad/idle/skeleton.json", 1.0F);
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
            this.stateData.setMix("Hit", "Idle", 0.1F);
            e.setTimeScale(0.6F);
        }
        this.flipHorizontal = true;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = A2_ATTACK_DAMAGE;
            this.bigAttackdamage = A2_BIG_ATTACK_DAMAGE;
        } else {
            this.attackDamage = ATTACK_DAMAGE;
            this.bigAttackdamage = BIG_ATTACK_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.bigAttackdamage));
        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BUFF: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, VIGOR_BUFF, true)));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FortitudePower(this, FORTITUDE_BUFF, true)));
                }
                break;
            }
            case CHARGE: {
                break;
            }
            case BIG_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(BUFF)) {
            this.setMove(CHARGE, Intent.UNKNOWN);
        } else if (this.lastMove(CHARGE)) {
            this.setMove(BIG_ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else if (this.lastMove(BIG_ATTACK)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base);
        } else {
            this.setMove(BUFF, Intent.BUFF);
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Mirror");
        NAME = Mirror.monsterStrings.NAME;
        MOVES = Mirror.monsterStrings.MOVES;
        DIALOG = Mirror.monsterStrings.DIALOG;
    }
}