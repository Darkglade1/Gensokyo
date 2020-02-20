package Gensokyo.monsters.act1.marisaMonsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.AnimatedMoveAction;
import Gensokyo.actions.RezAction;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.cards.Frozen;
import Gensokyo.powers.act1.ElementalBarrier;
import Gensokyo.powers.act1.FortitudePower;
import Gensokyo.powers.act1.VigorPower;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.HashMap;
import java.util.Map;

public class Patchouli extends CustomMonster
{
    public static final String ID = "Gensokyo:Patchouli";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private Map<Byte, EnemyMoveInfo> moves;
    private int element = 0;
    private static final int FIRE = 0;
    private static final int WATER = 1;
    private static final int WOOD = 2;
    private static final int METAL = 3;
    private static final int EARTH = 4;
    private static final int NUM_ELEMENTS = 5;
    private static final byte FIRE_MOVE_1 = 0;
    private static final byte FIRE_MOVE_2 = 1;
    private static final byte WATER_MOVE_1 = 2;
    private static final byte WATER_MOVE_2 = 3;
    private static final byte WOOD_MOVE_1 = 4;
    private static final byte WOOD_MOVE_2 = 5;
    private static final byte METAL_MOVE_1 = 6;
    private static final byte METAL_MOVE_2 = 7;
    private static final byte EARTH_MOVE_1 = 8;
    private static final byte EARTH_MOVE_2 = 9;
    private static final byte MOVE_1 = 10;
    private static final byte MOVE_2 = 11;
    private static final int HEAL = 10;
    private static final int A18_HEAL = 13;
    private static final int STRENGTH = 2;
    private static final int A18_STRENGTH = 3;
    private static final int BLOCK = 10;
    private static final int A8_BLOCK = 11;
    private static final int FIRE_ATTACK_DAMAGE = 35;
    private static final int A3_FIRE_ATTACK_DAMAGE = 38;
    private static final int WATER_HEAL_DAMAGE = 9;
    private static final int A3_WATER_HEAL_DAMAGE = 10;
    private static final int WATER_DEBUFF_DAMAGE = 11;
    private static final int A3_WATER_DEBUFF_DAMAGE = 12;
    private static final int WATER_STATUS_COUNT = 2;
    private static final int A18_WATER_STATUS_COUNT = 3;
    private static final int WOOD_BUFF_AMOUNT = 3;
    private static final int WOOD_MULTI_HIT_DAMAGE = 6;
    private static final int A3_WOOD_MULTI_HIT_DAMAGE = 7;
    private static final int WOOD_ATTACK_HITS = 3;
    private static final int METAL_MULTI_HIT_DAMAGE = 10;
    private static final int A3_METAL_MULTI_HIT_DAMAGE = 11;
    private static final int METAL_HIT_COUNT = 2;
    private static final int METAL_DEBUFF_DAMAGE = 10;
    private static final int A3_METAL_DEBUFF_DAMAGE = 11;
    private static final int METAL_DEBUFF = 2;
    private static final int EARTH_ATTACK_DAMAGE = 22;
    private static final int A3_EARTH_ATTACK_DAMAGE = 24;
    private static final int EARTH_DEBUFF_DAMAGE = 9;
    private static final int A3_EARTH_DEBUFF_DAMAGE = 10;
    private static final int EARTH_DEBUFF = 2;

    private int heal;
    private int strength;
    private int woodMultiDamage;
    private int waterDebuffDamage;
    private int status;
    private int metalDebuffDamage;
    private int waterHealDamage;
    private int earthAttackDamage;
    private int earthDebuffDamage;
    private int fireAttackDamage;
    private int metalMultiDamage;
    private int block;
    private boolean useRoyalFlare = false;
    private static final int HP = 50;
    private static final int A8_HP = 55;

    public static final int STARTING_INVINCIBLE = 10;
    public static final int INVINCIBLE_INCREMENT = 2;

    public static final float orbOffset = 400.0F;
    public AbstractMonster[] orbs = new AbstractMonster[NUM_ELEMENTS];

    public Patchouli() {
        this(0.0f, 0.0f);
    }

