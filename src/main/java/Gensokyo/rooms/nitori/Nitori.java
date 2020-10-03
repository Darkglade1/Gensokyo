package Gensokyo.rooms.nitori;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.events.extra.CandidFailure;
import Gensokyo.events.extra.CandidFriend;
import Gensokyo.monsters.act1.Yukari;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;

import java.util.Arrays;

import static Gensokyo.GensokyoMod.makeID;
import static Gensokyo.GensokyoMod.makeUIPath;
import static com.megacrit.cardcrawl.shop.Merchant.*;

// (SECTION 0) - Introduction

// If you're reading this to understand how Nitori's actions function codewise, welcome!
// If you're reading this to understand Nitori's mechanics, welcome as well!
// Regardless, I hope this documentation fulfills what you're looking for.

// The documentation for this boss is split into the following categories:

    // (SECTION 1) - Class-Information - Information about the properties of Nitori

    // (SECTION 2) - Mechanics Information - Information about Nitori's fight. This includes:
        // 2.a) Turn pattern for Nitori's fight.
        // 2.b) Unique mechanics to Nitori's fight:
            // 2.b.a) Nitori and the turn-timer.
            // 2.b.b) Nitori's unique statuses
            // 2.b.c) Nitori's unique buffs.
            // 2.b.d) Nitori's unique debuffs.

