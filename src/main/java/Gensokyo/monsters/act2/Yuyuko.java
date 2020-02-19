package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.RazIntent.IntentEnums;
import Gensokyo.actions.YeetPlayerAction;
import Gensokyo.cards.Butterfly;
import Gensokyo.powers.act2.DeathTouch;
import Gensokyo.powers.act2.DeathTouchFrail;
import Gensokyo.powers.act2.DeathTouchWeak;
import Gensokyo.powers.act2.Reflowering;
import Gensokyo.vfx.EmptyEffect;
import actlikeit.dungeons.CustomDungeon;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Yuyuko extends CustomMonster
{
    private static final Texture FAN = new Texture("GensokyoResources/images/monsters/Yuyuko/Fan.png");
    private TextureRegion FAN_REGION;
    public static final String ID = "Gensokyo:Yuyuko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte GHOSTLY_BUTTERFLY = 0;
    private static final byte GHASTLY_DREAM = 1;
    private static final byte LAW_OF_MORTALITY = 2;
    private static final byte RESURRECTION_BUTTERFLY = 3;
    private static final byte SAIGYOUJI_PARINIRVANA = 4;
    private static final byte SPIRITS_THAT_DIED_WELL = 5;
    private static final int COOLDOWN = 2;
    private static final int GHOSTLY_BUTTERFLY_DAMAGE = 16;
    private static final int A4_GHOSTLY_BUTTERFLY_DAMAGE = 18;
    private static final int GHASTLY_DREAM_DAMAGE = 10;
    private static final int A4_GHASTLY_DREAM_DAMAGE = 11;
    private static final int RESURRECTION_BUTTERFLY_DAMAGE = 12;
    private static final int A4_RESURRECTION_BUTTERFLY_DAMAGE = 13;
    private static final int BLOCK = 11;
    private static final int A9_BLOCK = 12;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int STATUS_COUNT = 1;
    private static final int A19_STATUS_COUNT = 2;
    private static final int FAN_INCREMENT = 1;
    private static final int A19_FAN_INCREMENT = 2;
    public static final int FAN_THRESHOLD = 10;
    private static final int HP = 260;
    private static final int A9_HP = 280;
    private int ghostlyButterflyDamage;
    private int ghastlyDreamDamage;
    private int resurrectionButterflyDamage;
    private int statusCount;
    private int fanIncrement;
    private int block;
    public int fanCounter;
    private int turnCounter;
    private Map<Byte, EnemyMoveInfo> moves;
    public AbstractMonster[] minions = new AbstractMonster[2];
    private ArrayList<AbstractAnimation> souls = new ArrayList<>();
    private AbstractAnimation attackAnimations = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/AttackAnimations/Spriter/AttackAnimations.scml");

    public Yuyuko() {
        this(0.0f, 0.0f);
    }

    public Yuyuko(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.statusCount = A19_STATUS_COUNT;
            this.fanIncrement = A19_FAN_INCREMENT;
        } else {
            this.statusCount = STATUS_COUNT;
            this.fanIncrement = FAN_INCREMENT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.ghostlyButterflyDamage = A4_GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = A4_GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = A4_RESURRECTION_BUTTERFLY_DAMAGE;
        } else {
            this.ghostlyButterflyDamage = GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = RESURRECTION_BUTTERFLY_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GHOSTLY_BUTTERFLY, new EnemyMoveInfo(GHOSTLY_BUTTERFLY, Intent.ATTACK, this.ghostlyButterflyDamage, 0, false));
        this.moves.put(GHASTLY_DREAM, new EnemyMoveInfo(GHASTLY_DREAM, Intent.ATTACK_DEFEND, this.ghastlyDreamDamage, 0, false));
        this.moves.put(LAW_OF_MORTALITY, new EnemyMoveInfo(LAW_OF_MORTALITY, Intent.DEBUFF, -1, 0, true));
        this.moves.put(RESURRECTION_BUTTERFLY, new EnemyMoveInfo(RESURRECTION_BUTTERFLY, Intent.ATTACK_DEBUFF, this.resurrectionButterflyDamage, 0, false));
        this.moves.put(SAIGYOUJI_PARINIRVANA, new EnemyMoveInfo(SAIGYOUJI_PARINIRVANA, IntentEnums.DEATH, -1, 0, false));
        this.moves.put(SPIRITS_THAT_DIED_WELL, new EnemyMoveInfo(SPIRITS_THAT_DIED_WELL, Intent.UNKNOWN, -1, 0, false));

        this.FAN_REGION = new TextureRegion(FAN);
        Player.PlayerListener listener = new YuyukoListener(this);
        ((BetterSpriterAnimation)this.attackAnimations).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("BorderOfLife");
        this.addToBot(new ApplyPowerAction(this, this, new DeathTouch(this)));
        this.addToBot(new ApplyPowerAction(this, this, new Reflowering(this, this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GHOSTLY_BUTTERFLY: {
                runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                turnCounter++;
                break;
            }
            case GHASTLY_DREAM: {
                runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, this.block));
                }
                turnCounter++;
                break;
            }
            case LAW_OF_MORTALITY: {
                runAnim("MagicCircle");
                CardCrawlGame.sound.playV("Gensokyo:magic", 1.2F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                turnCounter++;
                break;
            }
            case RESURRECTION_BUTTERFLY: {
                runAnim("ButterflyCircle");
                CardCrawlGame.sound.playV("Gensokyo:pest", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Butterfly(), statusCount));
                incrementFan(fanIncrement);
                turnCounter = 0;
                break;
            }
            case SAIGYOUJI_PARINIRVANA: {
                runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                addToBot(new YeetPlayerAction());
                break;
            }
            case SPIRITS_THAT_DIED_WELL: {
                PurpleSoul minion2 = new PurpleSoul(-300.0F, 0.0F, this);
                minions[1] = minion2;
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion2, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion2, this, new DeathTouchWeak(minion2, 1)));

                BlueSoul minion1 = new BlueSoul(-500.0F, 0.0F, this);
                minions[0] = minion1;
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(minion1, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion1, this, new DeathTouchFrail(minion1, 1)));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void incrementFan(int amount) {
        if (fanCounter < FAN_THRESHOLD) {
            fanCounter += amount;
            if (fanCounter > FAN_THRESHOLD) {
                fanCounter = FAN_THRESHOLD;
            }
            if (this.hasPower(Reflowering.POWER_ID)) {
                this.getPower(Reflowering.POWER_ID).flashWithoutSound();
                this.getPower(Reflowering.POWER_ID).amount = fanCounter;
            }
            for (int i = 0; i < amount; i++) {
                if (souls.size() < FAN_THRESHOLD) {
                    if (souls.size() < 5) {
                        souls.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/BlueSoul/Spriter/BlueSoulAnimation.scml"));
                    } else {
                        souls.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/PurpleSoul/Spriter/PurpleSoulAnimation.scml"));
                    }
                    if (MathUtils.randomBoolean()) {
                        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.3F);
                    } else {
                        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.3F);
                    }
                }
            }
        }
    }

    @Override
    protected void getMove(final int num) {
        if (minions[0] == null && minions[1] == null) {
            this.setMoveShortcut(SPIRITS_THAT_DIED_WELL);
        } else if (this.fanCounter >= FAN_THRESHOLD) {
            this.setMoveShortcut(SAIGYOUJI_PARINIRVANA);
        } else if (turnCounter >= COOLDOWN) {
            this.setMoveShortcut(RESURRECTION_BUTTERFLY);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GHOSTLY_BUTTERFLY)) {
                possibilities.add(GHOSTLY_BUTTERFLY);
            }
            if (!this.lastMove(GHASTLY_DREAM)) {
                possibilities.add(GHASTLY_DREAM);
            }
            if (!this.lastMove(LAW_OF_MORTALITY) && !this.lastMoveBefore(LAW_OF_MORTALITY)) {
                possibilities.add(LAW_OF_MORTALITY);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;
        sb.setColor(Color.WHITE);
        sb.draw(FAN_REGION, this.drawX - this.FAN_REGION.getRegionWidth() * scaleWidth, this.drawY + (this.FAN_REGION.getRegionHeight() * scaleHeight) / 2, 0.0F, 0.0F, this.FAN_REGION.getRegionWidth(), this.FAN_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        float xOffsetIncrement = 75.0F;
        float yOffsetIncrement = 130.0F;
        float xOffset = 160.0F;
        float yOffset = 130.0F;
        for (int i = 0; i < souls.size(); i++) {
            AbstractAnimation soul = souls.get(i);
            soul.renderSprite(sb, this.drawX - (this.FAN_REGION.getRegionWidth() - xOffset) * scaleWidth, this.drawY + ((this.FAN_REGION.getRegionHeight() + yOffset )* scaleHeight) / 2);
            xOffset += xOffsetIncrement;
            if (i < 4) {
                yOffset += yOffsetIncrement;
            } else if (i > 4) {
                yOffset -= yOffsetIncrement;
            }
        }
        attackAnimations.renderSprite(sb, AbstractDungeon.player.drawX, AbstractDungeon.player.drawY);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof BlueSoul || mo instanceof PurpleSoul) {
                if (!mo.isDead && !mo.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yuyuko");
        NAME = Yuyuko.monsterStrings.NAME;
        MOVES = Yuyuko.monsterStrings.MOVES;
        DIALOG = Yuyuko.monsterStrings.DIALOG;
    }

    //Runs a specific animation
    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.attackAnimations).myPlayer.setAnimation(animation);
    }

    //Resets character back to idle animation
    public void resetAnimation() {
        ((BetterSpriterAnimation)this.attackAnimations).myPlayer.setAnimation("None");
    }

    public class YuyukoListener implements Player.PlayerListener {

        private Yuyuko character;

        public YuyukoListener(Yuyuko character) {
            this.character = character;
        }

        public void animationFinished(Animation animation){
            if (!animation.name.equals("None")) {
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