    public Patchouli(final float x, final float y) {
        super(Patchouli.NAME, ID, HP, -5.0F, 0, 200.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Patchouli/Spriter/PatchouliAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.heal = A18_HEAL;
            this.strength = A18_STRENGTH;
            this.status = A18_WATER_STATUS_COUNT;
        } else {
            this.heal = HEAL;
            this.strength = STRENGTH;
            this.status = WATER_STATUS_COUNT;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
            this.block = A8_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.woodMultiDamage = A3_WOOD_MULTI_HIT_DAMAGE;
            this.waterDebuffDamage = A3_WATER_DEBUFF_DAMAGE;
            this.metalDebuffDamage = A3_METAL_DEBUFF_DAMAGE;
            this.waterHealDamage = A3_WATER_HEAL_DAMAGE;
            this.earthAttackDamage = A3_EARTH_ATTACK_DAMAGE;
            this.earthDebuffDamage = A3_EARTH_DEBUFF_DAMAGE;
            this.fireAttackDamage = A3_FIRE_ATTACK_DAMAGE;
            this.metalMultiDamage = A3_METAL_MULTI_HIT_DAMAGE;
        } else {
            this.woodMultiDamage = WOOD_MULTI_HIT_DAMAGE;
            this.waterDebuffDamage = WATER_DEBUFF_DAMAGE;
            this.metalDebuffDamage = METAL_DEBUFF_DAMAGE;
            this.waterHealDamage = WATER_HEAL_DAMAGE;
            this.earthAttackDamage = EARTH_ATTACK_DAMAGE;
            this.earthDebuffDamage = EARTH_DEBUFF_DAMAGE;
            this.fireAttackDamage = FIRE_ATTACK_DAMAGE;
            this.metalMultiDamage = METAL_MULTI_HIT_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.fireAttackDamage));
        this.damage.add(new DamageInfo(this, this.waterHealDamage));
        this.damage.add(new DamageInfo(this, this.waterDebuffDamage));
        this.damage.add(new DamageInfo(this, this.woodMultiDamage));
        this.damage.add(new DamageInfo(this, this.metalMultiDamage));
        this.damage.add(new DamageInfo(this, this.metalDebuffDamage));
        this.damage.add(new DamageInfo(this, this.earthAttackDamage));
        this.damage.add(new DamageInfo(this, this.earthDebuffDamage));