public class Nitori extends CustomMonster {
    // (SECTION 1) - Class-Information - Information about the properties of Nitori
    // Nitori's ID, used for localisation files, and is prefixed with the modID.
    public static final String ID = makeID(Nitori.class.getSimpleName());
    // Localisation files for Nitori, fetched using the ID.
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = monsterStrings.NAME;
    // Nitori's "States". This controls the scaling of Nitori's actions mentioned earlier in the file.
    // PHASE_1 refers to pre Act 1 Boss Nitori.
    // PHASE_2 refers to pre Act 2 Boss Nitori.
    // PHASE_3 refers to pre Act 3 Boss Nitori.
    // PHASE_4 refers to post Act 3 Boss Nitori.
    public enum nitoriStates{PHASE_1, PHASE_2, PHASE_3, PHASE_4}
    // HP information for Nitori (PHASE_1)
    private static final int HP_0BOSS = 250 + 140 + 240 / 3;
    private static final int HP_0BOSS_A9 = 264 + 150 + 250 / 3;
    // HP information for Nitori (PHASE_2)
    private static final int HP_1BOSS = 420 + 282 + 300 / 3;
    private static final int HP_1BOSS_A9 = 440 + 300 + 320 / 3;
    // HP information for Nitori (PHASE_3) && PHASE_4 (HP_2BOSS_A9)
    private static final int HP_2BOSS = (250 * 2) + (300 * 2) + 456 / 3;
    private static final int HP_2BOSS_A9 = (265 * 2) + (320 * 2) + 480 / 3;
    // Move Bytes for Nitori.
    // These moves often perform similar actions, but vary in interaction between phases.
    private static final byte OPENING = 1;
    private static final byte BUFF = 2;
    private static final byte ATTACK_STANDARD_MULTIHIT = 3;
    private static final byte ATTACK_STANDARD = 4;
    private static final byte RNG_FRAIL = 5;
    private static final byte RNG_ATTACK_BLOCK = 6;
    private static final byte RNG_TEMPHP = 7;
    private static final byte RNG_ATTACK = 8;
    private static final byte RNG_STR_DEX_DOWN = 9;
    private static final byte TURN_CYCLE_END_ATTACK = 10;
    private static final byte TURN_LIMIT_REACHED = 99;
    // Move Characteristics for Nitori, ordered by move-byte appearance (See Above)
    // OPENING
    private static final AbstractCard[] OPENING_STATUS_POOL = {new Burn(), new Burn(), new Burn(), new Burn(), new Burn()};
    private static final int PHASE1_OPENING_STATUS_AMOUNT = 1;
    private static final int PHASE2_OPENING_STATUS_AMOUNT = 2;
    private static final int PHASE3_OPENING_STATUS_AMOUNT = 4;
    private static final int PHASE4_OPENING_STATUS_AMOUNT = 5;
    private static final int OPENING_DEBUFF_AMOUNT = 2;
    // BUFF
    private static final int PHASE1_BUFF_STRENGTH_AMOUNT = 1;
    private static final int PHASE2_BUFF_STRENGTH_AMOUNT = 2;
    private static final int PHASE3_BUFF_STRENGTH_AMOUNT = 2;
    private static final int PHASE4_BUFF_STRENGTH_AMOUNT = 3;
    private static final int PHASE1_BUFF_ARTIFACT_AMOUNT = 1;
    private static final int PHASE2_BUFF_ARTIFACT_AMOUNT = 2;
    // ATTACK_STANDARD_MULTIHIT
    private static final int PHASE1_ATTACK_STANDARD_MULTIHIT_DAMAGE = 5;
    private static final int PHASE2_ATTACK_STANDARD_MULTIHIT_DAMAGE = 7;
    private static final int PHASE3_ATTACK_STANDARD_MULTIHIT_DAMAGE = 9;
    private static final int PHASE4_ATTACK_STANDARD_MULTIHIT_DAMAGE = 11;
    private static final int ATTACK_STANDARD_MULTIHIT_HITS = 2;
    // ATTACK_STANDARD
    private static final int PHASE1_ATTACK_STANDARD_DAMAGE = 9;
    private static final int PHASE2_ATTACK_STANDARD_DAMAGE = 13;
    private static final int PHASE3_ATTACK_STANDARD_DAMAGE = 17;
    private static final int PHASE4_ATTACK_STANDARD_DAMAGE = 21;
    // RNG_FRAIL
    private static final int PHASE1_RNG_FRAIL_FRAIL_AMOUNT = 1;
    private static final int PHASE2_RNG_FRAIL_FRAIL_AMOUNT = 1;
    private static final int PHASE3_RNG_FRAIL_FRAIL_AMOUNT = 2;
    private static final int PHASE4_RNG_FRAIL_FRAIL_AMOUNT = 2;
    private static final int PHASE1_RNG_FRAIL_DRAW_REDUCTION = 1;
    private static final int PHASE2_RNG_FRAIL_DRAW_REDUCTION = 2;
    private static final int PHASE3_RNG_FRAIL_DRAW_REDUCTION = 2;
    private static final int PHASE4_RNG_FRAIL_DRAW_REDUCTION = 2;
    private static final int PHASE3_RNG_FRAIL_ENERGY_REDUCTION = 1;
    private static final AbstractCard PHASE4_RNG_FRAIL_STATUS = new VoidCard();
    // RNG_ATTACK_BLOCK
    private static final int PHASE1_RNG_ATTACK_BLOCK_DAMAGE = 7;
    private static final int PHASE2_RNG_ATTACK_BLOCK_DAMAGE = 11;
    private static final int PHASE3_RNG_ATTACK_BLOCK_DAMAGE = 15;
    private static final int PHASE4_RNG_ATTACK_BLOCK_DAMAGE = 19;
    // RNG_TEMPHP
    private static final int PHASE1_RNG_TEMPHP_TEMPHP = 25;
    private static final int PHASE2_RNG_TEMPHP_TEMPHP = 35;
    private static final int PHASE3_RNG_TEMPHP_TEMPHP = 50;
    private static final int PHASE4_RNG_TEMPHP_TEMPHP = 75;
    // RNG_ATTACK
    private static final int PHASE1_RNG_ATTACK_STANDARD_DAMAGE = 5;
    private static final int PHASE2_RNG_ATTACK_STANDARD_DAMAGE = 9;
    private static final int PHASE3_RNG_ATTACK_STANDARD_DAMAGE = 13;
    private static final int PHASE4_RNG_ATTACK_STANDARD_DAMAGE = 17;
    private static final AbstractCard RNG_ATTACK_STANDARD_STATUS = new VoidCard();
    private static final int PHASE1_RNG_ATTACK_MULTIHIT_DAMAGE = 3;
    private static final int PHASE2_RNG_ATTACK_MULTIHIT_DAMAGE = 5;
    private static final int PHASE3_RNG_ATTACK_MULTIHIT_DAMAGE = 7;
    private static final int PHASE4_RNG_ATTACK_MULTIHIT_DAMAGE = 9;
    private static final int RNG_ATTACK_MULTIHIT_HITS = 2;
    private static final int RNG_ATTACK_MULTIHIT_WEAK_AMOUNT = 2;
    private static final int RNG_ATTACK_MULTIHIT_VULNERABLE_AMOUNT = 1;
    // RNG_STR_DEX_DOWN
    private static final int PHASE1_RNG_STR_DEX_DOWN = 1;
    private static final int PHASE2_RNG_STR_DEX_DOWN = 2;
    private static final int PHASE3_RNG_STR_DEX_DOWN = 3;
    private static final int PHASE4_RNG_STR_DEX_DRAIN = 3;
    // TURN_CYCLE_END_ATTACK
    private static final int PHASE1_TURN_CYCLE_END_ATTACK_DAMAGE = 25;
    private static final int PHASE2_TURN_CYCLE_END_ATTACK_DAMAGE = 35;
    private static final int PHASE3_TURN_CYCLE_END_ATTACK_DAMAGE = 45;
    private static final int PHASE4_TURN_CYCLE_END_ATTACK_DAMAGE = 55;
    // Nitori Fight Information
    private static final int[] damageInfo = {
            // Indexes 0-3
            PHASE1_ATTACK_STANDARD_MULTIHIT_DAMAGE,
            PHASE2_ATTACK_STANDARD_MULTIHIT_DAMAGE,
            PHASE3_ATTACK_STANDARD_MULTIHIT_DAMAGE,
            PHASE4_ATTACK_STANDARD_MULTIHIT_DAMAGE,
            // 4 - 7
            PHASE1_ATTACK_STANDARD_DAMAGE,
            PHASE2_ATTACK_STANDARD_DAMAGE,
            PHASE3_ATTACK_STANDARD_DAMAGE,
            PHASE4_ATTACK_STANDARD_DAMAGE,
            // 8 - 11
            PHASE1_RNG_ATTACK_BLOCK_DAMAGE,
            PHASE2_RNG_ATTACK_BLOCK_DAMAGE,
            PHASE3_RNG_ATTACK_BLOCK_DAMAGE,
            PHASE4_RNG_ATTACK_BLOCK_DAMAGE,
            // 12 - 15
            PHASE1_RNG_ATTACK_STANDARD_DAMAGE,
            PHASE2_RNG_ATTACK_STANDARD_DAMAGE,
            PHASE3_RNG_ATTACK_STANDARD_DAMAGE,
            PHASE4_RNG_ATTACK_STANDARD_DAMAGE,
            // 16 - 19
            PHASE1_RNG_ATTACK_MULTIHIT_DAMAGE,
            PHASE2_RNG_ATTACK_MULTIHIT_DAMAGE,
            PHASE3_RNG_ATTACK_MULTIHIT_DAMAGE,
            PHASE4_RNG_ATTACK_MULTIHIT_DAMAGE,
            // 20 - 23
            PHASE1_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE2_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE3_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE4_TURN_CYCLE_END_ATTACK_DAMAGE
    };
    // Tracks the Artificial-Turn-Timer
    private static nitoriStates beginningState = nitoriStates.PHASE_1;
    private static nitoriStates currentState = nitoriStates.PHASE_1;
    private int globalCounter = 0;
    private static final int PHASE1_START_ENDTIMER = 50;
    private static final int PHASE2_START_ENDTIMER = 40;
    private static final int PHASE3_START_ENDTIMER = 30;
    private static final int PHASE4_START_ENDTIMER = 20;
    // Tracks how Nitori should move.
    private boolean OPENING_DONE = false;
    private boolean BUFF_DONE = false;
    private boolean ATTACK_STANDARD_MULTIHIT_DONE = false;
    private boolean ATTACK_STANDARD_DONE = false;
    private boolean RNG_FRAIL_DONE = false;
    private boolean RNG_ATTACK_BLOCK_DONE = false;
    private boolean RNG_TEMPHP_DONE = false;
    private boolean RNG_ATTACK_DONE = false;
    private boolean RNG_STR_DEX_DOWN_DONE = false;
    public Nitori() { this(0.0f, 0.0f); }
    public Nitori(final float x, final float y) {
        super(NAME, ID, HP_0BOSS, -5.0F, 0, 280.0f, 285.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Nitori/Spriter/NitoriAnimations.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 9) {
            switch (AbstractDungeon.bossCount){
                case 0:
                    this.setHp(HP_0BOSS_A9);
                    this.setState(nitoriStates.PHASE_1);
                    break;
                case 1:
                    this.setHp(HP_1BOSS_A9);
                    this.setState(nitoriStates.PHASE_2);
                    break;
                case 2:
                    this.setHp(HP_2BOSS_A9);
                    this.setState(nitoriStates.PHASE_3);
                default:
                    this.setHp(HP_2BOSS_A9);
                    this.setState(nitoriStates.PHASE_4);
                    break;
            }
        } else {
            switch (AbstractDungeon.bossCount){
                case 0:
                    this.setHp(HP_0BOSS);
                    this.setState(nitoriStates.PHASE_1);
                    break;
                case 1:
                    this.setHp(HP_1BOSS);
                    this.setState(nitoriStates.PHASE_2);
                    break;
                case 2:
                    this.setHp(HP_2BOSS);
                    this.setState(nitoriStates.PHASE_3);
                default:
                    this.setHp(HP_2BOSS_A9);
                    this.setState(nitoriStates.PHASE_4);
                    break;
            }
        }
        setBeginningState(currentState);
        for(int i : damageInfo){ this.damage.add(new DamageInfo(this, i)); }
        // Animation Handler
        Player.PlayerListener listener = new NitoriListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }
    public void setBeginningState(nitoriStates state){ beginningState = state; }
    public nitoriStates getBeginningState(){ return beginningState; }
    public nitoriStates getState(){ return currentState; }
    public void setState(nitoriStates state){ currentState = state; }
    public int getGlobalCounter(){ return globalCounter; }
    public void incrementGlobalCounter(){
        globalCounter++;
        // todo: decrement power
    }
    public void setGlobalCounter(int value){ globalCounter = value; }
    public boolean getBattleEnd(){
        switch (getState()){
            case PHASE_4:
                switch (getBeginningState()){
                    case PHASE_1:
                        if(getGlobalCounter() >= PHASE1_START_ENDTIMER){ return true; }
                        return false;
                    case PHASE_2:
                        if(getGlobalCounter() >= PHASE2_START_ENDTIMER){ return true; }
                        return false;
                    case PHASE_3:
                        if(getGlobalCounter() >= PHASE3_START_ENDTIMER){ return true; }
                        return false;
                    case PHASE_4:
                        if(getGlobalCounter() >= PHASE4_START_ENDTIMER){ return true; }
                        return false;
                    default:
                        return false;
                }
            default: return false;
        }
    }
    // (SECTION 2) - Mechanics Information - Information about Nitori's fight.
        // Nitori is a Superboss, able to be fought by selecting the [Fight!] Option at her shop.
        // Nitori's shop only spawns in Gensokyo, you will not be able to find the shop in any other act.
        // However, if you wanted to test Nitori, you can do Fight Gensokyo:Nitori in the BaseMod console to fight her.
        // Nitori is a scaling boss, scaling in two ways:

