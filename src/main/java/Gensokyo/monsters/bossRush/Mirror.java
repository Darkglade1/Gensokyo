package Gensokyo.monsters.bossRush;

import Gensokyo.patches.MirrorGetAnimationPatch;
import Gensokyo.powers.Innocence;
import Gensokyo.powers.NextTurnInnocence;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.HashMap;
import java.util.Map;

public class Mirror extends CustomMonster
{
    public static final String ID = "Gensokyo:Mirror";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public static final byte GUILT = 0;
    public static final byte INNOCENCE = 1;
    public static final byte JUSTICE = 2;

    private static final int GUILT_DAMAGE = 15;
    private static final int A4_GUILT_DAMAGE = 16;
    private int guiltDamage;

    private static final int BLOCK = 15;
    private static final int A9_BLOCK = 16;
    private int block;

    private static final int HEAL = 3;
    private static final int A19_HEAL = 4;
    private int heal;

    private static final int STRENGTH = 3;
    private static final int A19_STRENGTH = 4;
    private int strength;

    private static final int HP = 150;
    private static final int A9_HP = 160;

    private Map<Byte, EnemyMoveInfo> moves;
    private Eiki eiki;

    public Mirror() {
        this(0.0f, 0.0f, null);
    }

    public Mirror(final float x, final float y, Eiki eiki) {
        super(NAME, ID, HP, 0.0F, 0, 200.0F, AbstractDungeon.player.hb_h, null, x, y);
        this.eiki = eiki;
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

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strength = A19_STRENGTH;
            this.heal = A19_HEAL;
        } else {
            this.strength = STRENGTH;
            this.heal = HEAL;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP, A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP, HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.guiltDamage = A4_GUILT_DAMAGE;
        } else {
            this.guiltDamage = GUILT_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GUILT, new EnemyMoveInfo(GUILT, Intent.ATTACK, this.guiltDamage, 0, false));
        this.moves.put(INNOCENCE, new EnemyMoveInfo(INNOCENCE, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(JUSTICE, new EnemyMoveInfo(JUSTICE, Intent.BUFF, -1, 0, true));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GUILT: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                eiki.incrementGuilt(1);
                break;
            }
            case INNOCENCE: {
                int difference = eiki.innocenceCount - eiki.guiltCount;
                if (difference < 0) {
                    difference = 0;
                }
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (this.hasPower(Innocence.POWER_ID)) {
                        this.addToBot(new HealAction(mo, this, this.block ));
                    } else {
                        this.addToBot(new GainBlockAction(mo, this.block));
                    }
                    this.addToBot(new HealAction(mo, this, this.heal + difference));
                }
                eiki.incrementInnocence(1);
                break;
            }
            case JUSTICE: {
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
                break;
            }
        }
        this.addToBot(new RemoveSpecificPowerAction(this, this, Innocence.POWER_ID));
        this.addToBot(new RemoveSpecificPowerAction(this, this, NextTurnInnocence.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        this.setMoveShortcut(GUILT);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        eiki.mirrorDeath();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Mirror");
        NAME = Mirror.monsterStrings.NAME;
        MOVES = Mirror.monsterStrings.MOVES;
        DIALOG = Mirror.monsterStrings.DIALOG;
    }
}