        this.moves = new HashMap<>();
        this.moves.put(FIRE_MOVE_1, new EnemyMoveInfo(MOVE_1, Intent.BUFF, -1, 0, false));
        this.moves.put(FIRE_MOVE_2, new EnemyMoveInfo(MOVE_2, Intent.ATTACK, this.fireAttackDamage, 0, false));
        this.moves.put(WATER_MOVE_1, new EnemyMoveInfo(MOVE_1, Intent.ATTACK_DEBUFF, this.waterDebuffDamage, 0, false));
        this.moves.put(WATER_MOVE_2, new EnemyMoveInfo(MOVE_2, Intent.ATTACK_BUFF, this.waterHealDamage, 0, false));
        this.moves.put(WOOD_MOVE_1, new EnemyMoveInfo(MOVE_1, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(WOOD_MOVE_2, new EnemyMoveInfo(MOVE_2, Intent.ATTACK, this.woodMultiDamage, WOOD_ATTACK_HITS, true));
        this.moves.put(METAL_MOVE_1, new EnemyMoveInfo(MOVE_1, Intent.ATTACK, this.metalMultiDamage, METAL_HIT_COUNT, true));
        this.moves.put(METAL_MOVE_2, new EnemyMoveInfo(MOVE_2, Intent.ATTACK_DEBUFF, this.metalDebuffDamage, 0, false));
        this.moves.put(EARTH_MOVE_1, new EnemyMoveInfo(MOVE_1, Intent.ATTACK_DEBUFF, this.earthDebuffDamage, 0, false));
        this.moves.put(EARTH_MOVE_2, new EnemyMoveInfo(MOVE_2, Intent.ATTACK, this.earthAttackDamage, 0, false));

        Player.PlayerListener listener = new PatchouliListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly("LockedGirl");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ElementalBarrier(this, STARTING_INVINCIBLE), STARTING_INVINCIBLE));
        this.spawnOrbs();
        element = FIRE;
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case MOVE_1: {
                runAnim("SpellA");
                if (element == FIRE) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
                    this.useRoyalFlare = true;
                } else if (element == WATER) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.POISON));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Frozen(), this.status));
                } else if (element == WOOD) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                    if (this.hasPower(VigorPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FortitudePower(this, WOOD_BUFF_AMOUNT - 1, true), WOOD_BUFF_AMOUNT - 1));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VigorPower(this, WOOD_BUFF_AMOUNT, true), WOOD_BUFF_AMOUNT));
                    }
                } else if (element == METAL) {
                    for (int i = 0; i < METAL_HIT_COUNT; i++) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                } else if (element == EARTH) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.POISON));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, EARTH_DEBUFF, true), EARTH_DEBUFF));
                }
                break;
            }
            case MOVE_2: {
                runAnim("SpellB");
                if (element == FIRE) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                    this.useRoyalFlare = false;
                }   else if (element == WATER) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                    AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.heal));
                    AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                } else if (element == WOOD) {
                    for (int i = 0; i < WOOD_ATTACK_HITS; i++) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                } else if (element == METAL) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.POISON));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, METAL_DEBUFF, true), METAL_DEBUFF));
                } else if (element == EARTH) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (element == FIRE) {
            if (useRoyalFlare) {
                this.setMoveShortcut(FIRE_MOVE_2);
            } else {
                this.setMoveShortcut(FIRE_MOVE_1);
            }
        } else if (this.lastMove(MOVE_1)) {
            if (element == WATER) {
                this.setMoveShortcut(WATER_MOVE_2);
            } else if (element == WOOD) {
                this.setMoveShortcut(WOOD_MOVE_2);
            } else if (element == METAL) {
                this.setMoveShortcut(METAL_MOVE_2);
            } else if (element == EARTH) {
                this.setMoveShortcut(EARTH_MOVE_2);
            }
        } else {
            if (element == WATER) {
                this.setMoveShortcut(WATER_MOVE_1);
            } else if (element == WOOD) {
                this.setMoveShortcut(WOOD_MOVE_1);
            } else if (element == METAL) {
                this.setMoveShortcut(METAL_MOVE_1);
            } else if (element == EARTH) {
                this.setMoveShortcut(EARTH_MOVE_1);
            }
        }
        this.createIntent();
    }

    public void shiftIntent() {
        if (element == FIRE) {
            element = WATER;
        } else if (element == WATER) {
            element = WOOD;
        } else if (element == WOOD) {
            element = METAL;
        } else if (element == METAL) {
            element = EARTH;
        } else {
            element = FIRE;
        }
        if (moveHistory.size() > 0) {
            this.moveHistory.remove(moveHistory.size() - 1); //Needed so she shifts from Move1 types to other Move1 types and vice versa for Move2 types if player kills an orb
        }
        shiftOrbs();
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void spawnOrbs() {
        float x = 0;
        float y = orbOffset;
        AbstractMonster orb = new FireOrb(x, y, this);
        orbs[0] = orb;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(orb, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(orb));

        x = orbOffset * 0.60F;
        y = orbOffset * 0.60F;
        orb = new WaterOrb(x, y, this);
        orbs[1] = orb;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(orb, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(orb));

        x = orbOffset * 0.60F;
        y = orbOffset * 0.10F;
        orb = new WoodOrb(x, y, this);
        orbs[2] = orb;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(orb, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(orb));

        x = -orbOffset * 0.60F;
        y = orbOffset * 0.10F;
        orb = new MetalOrb(x, y, this);
        orbs[3] = orb;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(orb, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(orb));

        x = -orbOffset * 0.60F;
        y = orbOffset * 0.60F;
        orb = new EarthOrb(x, y, this);
        orbs[4] = orb;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(orb, true));
        AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(orb));
    }

    //Visually rearranges the orbs
    private void shiftOrbs() {
        AbstractMonster[] newOrbs = new AbstractMonster[orbs.length];
        for (int i = 0; i < orbs.length; i++) {
            AbstractMonster orb = orbs[i];
            if (i == 0) {
                newOrbs[orbs.length - 1] = orb;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new AnimatedMoveAction(orb, orb.drawX, orb.drawY, orbs[orbs.length - 1].drawX, orbs[orbs.length - 1].drawY)));
            } else {
                newOrbs[i - 1] = orb;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new AnimatedMoveAction(orb, orb.drawX, orb.drawY, orbs[i - 1].drawX, orbs[i - 1].drawY)));
            }
        }
        orbs = newOrbs;

        AbstractDungeon.actionManager.addToBottom(new RezAction(orbs[0])); //new active orb is at slot 0
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], info.nextMove, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Patchouli");
        NAME = Patchouli.monsterStrings.NAME;
        MOVES = Patchouli.monsterStrings.MOVES;
        DIALOG = Patchouli.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        runAnim("Defeat");
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof PatchyOrb) {
                if (!mo.isDead && !mo.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
        }
    }

    //Runs a specific animation
    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
    }

    //Resets character back to idle animation
    public void resetAnimation() {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Idle");
    }

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class PatchouliListener implements Player.PlayerListener {

        private Patchouli character;

        public PatchouliListener(Patchouli character) {
            this.character = character;
        }

        public void animationFinished(Animation animation){
            if (animation.name.equals("Defeat")) {
                character.stopAnimation();
            } else if (!animation.name.equals("Idle")) {
                character.resetAnimation();
            }
        }

        //UNUSED
        public void animationChanged(Animation var1, Animation var2){

        }

        //UNUSED
        public void preProcess(Player var1){

        }

        //UNUSED
        public void postProcess(Player var1){

        }

        //UNUSED
        public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){

        }
    }
}