        // 1): Nitori's health scales based on the progress of the run.
            // This means Nitori gains more health after killing the Act 1 boss, and after killing the Act 2 boss.
            // Nitori's health does not scale any further after Act 3.
            // (Nitori would skyrocket in terms of HP if she scaled on the boss-count after Act-3, and damage sponge bosses aren't fun, especially paired with endless mode's own scaling.)
            // Instead, if the player is not above Ascension 9, Nitori's base health is set to the A9 variant.

            // Information about Nitori's Health:
                // Pre Act 1 Boss: 470 Health. (497 Health [Ascension 9+])
                // Pre Act 2 Boss: 802 Health. (846 Health [Ascension 9+])
                // Pre Act 3 Boss: 1252 Health. (1330 Health [Ascension 9+])
                // Post Act 3 Boss: 1330 Health.

                // Fact: Nitori's health comes from the average health of the three bosses on the respective difficulty.
                // (Donu & Deca's health is calculated as the sum of two parts, same with the Awakened One's two phases.)

        // 2): Nitori's actions scale based on the progress of the run, and not based on Ascension.
            // Since Nitori is intended to be designed as an Ascension 20 Superboss, it is not necessary to make different alterations based on the ascension.
            // This is also a method to scale Nitori after clearing Act 3.
            // State-specific changes and Ascension-level changes are a nightmare to balance in tandem with each other.
            // (So, for the simplicity of this larger-than-average project, Nitori sticks to state-changes.)

