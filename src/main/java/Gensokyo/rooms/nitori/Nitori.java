package Gensokyo.rooms.nitori;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.cards.EMP;
import Gensokyo.cards.FlashFlood;
import Gensokyo.cards.SelfRepair;
import Gensokyo.cards.TorpedoBarrage;
import Gensokyo.cards.Whirlpool;
import Gensokyo.powers.extra.HydraulicCamouflage;
import Gensokyo.powers.extra.NitoriTimer;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;

import static Gensokyo.GensokyoMod.makeID;
import static Gensokyo.GensokyoMod.makeUIPath;
import static com.megacrit.cardcrawl.shop.Merchant.DRAW_X;
import static com.megacrit.cardcrawl.shop.Merchant.DRAW_Y;

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
    private static final int HP_0BOSS = (250 + 140 + 240) / 3;
    private static final int HP_0BOSS_A9 = (264 + 150 + 250) / 3;
    // HP information for Nitori (PHASE_2)
    private static final int HP_1BOSS = (420 + 282 + 300) / 3;
    private static final int HP_1BOSS_A9 = (440 + 300 + 320) / 3;
    // HP information for Nitori (PHASE_3) && PHASE_4 (HP_2BOSS_A9)
    private static final int HP_2BOSS = ((250 * 2) + (300 * 2) + 456) / 3;
    private static final int HP_2BOSS_A9 = ((265 * 2) + (320 * 2) + 480) / 3;
    // Move Bytes for Nitori.
    // These moves often perform similar actions, but vary in interaction between phases.
    private static final byte OPENING = 1;
    private static final byte BUFF = 2;
    private static final byte ATTACK_STANDARD_MULTIHIT = 3;
    private static final byte ATTACK_STANDARD = 4;
    private static final byte RNG_FRAIL_ATTACK = 5;
    private static final byte RNG_ATTACK_BLOCK = 6;
    private static final byte RNG_TEMPHP = 7;
    private static final byte RNG_ATTACK = 8;
    private static final byte RNG_STR_DEX_DOWN = 9;
    private static final byte TURN_CYCLE_END_ATTACK = 10;
    private static final byte TURN_LIMIT_REACHED = 99;
    // Move Characteristics for Nitori, ordered by move-byte appearance (See Above)
    // OPENING
    private static final AbstractCard[] OPENING_STATUS_POOL = {new FlashFlood(), new TorpedoBarrage(), new EMP(), new Whirlpool(), new SelfRepair()};
    private static final int PHASE1_OPENING_STATUS_AMOUNT = 1;
    private static final int PHASE2_OPENING_STATUS_AMOUNT = 2;
    private static final int PHASE3_OPENING_STATUS_AMOUNT = 4;
    private static final int PHASE4_OPENING_STATUS_AMOUNT = 5;
    private static final int OPENING_DEBUFF_AMOUNT = 2;
    private static final int PHASE4_OPENING_DEBUFF_AMOUNT = 3;
    // BUFF
    private static final int PHASE1_BUFF_STRENGTH_AMOUNT = 1;
    private static final int PHASE2_BUFF_STRENGTH_AMOUNT = 2;
    private static final int PHASE3_BUFF_STRENGTH_AMOUNT = 2;
    private static final int PHASE4_BUFF_STRENGTH_AMOUNT = 3;
    private static final int PHASE1_BUFF_ARTIFACT_AMOUNT = 1;
    private static final int PHASE2_BUFF_ARTIFACT_AMOUNT = 2;
    // ATTACK_STANDARD_MULTIHIT
    private static final int PHASE1_ATTACK_STANDARD_MULTIHIT_DAMAGE = 4;
    private static final int PHASE2_ATTACK_STANDARD_MULTIHIT_DAMAGE = 6;
    private static final int PHASE3_ATTACK_STANDARD_MULTIHIT_DAMAGE = 8;
    private static final int PHASE4_ATTACK_STANDARD_MULTIHIT_DAMAGE = 10;
    private static final int ATTACK_STANDARD_MULTIHIT_HITS = 2;
    // ATTACK_STANDARD
    private static final int PHASE1_ATTACK_STANDARD_DAMAGE = 8;
    private static final int PHASE2_ATTACK_STANDARD_DAMAGE = 12;
    private static final int PHASE3_ATTACK_STANDARD_DAMAGE = 16;
    private static final int PHASE4_ATTACK_STANDARD_DAMAGE = 20;
    // RNG_FRAIL_ATTACK
    private static final int PHASE1_RNG_FRAIL_ATTACK_FRAIL_AMOUNT = 1;
    private static final int PHASE2_RNG_FRAIL_ATTACK_FRAIL_AMOUNT = 2;
    private static final int PHASE3_RNG_FRAIL_ATTACK_FRAIL_AMOUNT = 3;
    private static final int PHASE4_RNG_FRAIL_ATTACK_FRAIL_AMOUNT = 3;
    private static final AbstractCard PHASE4_RNG_FRAIL_ATTACK_STATUS = new FlashFlood();
    // RNG_TEMPHP
    private static final int PHASE1_RNG_TEMPHP_TEMPHP = 20;
    private static final int PHASE2_RNG_TEMPHP_TEMPHP = 30;
    private static final int PHASE3_RNG_TEMPHP_TEMPHP = 45;
    private static final int PHASE4_RNG_TEMPHP_TEMPHP = 75;
    // RNG_STR_DEX_DOWN
    private static final int PHASE1_RNG_STR_DEX_DOWN = 1;
    private static final int PHASE2_RNG_STR_DEX_DOWN = 2;
    private static final int PHASE3_RNG_STR_DEX_DOWN = 3;
    private static final int PHASE4_RNG_STR_DEX_DRAIN = 3;
    // TURN_CYCLE_END_ATTACK
    private static final int PHASE1_TURN_CYCLE_END_ATTACK_DAMAGE = 20;
    private static final int PHASE2_TURN_CYCLE_END_ATTACK_DAMAGE = 30;
    private static final int PHASE3_TURN_CYCLE_END_ATTACK_DAMAGE = 40;
    private static final int PHASE4_TURN_CYCLE_END_ATTACK_DAMAGE = 50;
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
            PHASE1_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE2_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE3_TURN_CYCLE_END_ATTACK_DAMAGE,
            PHASE4_TURN_CYCLE_END_ATTACK_DAMAGE
    };
    // Tracks the Artificial-Turn-Timer
    private static nitoriStates beginningState = nitoriStates.PHASE_1;
    private static nitoriStates currentState = nitoriStates.PHASE_1;
    private int globalCounter = 0;
    private static final int PHASE1_START_ENDTIMER = 25;
    private static final int PHASE2_START_ENDTIMER = 35;
    private static final int PHASE3_START_ENDTIMER = 45;
    private static final int PHASE4_START_ENDTIMER = 55;
    // Tracks how Nitori should move.
    private boolean BATTLE_OVER = false;
    private boolean STATUSES_DONE = false;
    private boolean OPENING_DONE = false;
    private boolean BUFF_DONE = false;
    private boolean ATTACK_STANDARD_MULTIHIT_DONE = false;
    private boolean ATTACK_STANDARD_DONE = false;
    private boolean RNG_FRAIL_ATTACK_DONE = false;
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
    public void incrementGlobalCounter(){ globalCounter++; }
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

                // Nitori will shuffle an amount of statuses from the status pool into the player's discard pile (draw pile on Phase 3+) . (The status pool is displayed below, and cards are obtained in order from top to bottom.)
                    // Nitori will add 1 Status in Phase 1, 2 in Phase 2, 4 in Phase 3, and 5 in Phase 4.
                    // The list of statuses is the following:
                        // 1x Flash Flood
                        // 1x Torpedo Barrage
                        // 1x EMP
                        // 1x Whirlpool
                        // 1x Self-Repair
                        // (Please see Section 2.b.b - Nitori's unique statuses to find out what they do.)
                        // (Statuses will only be applied on the first OPENING move.)

                // Nitori will also apply the player with several debuffs, including:
                    // 2 Weak (Phase 1+) -> 3 Weak (Phase 4 Only)
                    // 2 Vulnerable (Phase 2+) -> 3 Vulnerable (Phase 4 Only)
                    // 2 Frail (Phase 3+) -> 3 Frail (Phase 4 Only)

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
                    // 8 Damage (Phase 1 Only)
                    // 12 Damage (Phase 2 Only)
                    // 16 Damage (Phase 3 Only)
                    // 20 Damage (Phase 4 Only)

            // Action #4) ATTACK_STANDARD_MULTIHIT
                // This has a 50% chance to be Nitori's move on turn 3. If it is not Nitori's move on turn 3, it will be Nitori's move on turn 4. Nitori cannot use two ATTACK_STANDARD_MULTIHITS in a row.
                // This attack will always hit twice.
                // Nitori will attack the player, for the following damage:
                    // 4 x 2 Damage (Phase 1 Only)
                    // 6 x 2 Damage (Phase 2 Only)
                    // 8 x 2 Damage (Phase 3 Only)
                    // 10 x 2 Damage (Phase 4 Only)

            // Nitori's actions will now follow a pseudo-random pattern, from Actions #5 to Actions #9.
            // However, there are some rules that prevent Nitori from having completely random outputs.
                // Nitori cannot use the same move twice. After using all of the moves once (Actions #5, #6, #7, #8 and #9), Nitori will jump to Action #10 on the next turn.
                // Nitori can only use Action #7 after Action #6 has been used in the previous turn.
                // Nitori can only use Action #8 after Action #5 or Action #7 have been used. Action #8's properties change depending on if Action #5 or Action #7 was used in the previous turn.
                // This means Nitori can only call upon Actions #5, #6 and #9 at the start of this sequence.
                // Checking for previous moves takes priority over rolling for an action to take.

            // Action #5) RNG_FRAIL_ATTACK
                // Same damage as ATTACK_STANDARD, but also applies:
                    // 1 Frail (Phase 1)
                    // 2 Frail (Phase 2)
                    // 3 Frail (Phase 3)
                    // 3 Frail, 1 Flash Flood (Phase 4 Only)

            // Action #6) RNG_ATTACK_BLOCK
                // Nitori attacks the player, dealing the same damage as ATTACK_STANDARD.
                // Nitori also gains block equal to the damage.

            // Action #7) RNG_TEMPHP
                // Nitori will gain Temp HP for herself:
                    // 25 Temp HP (Phase 1 Only)
                    // 35 Temp HP (Phase 2 Only)
                    // 50 Temp HP (Phase 3 Only)
                    // 75 Temp HP (Phase 4 Only)

            // Action #8) RNG_ATTACK_STANDARD / RNG_ATTACK_MULTIHIT
                // A multihit with the same damage as ATTACK_STANDARD_MULTIHIT

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
            // Starting the battle at Phase 1: 25 Turns.
            // Starting the battle at Phase 2: 35 Turns.
            // Starting the battle at Phase 3: 45 Turns.
            // Starting the battle at Phase 4: 55 Turns.

        // (Because Nitori scales throughout the fight, it's likely the player will either die or beat Nitori in the timeframe, making this way of defeat unlikely.)

        // 2.b.b) Nitori's unique statuses
            // Nitori has 5 unique statuses which are used in the fight. These are:
                // 1x Flash Flood - 5 Cost, When this card is drawn, reduce its cost by 1 this combat. NL Exhaust.
                // 1x Torpedo Barrage - At the end of your turn, add 3 Oxygen Torpedo to your discard pile. Ethereal (Oxygen Torpedo - At the end of your turn, take 3 damage. Increase this card's damage by 3 this combat)
                // 1x EMP - Whenever this card is drawn, lose 1 Energy and Draw 1 card less next turn. Ethereal.
                // 1x Whirlpool - 1 Cost, Whenever this card is in your hand, you cannot play Skills.
                // 1x Self-Repair - Retain. Nitori gains 20 Block. When Retained, Nitori gains 5 Temporary HP. Increase the Tempory HP gained by 5 this combat.

        // 2.b.c) Nitori's unique buffs.
            // Hydraulic Camouflage - Every other debuff is negated.
            // Hydraulic Camouflage EX - Receives 10% reduced damage from ALL sources. Every other debuff is negated.

    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBGM("こころ.ogg");
        int powerAmount;
        switch (getBeginningState()){
            case PHASE_1:
                powerAmount = PHASE1_START_ENDTIMER;
                break;
            case PHASE_2:
                powerAmount = PHASE2_START_ENDTIMER;
                break;
            case PHASE_3:
                powerAmount = PHASE3_START_ENDTIMER;
                break;
            case PHASE_4:
                powerAmount = PHASE4_START_ENDTIMER;
                break;
            default:
                powerAmount = 50;
        }
        addToBot(new ApplyPowerAction(this, this, new NitoriTimer(this, powerAmount)));
    }
    @Override
    public void takeTurn() {
//        switch (this.nextMove) {
//            case TURN_LIMIT_REACHED:
//                BATTLE_OVER = true;
//                AbstractDungeon.overlayMenu.proceedButton.hide();
//                NitoriStoreScreen.init();
//                AbstractDungeon.topLevelEffects.clear();
//                AbstractDungeon.effectList.clear();
//                AbstractDungeon.currMapNode.room = new EventRoom();
//                AbstractDungeon.currMapNode.room.setMapImg(new Texture(makeUIPath("nitori.png")), new Texture(makeUIPath("nitori_outline.png")));
//                AbstractDungeon.getCurrRoom().event = new CandidFailure();
//                AbstractDungeon.getCurrRoom().event.reopen();
//                CardCrawlGame.fadeIn(1.5F);
//                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
//                AbstractDungeon.overlayMenu.hideCombatPanels();
//                break;
//            case OPENING:
//                int statusAmount = 0;
//                float offset = 0.2f;
//                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
//                if(!(STATUSES_DONE)) {
//                    for (AbstractCard c : OPENING_STATUS_POOL) {
//                        if (getState() == nitoriStates.PHASE_1 && statusAmount != PHASE1_OPENING_STATUS_AMOUNT) {
//                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
//                            statusAmount++;
//                        } else if (getState() == nitoriStates.PHASE_2 && statusAmount != PHASE2_OPENING_STATUS_AMOUNT) {
//                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
//                            statusAmount++;
//                        } else if (getState() == nitoriStates.PHASE_3 && statusAmount != PHASE3_OPENING_STATUS_AMOUNT) {
//                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
//                            statusAmount++;
//                        } else if (getState() == nitoriStates.PHASE_4 && statusAmount != PHASE4_OPENING_STATUS_AMOUNT) {
//                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false, Settings.WIDTH * offset, Settings.HEIGHT / 2.0F));
//                            statusAmount++;
//                        }
//                    }
//                }
//                switch (getState()){
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, OPENING_DEBUFF_AMOUNT, true), OPENING_DEBUFF_AMOUNT));
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, PHASE4_OPENING_DEBUFF_AMOUNT, true), PHASE4_OPENING_DEBUFF_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, PHASE4_OPENING_DEBUFF_AMOUNT, true), PHASE4_OPENING_DEBUFF_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE4_OPENING_DEBUFF_AMOUNT, true), PHASE4_OPENING_DEBUFF_AMOUNT));
//                        break;
//                    default: break;
//                }
//                STATUSES_DONE = true;
//                OPENING_DONE = true;
//                break;
//            case BUFF:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE1_BUFF_STRENGTH_AMOUNT), PHASE1_BUFF_STRENGTH_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, PHASE1_BUFF_ARTIFACT_AMOUNT), PHASE1_BUFF_ARTIFACT_AMOUNT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE2_BUFF_STRENGTH_AMOUNT), PHASE2_BUFF_STRENGTH_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, PHASE2_BUFF_ARTIFACT_AMOUNT), PHASE2_BUFF_ARTIFACT_AMOUNT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE3_BUFF_STRENGTH_AMOUNT), PHASE3_BUFF_STRENGTH_AMOUNT));
//                        boolean hasHydro = false;
//                        for(AbstractPower p: this.powers){
//                            if(p instanceof HydraulicCamouflage){
//                                hasHydro = true;
//                                break;
//                            }
//                        }
//                        if(!(hasHydro)){ AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HydraulicCamouflage(this, false))); }
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE4_BUFF_STRENGTH_AMOUNT), PHASE4_BUFF_STRENGTH_AMOUNT));
//                        boolean hydroCheck = false;
//                        for(AbstractPower p: this.powers){
//                            if(p instanceof HydraulicCamouflage){
//                                if(!((HydraulicCamouflage) p).returnIsExtra()){ ((HydraulicCamouflage) p).extraTransition(); }
//                                hydroCheck = true;
//                                break;
//                            }
//                        }
//                        if(!(hydroCheck)){ AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HydraulicCamouflage(this, true))); }
//                        break;
//                    default: break;
//                }
//                BUFF_DONE = true;
//                break;
//            case ATTACK_STANDARD_MULTIHIT:
//                for (int i = 0; i < ATTACK_STANDARD_MULTIHIT_HITS; i++) {
//                    switch (getState()) {
//                        case PHASE_1:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_2:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_3:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_4:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        default: break;
//                    }
//                }
//                ATTACK_STANDARD_MULTIHIT_DONE = true;
//                break;
//            case ATTACK_STANDARD:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    default: break;
//                }
//                ATTACK_STANDARD_DONE = true;
//                break;
//            case RNG_FRAIL_ATTACK:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    default: break;
//                }
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE1_RNG_FRAIL_ATTACK_FRAIL_AMOUNT, true), PHASE1_RNG_FRAIL_ATTACK_FRAIL_AMOUNT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE2_RNG_FRAIL_ATTACK_FRAIL_AMOUNT, true), PHASE2_RNG_FRAIL_ATTACK_FRAIL_AMOUNT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE3_RNG_FRAIL_ATTACK_FRAIL_AMOUNT, true), PHASE3_RNG_FRAIL_ATTACK_FRAIL_AMOUNT));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, PHASE4_RNG_FRAIL_ATTACK_FRAIL_AMOUNT, true), PHASE4_RNG_FRAIL_ATTACK_FRAIL_AMOUNT));
//                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(PHASE4_RNG_FRAIL_ATTACK_STATUS, 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F));
//                        break;
//                    default: break;
//                }
//                RNG_FRAIL_ATTACK_DONE = true;
//                break;
//            case RNG_ATTACK_BLOCK:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(4).output));
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(5).output));
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(6).output));
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.damage.get(7).output));
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    default: break;
//                }
//                RNG_ATTACK_BLOCK_DONE = true;
//                break;
//            case RNG_TEMPHP:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE1_RNG_TEMPHP_TEMPHP));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE2_RNG_TEMPHP_TEMPHP));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE3_RNG_TEMPHP_TEMPHP));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this, this, PHASE4_RNG_TEMPHP_TEMPHP));
//                        break;
//                    default: break;
//                }
//                break;
//            case RNG_ATTACK:
//                for (int i = 0; i < ATTACK_STANDARD_MULTIHIT_HITS; i++) {
//                    switch (getState()) {
//                        case PHASE_1:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_2:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_3:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        case PHASE_4:
//                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                            break;
//                        default: break;
//                    }
//                }
//                RNG_ATTACK_DONE = true;
//                break;
//            case RNG_STR_DEX_DOWN:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE1_RNG_STR_DEX_DOWN), -PHASE1_RNG_STR_DEX_DOWN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -PHASE1_RNG_STR_DEX_DOWN), -PHASE1_RNG_STR_DEX_DOWN));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE2_RNG_STR_DEX_DOWN), -PHASE2_RNG_STR_DEX_DOWN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -PHASE2_RNG_STR_DEX_DOWN), -PHASE2_RNG_STR_DEX_DOWN));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE3_RNG_STR_DEX_DOWN), -PHASE3_RNG_STR_DEX_DOWN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -PHASE3_RNG_STR_DEX_DOWN), -PHASE3_RNG_STR_DEX_DOWN));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -PHASE4_RNG_STR_DEX_DRAIN), -PHASE4_RNG_STR_DEX_DRAIN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -PHASE4_RNG_STR_DEX_DRAIN), -PHASE4_RNG_STR_DEX_DRAIN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, PHASE4_RNG_STR_DEX_DRAIN), PHASE4_RNG_STR_DEX_DRAIN));
//                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DexterityPower(this, PHASE4_RNG_STR_DEX_DRAIN), PHASE4_RNG_STR_DEX_DRAIN));
//                        break;
//                    default: break;
//                }
//                RNG_STR_DEX_DOWN_DONE = true;
//                break;
//            case TURN_CYCLE_END_ATTACK:
//                switch (getState()) {
//                    case PHASE_1:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(8), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_2:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(9), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_3:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(10), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    case PHASE_4:
//                        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(11), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                        break;
//                    default: break;
//                }
//                OPENING_DONE = false;
//                BUFF_DONE = false;
//                ATTACK_STANDARD_MULTIHIT_DONE = false;
//                ATTACK_STANDARD_DONE = false;
//                RNG_FRAIL_ATTACK_DONE = false;
//                RNG_ATTACK_BLOCK_DONE = false;
//                RNG_TEMPHP_DONE = false;
//                RNG_ATTACK_DONE = false;
//                RNG_STR_DEX_DOWN_DONE = false;
//            default: break;
//        }
//        if(!BATTLE_OVER){ AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this)); }
//        incrementGlobalCounter();
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
        else if(!(RNG_ATTACK_DONE) && (lastMove(RNG_FRAIL_ATTACK) || lastMove(RNG_TEMPHP))){
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_ATTACK, Intent.ATTACK, (this.damage.get(0)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_2:
                    this.setMove(RNG_ATTACK, Intent.ATTACK, (this.damage.get(1)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_3:
                    this.setMove(RNG_ATTACK, Intent.ATTACK, (this.damage.get(2)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                case PHASE_4:
                    this.setMove(RNG_ATTACK, Intent.ATTACK, (this.damage.get(3)).base, ATTACK_STANDARD_MULTIHIT_HITS, true);
                    break;
                default: break;
            }
        }
        else if(!(RNG_TEMPHP_DONE) && lastMove(RNG_ATTACK_BLOCK)){ this.setMove(RNG_TEMPHP, IntentEnums.TEMPHP); }
        else if (!(RNG_ATTACK_BLOCK_DONE) && i < 33) {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(4)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(5)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(6)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(7)).base);
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
        else if(!(RNG_FRAIL_ATTACK_DONE) && i >= 66 & i <= 99){
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(4)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(5)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(6)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(7)).base);
                    break;
                default: break;
            }
        }
        else if (!(RNG_ATTACK_BLOCK_DONE)) {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(4)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(5)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(6)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_ATTACK_BLOCK, Intent.ATTACK_DEFEND, (this.damage.get(7)).base);
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
        else if(!(RNG_FRAIL_ATTACK_DONE)){
            switch (getState()) {
                case PHASE_1:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(4)).base);
                    break;
                case PHASE_2:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(5)).base);
                    break;
                case PHASE_3:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(6)).base);
                    break;
                case PHASE_4:
                    this.setMove(RNG_FRAIL_ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(7)).base);
                    break;
                default: break;
            }
        }
        else {
            switch (getState()) {
                case PHASE_1:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(8)).base);
                    break;
                case PHASE_2:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(9)).base);
                    break;
                case PHASE_3:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(10)).base);
                    break;
                case PHASE_4:
                    this.setMove(TURN_CYCLE_END_ATTACK, Intent.ATTACK, (this.damage.get(11)).base);
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
