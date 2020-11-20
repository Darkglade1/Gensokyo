//package Gensokyo.monsters.act3;
//
//import Gensokyo.BetterSpriterAnimation;
//import Gensokyo.powers.act3.TimeDilation;
//import Gensokyo.util.PreviewIntent;
//import basemod.abstracts.CustomMonster;
//import com.brashmonkey.spriter.Animation;
//import com.brashmonkey.spriter.Player;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.animations.TalkAction;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.common.DamageAction;
//import com.megacrit.cardcrawl.actions.common.GainBlockAction;
//import com.megacrit.cardcrawl.actions.common.RollMoveAction;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.localization.MonsterStrings;
//import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
//import com.megacrit.cardcrawl.powers.StrengthPower;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Sakuya extends CustomMonster
//{
//    public static final String ID = "Gensokyo:Sakuya";
//    private static final MonsterStrings monsterStrings;
//    public static final String NAME;
//    public static final String[] MOVES;
//    public static final String[] DIALOG;
//    private boolean firstMove = true;
//
//    public static final byte BIG_ATTACK = 0;
//    public static final byte BLOCK_BUFF = 1;
//    public static final byte DEBUFF = 2;
//    public static final byte MULTI_ATTACK = 3;
//
//    private static final int MULTI_ATTACK_DAMAGE = 7;
//    private static final int A3_MULTI_ATTACK_DAMAGE = 8;
//    private static final int HITS = 3;
//
//    private static final int BIG_ATTACK_DAMAGE = 50;
//    private static final int A3_BIG_ATTACK_DAMAGE = 55;
//
//    private static final int STRENGTH = 3;
//    private static final int A18_STRENGTH = 4;
//
//    private static final int BLOCK = 20;
//    private static final int A8_BLOCK = 24;
//
//    private static final int STATUS_AMT = 5;
//    private static final int A18_STATUS_AMT = 7;
//
//    private static final int HP_MIN = 195;
//    private static final int HP_MAX = 200;
//    private static final int A8_HP_MIN = 210;
//    private static final int A8_HP_MAX = 215;
//    private int multiAttackDamage;
//    private int bigAttackDamage;
//    private int block;
//    private int strength;
//    private int status;
//    private EnemyMoveInfo secondMove;
//    private PreviewIntent secondIntent;
//
//    private Map<Byte, EnemyMoveInfo> moves;
//
//    public Sakuya() {
//        this(0.0f, 0.0f);
//    }
//
//    public Sakuya(final float x, final float y) {
//        super(Sakuya.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 245.0f, null, x, y);
//        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sakuya/Spriter/SakuyaAnimation.scml");
//        this.type = EnemyType.ELITE;
//        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
//        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
//        if (AbstractDungeon.ascensionLevel >= 18) {
//            strength = A18_STRENGTH;
//            status = A18_STATUS_AMT;
//        } else {
//            strength = STRENGTH;
//            status = STATUS_AMT;
//        }
//        if (AbstractDungeon.ascensionLevel >= 8) {
//            this.setHp(A8_HP_MIN, A8_HP_MAX);
//            this.block = A8_BLOCK;
//        } else {
//            this.setHp(HP_MIN, HP_MAX);
//            this.block = BLOCK;
//        }
//
//        if (AbstractDungeon.ascensionLevel >= 3) {
//            this.multiAttackDamage = A3_MULTI_ATTACK_DAMAGE;
//            this.bigAttackDamage = A3_BIG_ATTACK_DAMAGE;
//        } else {
//            this.multiAttackDamage = MULTI_ATTACK_DAMAGE;
//            this.bigAttackDamage = BIG_ATTACK_DAMAGE;
//        }
//
//        this.moves = new HashMap<>();
//        this.moves.put(BIG_ATTACK, new EnemyMoveInfo(BIG_ATTACK, Intent.ATTACK, this.multiAttackDamage, HITS, true));
//        this.moves.put(BLOCK_BUFF, new EnemyMoveInfo(BLOCK_BUFF, Intent.ATTACK_DEFEND, this.bigAttackDamage, 0, false));
//        this.moves.put(DEBUFF, new EnemyMoveInfo(DEBUFF, Intent.BUFF, -1, 0, false));
//
//        Player.PlayerListener listener = new SakuyaListener(this);
//        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
//    }
//
//    @Override
//    public void usePreBattleAction() {
//        //AbstractDungeon.getCurrRoom().playBgmInstantly("Bhavagra");
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeDilation(this)));
//    }
//
//    @Override
//    public void takeTurn() {
//        if (this.firstMove) {
//            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
//            firstMove = false;
//        }
//        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
//        if(info.base > -1) {
//            info.applyPowers(this, AbstractDungeon.player);
//        }
//        switch (this.nextMove) {
//            case BIG_ATTACK: {
//                //runAnim("Attack");
//                for (int i = 0; i < HITS; i++) {
//                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
//                }
//                break;
//            }
//            case BLOCK_BUFF: {
//                //runAnim("Attack");
//                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
//                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
//                break;
//            }
//            case DEBUFF: {
//                //runAnim("Spell");
//                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
//                break;
//            }
//        }
//        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
//    }
//
//    @Override
//    protected void getMove(final int num) {
//        this.setMoveShortcut(BIG_ATTACK);
//    }
//
//    public void setMoveShortcut(byte next) {
//        EnemyMoveInfo info = this.moves.get(next);
//        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
//    }
//
//    static {
//        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
//        NAME = Sakuya.monsterStrings.NAME;
//        MOVES = Sakuya.monsterStrings.MOVES;
//        DIALOG = Sakuya.monsterStrings.DIALOG;
//    }
//
//    @Override
//    public void die(boolean triggerRelics) {
//        //runAnim("Defeat");
//        ((BetterSpriterAnimation)this.animation).startDying();
//        super.die(triggerRelics);
//    }
//
//    //Runs a specific animation
//    public void runAnim(String animation) {
//        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
//    }
//
//    //Resets character back to idle animation
//    public void resetAnimation() {
//        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Idle");
//    }
//
//    //Prevents any further animation once the death animation is finished
//    public void stopAnimation() {
//        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
//        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
//        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
//    }
//
//    public class SakuyaListener implements Player.PlayerListener {
//
//        private Sakuya character;
//
//        public SakuyaListener(Sakuya character) {
//            this.character = character;
//        }
//
//        public void animationFinished(Animation animation){
//            if (animation.name.equals("Defeat")) {
//                character.stopAnimation();
//            } else if (!animation.name.equals("Idle")) {
//                character.resetAnimation();
//            }
//        }
//
//        //UNUSED
//        public void animationChanged(Animation var1, Animation var2){
//
//        }
//
//        //UNUSED
//        public void preProcess(Player var1){
//
//        }
//
//        //UNUSED
//        public void postProcess(Player var1){
//
//        }
//
//        //UNUSED
//        public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){
//
//        }
//    }
//}