    // SECTION 2.a) Nitori's turn pattern.
            // Enemies in Slay the Spire can be divided into three categories:
                // 1) Enemies who have pre-set turn patterns, and will never divert from that pattern.
                    // An example of this is Bronze Automaton, who will always start with Spawn Orbs, then repeats the following:
                        // Flail → Boost → Flail → Boost → HYPER BEAM → Stunned
                // 2) Enemies who can have some pre-set elements, but have elements that are influenced by the game's randomness.
                    // Most enemies follow this format, an example being Awakened One (Second Phase), who always starts with Dark Echo, then goes into a random pattern.
                    // These random patterns have restrictions placed upon them, as in the example given, where the Awakened One cannot use Tackle three times in a row and cannot use Sludge three times in a row.
                // 3) Enemies who are influenced by external factors.
                    // There is one example of this in the basegame, Writhing Mass (Who changes attacks by being hit by the player)

            // Nitori follows category #2, having initial preset elements, but has random elements to her fight.
            // With that in mind, this documentation will describe the process of Nitori's fight and the randomness contained.
            // (If you prefer diagrams, a flowchart is attached in this directory visualising everything in this section. (Gensokyo.rooms.nitori))

            // Before Nitori's turn is calculated, external conditions are checked referring to the fight's timer.
            // If the timer's conditions are met, Nitori will use a special intent, which ends the fight.
            // It's important to note that, using this intent or dealing fatal damage to the player, Nitori cannot kill the player, merely end the fight with the player on 1 HP.
            // (This does mean the player is vulnerable for the next fight ahead, causing some punishment for losing)

            // Action #1) OPENING -
                // Nitori's first move will always be this move.

                // Nitori will shuffle an amount of statuses from the status pool into the player's draw pile. (The status pool is displayed below, and cards are obtained in order from top to bottom.)
                    // Nitori will add 1 Status in Phase 1, 2 in Phase 2, 4 in Phase 3, and 5 in Phase 4.
                    // The list of statuses is the following:
                        // 1x Rushing Waters
                        // 1x Empty Salvo
                        // 1x EMP
                        // 1x Whirlpool
                        // 1x Self-Repair

                        // (Please see Section 2.b.b - Nitori's unique statuses to find out what they do.)

                // Nitori will also apply the player with several debuffs, including:
                    // 2 Weak -> 2 Overclocked Weak (Phase 4 Only)
                    // 2 Vulnerable -> 2 Overclocked Vulnerable (Phase 4 Only)
                    // 2 Frail (Phase 2 onwards)

                    // (Please see Section 2.b.d - Nitori's unique debuffs to find out what Overclocked debuffs do.)

            // Action #2) BUFF -
                // Nitori's second move will be always be this move.

                // Nitori will buff herself, giving her several quantities, varying on the phase. The buffs possible to be applied are the following:
                    // 1 Strength, 1 Artifact (Phase 1 Only)
                    // 2 Strength, 2 Artifact (Phase 2 Only)
                    // 2 Strength, Hydraulic Camouflage (Phase 3 Only)
                    // 3 Strength, Hydraulic Camouflage EX (Phase 4 Only)

                    // (Please see Section 2.b.c - Nitori's unique buffs.)

            // Action #3) ATTACK_STANDARD
                // This has a 50% chance to be Nitori's move on turn 3. If it is not Nitori's move on turn 3, it will be Nitori's move on turn 4. Nitori cannot use two ATTACK_STANDARDS in a row.
                // Nitori will attack the player, for the following damage:
                    // 9 Damage (Phase 1 Only)
                    // 13 Damage (Phase 2 Only)
                    // 17 Damage (Phase 3 Only)
                    // 21 Damage (Phase 4 Only)

            // Action #4) ATTACK_STANDARD_MULTIHIT
                // This has a 50% chance to be Nitori's move on turn 3. If it is not Nitori's move on turn 3, it will be Nitori's move on turn 4. Nitori cannot use two ATTACK_STANDARD_MULTIHITS in a row.
                // This attack will always hit twice.
                // Nitori will attack the player, for the following damage:
                    // 5 x 2 Damage (Phase 1 Only)
                    // 7 x 2 Damage (Phase 2 Only)
                    // 9 x 2 Damage (Phase 3 Only)
                    // 11 x 2 Damage (Phase 4 Only)

            // Nitori's actions will now follow a pseudo-random pattern, from Actions #5 to Actions #9.
            // However, there are some rules that prevent Nitori from having completely random outputs.
                // Nitori cannot use the same move twice. After using all of the moves once (Actions #5, #6, #7, #8 and #9), Nitori will jump to Action #10 on the next turn.
                // Nitori can only use Action #7 after Action #6 has been used in the previous turn.
                // Nitori can only use Action #8 after Action #5 or Action #7 have been used. Action #8's properties change depending on if Action #5 or Action #7 was used in the previous turn.
                // This means Nitori can only call upon Actions #5, #6 and #9 at the start of this sequence.
                // Checking for previous moves takes priority over rolling for an action to take.

            // Action #5) RNG_FRAIL
                // Nitori debuffs the player, including:
                    // 1 Frail, 1 Draw Reduction (Phase 1 Only)
                    // 1 Frail, 2 Draw Reduction (Phase 2 Only)
                    // 2 Frail, 2 Draw Reduction, 1 Less Energy next turn (Phase 3 Only)
                    // 2 Frail, 2 Draw Reduction, 1 Less Energy next turn, 1 Rushing Waves (Phase 4 Only)

            // Action #6) RNG_ATTACK_BLOCK
                // Nitori attacks the player, dealing:
                    // 7 Damage (Phase 1 Only)
                    // 11 Damage (Phase 2 Only)
                    // 15 Damage (Phase 3 Only)
                    // 19 Damage (Phase 4 Only)
                    // Nitori also gains block equal to the damage.

            // Action #7) RNG_TEMPHP
                // Nitori will gain Temp HP for herself:
                    // 25 Temp HP (Phase 1 Only)
                    // 35 Temp HP (Phase 2 Only)
                    // 50 Temp HP (Phase 3 Only)
                    // 75 Temp HP (Phase 4 Only)

            // Action #8) RNG_ATTACK_STANDARD / RNG_ATTACK_MULTIHIT
                // This action's actions vary depending on what move was used before.

                // If Action #5 was performed last turn, the following occurs:
                    // Nitori attacks the player.
                        // 5 Damage (Phase 1 Only)
                        // 9 Damage (Phase 2 Only)
                        // 13 Damage (Phase 3 Only)
                        // 17 Damage (Phase 4 Only)

                // If Action #7 was performed last turn, the following occurs:
                    // Nitori attacks the player. (This attack will always hit twice.)
                        // 3 x 2 Damage (Phase 1 Only)
                        // 5 x 2 Damage (Phase 2 Only)
                        // 7 x 2 Damage (Phase 3 Only)
                        // 9 x 2 Damage (Phase 4 Only)

                // Nitori shuffles an EMP into the draw pile.

            // Action #9) RNG_STR_DEX_DOWN
                // Nitori debuffs the player with the following:
                    // -1 Strength, -1 Dexterity (Phase 1 Only)
                    // -2 Strength, -2 Dexterity (Phase 2 Only)
                    // -3 Strength, -3 Dexterity (Phase 3 Only)
                    // -3 Strength, -3 Dexterity. Nitori gains +3 Strength, +3 Dexterity (Phase 4 Only)

            // Action #10) TURN_CYCLE_END
                // Nitori attacks the player.
                    // 25 Damage (Phase 1 Only)
                    // 35 Damage (Phase 2 Only)
                    // 45 Damage (Phase 3 Only)
                    // 55 Damage (Phase 4 Only)

                // Turn-trackers are reset to false.
                // Nitori's phase will be increased by 1.

    // SECTION 2.b.a) Nitori and the turn-timer.
        // Nitori operates on a turn-timer, which measures the time it takes for Nitori to get to and complete two cycles in Phase 4.
        // The turn the turn after the second Action #10 in Phase 4, Nitori's next intent will end the combat.
        // This means the amount of turns the player can take is the following:
            // Starting the battle at Phase 1: 50 Turns.
            // Starting the battle at Phase 2: 40 Turns.
            // Starting the battle at Phase 3: 30 Turns.
            // Starting the battle at Phase 4: 20 Turns.

        // (Because Nitori scales throughout the fight, it's likely the player will either die or beat Nitori in the timeframe, making this way of defeat unlikely.)

        // 2.b.b) Nitori's unique statuses
            // Nitori has 5 unique statuses which are used in the fight. These are:
                // 1x Rushing Waters - 5 Cost, When this card is drawn, reduce its cost by 1 this combat. NL Exhaust.
                // 1x Empty Salvo - At the end of your turn, add 3 Supersonic Missile to your discard pile. Ethereal (Supersonic Missile - At the end of your turn, take 3 damage. Increase this card's damage by 3 this combat)
                // 1x EMP - Whenever this card is drawn, lose 1 Energy and Draw 1 card less next turn. Ethereal.
                // 1x Whirlpool - Whenever this card is drawn, select 3 cards. They become Unplayable for 2 turns. Ethereal
                // 1x Self-Repair - Retain. Nitori gains 20 Block. When Retained, Nitori gains 5 Temporary HP. Increase the Tempory HP gained by 5 this combat.

        // 2.b.c) Nitori's unique buffs.
            // Hydraulic Camouflage - Every third debuff is negated.
            // Hydraulic Camouflage EX - Receives 10% reduced damage from ALL sources. Every other debuff is negated.

        // 2.b.d) Nitori's unique debuffs.
            // Overclocked Weak - Weak, but with 33%.
            // Overclocked Vulnerable - Vulnerable, but with 33%.

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TURN_LIMIT_REACHED:
                AbstractDungeon.overlayMenu.proceedButton.hide();
                NitoriStoreScreen.init();
                AbstractDungeon.topLevelEffects.clear();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.currMapNode.room = new EventRoom();
                AbstractDungeon.currMapNode.room.setMapImg(new Texture(makeUIPath("nitori.png")), new Texture(makeUIPath("nitori_outline.png")));
                AbstractDungeon.getCurrRoom().event = new CandidFailure();
                AbstractDungeon.getCurrRoom().event.reopen();
                CardCrawlGame.fadeIn(1.5F);
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
                AbstractDungeon.overlayMenu.hideCombatPanels();
                break;
            case OPENING:
                int statusAmount = 0;
                float offset = 0.2f;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                for (AbstractCard c : OPENING_STATUS_POOL) {
                    if (getState() == nitoriStates.PHASE_1 && !(statusAmount == PHASE1_OPENING_STATUS_AMOUNT)) {
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
                    } else if (getState() == nitoriStates.PHASE_2 && !(statusAmount == PHASE2_OPENING_STATUS_AMOUNT)) {
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
                    } else if (getState() == nitoriStates.PHASE_3 && !(statusAmount == PHASE3_OPENING_STATUS_AMOUNT)) {
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
                    } else if (getState() == nitoriStates.PHASE_4 && !(statusAmount == PHASE4_OPENING_STATUS_AMOUNT)) {
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
                    }
                    statusAmount++;
                    offset += 0.15f;
                }
                if (!(getState() == nitoriStates.PHASE_4)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
                } else {
                    // Todo: super weak / super vulnerable
                }
                if (!(getState() == nitoriStates.PHASE_1)) { AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT)); }
                OPENING_DONE = true;
                break;
            case BUFF:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE1_BUFF_STRENGTH_AMOUNT), PHASE1_BUFF_STRENGTH_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, PHASE1_BUFF_ARTIFACT_AMOUNT), PHASE1_BUFF_ARTIFACT_AMOUNT));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE2_BUFF_STRENGTH_AMOUNT), PHASE2_BUFF_STRENGTH_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, PHASE2_BUFF_ARTIFACT_AMOUNT), PHASE2_BUFF_ARTIFACT_AMOUNT));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE3_BUFF_STRENGTH_AMOUNT), PHASE3_BUFF_STRENGTH_AMOUNT));
                        // todo Hydraulic Camo
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE4_BUFF_STRENGTH_AMOUNT), PHASE4_BUFF_STRENGTH_AMOUNT));
                        // todo Hydraulic Camo EX
                        break;
                    default: break;
                }
                BUFF_DONE = true;
                break;
            case ATTACK_STANDARD_MULTIHIT:
                for (int i = 0; i < ATTACK_STANDARD_MULTIHIT_HITS; i++) {
                    switch (getState()) {
                        case PHASE_1:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        case PHASE_2:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        case PHASE_3:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            // todo Hydraulic Camo
                            break;
                        case PHASE_4:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            // todo Hydraulic Camo EX
                            break;
                        default: break;
                    }
                }
                ATTACK_STANDARD_MULTIHIT_DONE = true;
                break;
            case ATTACK_STANDARD:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    default: break;
                }
                ATTACK_STANDARD_DONE = true;
                break;
            case RNG_FRAIL:
                switch (getState()) {
                    // TODO energy reduction (All switches)
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE1_RNG_FRAIL_FRAIL_AMOUNT, true), PHASE1_RNG_FRAIL_FRAIL_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, PHASE1_RNG_FRAIL_DRAW_REDUCTION), PHASE1_RNG_FRAIL_DRAW_REDUCTION));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE2_RNG_FRAIL_FRAIL_AMOUNT, true), PHASE2_RNG_FRAIL_FRAIL_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, PHASE2_RNG_FRAIL_DRAW_REDUCTION), PHASE2_RNG_FRAIL_DRAW_REDUCTION));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE3_RNG_FRAIL_FRAIL_AMOUNT, true), PHASE3_RNG_FRAIL_FRAIL_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, PHASE3_RNG_FRAIL_DRAW_REDUCTION), PHASE3_RNG_FRAIL_DRAW_REDUCTION));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE4_RNG_FRAIL_FRAIL_AMOUNT, true), PHASE4_RNG_FRAIL_FRAIL_AMOUNT));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, PHASE4_RNG_FRAIL_DRAW_REDUCTION), PHASE4_RNG_FRAIL_DRAW_REDUCTION));

                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(PHASE4_RNG_FRAIL_STATUS, 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F));
                        break;
                    default: break;
                }
                RNG_FRAIL_DONE = true;
                break;
            case RNG_ATTACK_BLOCK:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(8).output));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(8), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(9).output));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(9), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(10).output));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(10), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(11).output));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(11), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    default: break;
                }
                RNG_ATTACK_BLOCK_DONE = true;
                break;
            case RNG_TEMPHP:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE1_RNG_TEMPHP_TEMPHP));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE2_RNG_TEMPHP_TEMPHP));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE3_RNG_TEMPHP_TEMPHP));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE4_RNG_TEMPHP_TEMPHP));
                        break;
                    default: break;
                }
                break;
            case RNG_ATTACK:
                if(this.lastMove(RNG_FRAIL)){
                    switch (getState()) {
                        case PHASE_1:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(12), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        case PHASE_2:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(13), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        case PHASE_3:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(14), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        case PHASE_4:
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(15), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            break;
                        default: break;
                    }
                }
                else {
                    for (int i = 0; i < RNG_ATTACK_MULTIHIT_HITS; i++) {
                        switch (getState()) {
                            case PHASE_1:
                                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(16), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                                break;
                            case PHASE_2:
                                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(17), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                                break;
                            case PHASE_3:
                                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(18), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                                break;
                            case PHASE_4:
                                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(19), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                                break;
                            default: break;
                        }
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(RNG_ATTACK_STANDARD_STATUS, 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F));
                RNG_ATTACK_DONE = true;
                break;
            case RNG_STR_DEX_DOWN:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE1_RNG_STR_DEX_DOWN), -PHASE1_RNG_STR_DEX_DOWN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE1_RNG_STR_DEX_DOWN), -PHASE1_RNG_STR_DEX_DOWN));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE2_RNG_STR_DEX_DOWN), -PHASE2_RNG_STR_DEX_DOWN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE2_RNG_STR_DEX_DOWN), -PHASE2_RNG_STR_DEX_DOWN));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE3_RNG_STR_DEX_DOWN), -PHASE3_RNG_STR_DEX_DOWN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE3_RNG_STR_DEX_DOWN), -PHASE3_RNG_STR_DEX_DOWN));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE4_RNG_STR_DEX_DRAIN), -PHASE4_RNG_STR_DEX_DRAIN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE4_RNG_STR_DEX_DRAIN), -PHASE4_RNG_STR_DEX_DRAIN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE4_RNG_STR_DEX_DRAIN), PHASE4_RNG_STR_DEX_DRAIN));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE4_RNG_STR_DEX_DRAIN), PHASE4_RNG_STR_DEX_DRAIN));
                        break;
                    default: break;
                }
                RNG_STR_DEX_DOWN_DONE = true;
                break;
            case TURN_CYCLE_END_ATTACK:
                switch (getState()) {
                    case PHASE_1:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(20), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_2:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(21), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_3:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(22), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    case PHASE_4:
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(23), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        break;
                    default: break;
                }
                OPENING_DONE = false;
                BUFF_DONE = false;
                ATTACK_STANDARD_MULTIHIT_DONE = false;
                ATTACK_STANDARD_DONE = false;
                RNG_FRAIL_DONE = false;
                RNG_ATTACK_BLOCK_DONE = false;
                RNG_TEMPHP_DONE = false;
                RNG_ATTACK_DONE = false;
                RNG_STR_DEX_DOWN_DONE = false;
                switch (getState()){
                    case PHASE_1:
                        setState(nitoriStates.PHASE_2);
                        break;
                    case PHASE_2:
                        setState(nitoriStates.PHASE_3);
                        break;
                    case PHASE_3:
                        setState(nitoriStates.PHASE_4);
                        break;
                    default: break;
                }
            default: break;
        }
        incrementGlobalCounter();
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    @Override
    protected void getMove(int i) {
        if(getBattleEnd()) { this.setMove(TURN_LIMIT_REACHED, Intent.MAGIC); }
        else if(!(OPENING_DONE)){ this.setMove(OPENING, Intent.STRONG_DEBUFF); }
        else if(!(BUFF_DONE)){ this.setMove(BUFF, Intent.BUFF); }
        else if(!(ATTACK_STANDARD_MULTIHIT_DONE) && (i <= 49 || lastMove(ATTACK_STANDARD))){
            switch (getState()) {
                case PHASE_1:
                    this.setMove(ATTACK_STANDARD_MULTIHIT, Intent.ATTACK, (this.damage.get(0)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_2:
                    this.setMove(ATTACK_STANDARD_MULTIHIT, Intent.ATTACK, (this.damage.get(1)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_3:
                    this.setMove(ATTACK_STANDARD_MULTIHIT, Intent.ATTACK, (this.damage.get(2)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_4:
                    this.setMove(ATTACK_STANDARD_MULTIHIT, Intent.ATTACK, (this.damage.get(3)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                default: break;
            }
        }
        else if(!(ATTACK_STANDARD_DONE) && (i >= 49 || lastMove(ATTACK_STANDARD_MULTIHIT))){
            switch (getState()) {
                case PHASE_1:
                    this.setMove(ATTACK_STANDARD, Intent.ATTACK, (this.damage.get(4)).base);
                    break;
                case PHASE_2:
                    this.setMove(ATTACK_STANDARD, Intent.ATTACK, (this.damage.get(5)).base);
                    break;
                case PHASE_3:
                    this.setMove(ATTACK_STANDARD, Intent.ATTACK, (this.damage.get(6)).base);
                    break;
                case PHASE_4:
                    this.setMove(ATTACK_STANDARD, Intent.ATTACK, (this.damage.get(7)).base);
                    break;
                default: break;
            }
        }
        else if(!(RNG_ATTACK_DONE) && lastMove(RNG_FRAIL) || lastMove(RNG_TEMPHP)){
            if(lastMove(RNG_FRAIL)){
                switch (getState()) {
                    case PHASE_1:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(12)).base);
                        break;
                    case PHASE_2:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(13)).base);
                        break;
                    case PHASE_3:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(14)).base);
                        break;
                    case PHASE_4:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(15)).base);
                        break;
                    default: break;
                }
            }
            else {
                switch (getState()) {
                    case PHASE_1:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(16)).base, RNG_ATTACK_MULTIHIT_HITS, true);
                        break;
                    case PHASE_2:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(17)).base, RNG_ATTACK_MULTIHIT_HITS, true);
                        break;
                    case PHASE_3:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(18)).base, RNG_ATTACK_MULTIHIT_HITS, true);
                        break;
                    case PHASE_4:
                        this.setMove(RNG_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(19)).base, RNG_ATTACK_MULTIHIT_HITS, true);
                        break;
                    default: break;
                }
            }
        }
        else if(!(RNG_TEMPHP_DONE) && lastMove(RNG_ATTACK_BLOCK)){ this.setMove(RNG_TEMPHP, IntentEnums.TEMPHP); }
        else if (!(RNG_ATTACK_BLOCK_DONE) && i < 33) {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(8)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(9)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(10)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(11)).base);
                    break;
                default: break;
            }
        }
        else if(!(RNG_STR_DEX_DOWN_DONE) && i >= 33 & i < 66){
            switch (getState()) {
                case PHASE_1:
                case PHASE_2:
                case PHASE_3:
                case PHASE_4:
                    this.setMove(RNG_STR_DEX_DOWN, Intent.DEBUFF);
                    break;
                default: break;
            }
        }
        else if(!(RNG_FRAIL_DONE) && i >= 66 & i <= 99){
            switch (getState()) {
                case PHASE_1:
                case PHASE_2:
                case PHASE_3:
                case PHASE_4:
                    this.setMove(RNG_FRAIL, Intent.STRONG_DEBUFF);
                    break;
                default: break;
            }
        }
        else if (!(RNG_ATTACK_BLOCK_DONE)) {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(8)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(9)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(10)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(11)).base);
                    break;
                default: break;
            }
        }
        else if(!(RNG_STR_DEX_DOWN_DONE)){
            switch (getState()) {
                case PHASE_1:
                case PHASE_2:
                case PHASE_3:
                case PHASE_4:
                    this.setMove(RNG_STR_DEX_DOWN, Intent.DEBUFF);
                    break;
                default: break;
            }
        }
        else if(!(RNG_FRAIL_DONE)){
            switch (getState()) {
                case PHASE_1:
                case PHASE_2:
                case PHASE_3:
                case PHASE_4:
                    this.setMove(RNG_FRAIL, Intent.STRONG_DEBUFF);
                    break;
                default: break;
            }
        }
        else {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(20)).base);
                    break;
                case PHASE_2:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(21)).base);
                    break;
                case PHASE_3:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(22)).base);
                    break;
                case PHASE_4:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(23)).base);
                    break;
                default: break;
            }
        }
    }
    @Override
    public void render(SpriteBatch sb){
        sb.setColor(Color.WHITE.cpy());
        sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X + 150F, DRAW_Y, 512.0F * Settings.scale, 512.0F * Settings.scale);
        sb.setColor(Color.WHITE.cpy());
        super.render(sb);
    }
    //Runs a specific animation
    public void runAnim(String animation) { ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation); }
    //Resets character back to idle animation
    public void resetAnimation() { ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Idle"); }
    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }
    public class NitoriListener implements Player.PlayerListener {
        private Nitori character;
        public NitoriListener(Nitori character) {
            this.character = character;
        }
        // Checks animation state
        public void animationFinished(Animation animation){
            if (animation.name.equals("Defeat")) { character.stopAnimation();
            } else if (!animation.name.equals("Idle")) { character.resetAnimation(); }
        }
        //UNUSED
        public void animationChanged(Animation var1, Animation var2){ }
        //UNUSED
        public void preProcess(Player var1){ }
        //UNUSED
        public void postProcess(Player var1){ }
        //UNUSED
        public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){ }
    }
}
