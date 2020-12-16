package Gensokyo.monsters.act3;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.CustomIntents.IntentEnums;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.RezAction;
import Gensokyo.actions.SetMaxHealthToCurrentAction;
import Gensokyo.actions.TemporaryMaxHPLossAction;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.actions.YeetPlayerAction;
import Gensokyo.cards.Butterfly;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.BorderOfDeath;
import Gensokyo.powers.act3.Deathtouch;
import Gensokyo.powers.act3.Empower;
import Gensokyo.vfx.EmptyEffect;
import actlikeit.dungeons.CustomDungeon;
import basemod.ReflectionHacks;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
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
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Yuyuko extends AbstractSpriterMonster
{
    private static final Texture FAN = new Texture("GensokyoResources/images/monsters/Yuyuko/Fan.png");
    private TextureRegion FAN_REGION;
    public static final String ID = GensokyoMod.makeID("Yuyuko");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GensokyoMod.makeID("CurseAttackIntent"));
    private static final String[] TEXT = uiStrings.TEXT;

    private boolean firstMove = true;
    private static final byte GHASTLY_DREAM = 0; //Attack
    private static final byte LAW_OF_MORTALITY = 1; //Debuff attack
    private static final byte RESURRECTION_BUTTERFLY = 2; //Shuffle statuses
    private static final byte INVITATION_FROM_NETHER_SIDE = 3; //Reduce Max HP
    private static final byte SAIGYOUJI_PARINIRVANA = 4; //Instant kill
    private static final byte GIFTS = 5; //block

    private static final int NORMAL_ATTACK_DAMAGE = 20;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 22;;

    private static final int DEBUFF_ATTACK_DAMAGE = 15;
    private static final int A4_DEBUFF_ATTACK_DAMAGE = 16;
    private static final int DEBUFF_AMOUNT = 1;

    private static final int STATUS_AMT = 3;
    private static final int A19_STATUS_AMT = 5;
    private int statusAmt;

    private static final int MAX_HP_REDUCTION = 10;
    private static final int A19_MAX_HP_REDUCTION = 14;
    private int maxHPReduction;

    private static final int EMPOWER_AMT = 5;
    private static final int A19_EMPOWER_AMT = 7;
    private int empower;

    private static final int BLOCK = 15;

    private static final int COOLDOWN = 2;
    private int counter = COOLDOWN;

    private static final int HP = 330;
    private static final int A9_HP = 350;

    private static final int MINION_HEALTH_INCREMENT = 1;
    private static final int A9_MINION_HEALTH_INCREMENT = 2;

    private static final int BLUE_SOULS = 5;
    private static final int PURPLE_SOULS = 5;

    private int normalDamage;
    private int debuffDamage;
    private int minionHealthIncrement;

    private int playerInitialCurrentHP;

    private Map<Byte, EnemyMoveInfo> moves;
    public ArrayList<YuyukoSoul> blueSouls = new ArrayList<>();
    public ArrayList<YuyukoSoul> purpleSouls = new ArrayList<>();
    private AbstractAnimation attackAnimations = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/AttackAnimations/Spriter/AttackAnimations.scml");

    public Yuyuko() {
        this(50.0F, 0.0f);
    }

    public Yuyuko(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.maxHPReduction = A19_MAX_HP_REDUCTION;
            this.statusAmt = A19_STATUS_AMT;
            empower = A19_EMPOWER_AMT;
        } else {
            this.maxHPReduction = MAX_HP_REDUCTION;
            this.statusAmt = STATUS_AMT;
            empower = EMPOWER_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            minionHealthIncrement = A9_MINION_HEALTH_INCREMENT;
        } else {
            this.setHp(HP);
            minionHealthIncrement = MINION_HEALTH_INCREMENT;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.debuffDamage = A4_DEBUFF_ATTACK_DAMAGE;
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
        } else {
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GHASTLY_DREAM, new EnemyMoveInfo(GHASTLY_DREAM, Intent.ATTACK, normalDamage, 0, false));
        this.moves.put(LAW_OF_MORTALITY, new EnemyMoveInfo(LAW_OF_MORTALITY, Intent.ATTACK_DEBUFF, debuffDamage, 0, false));
        this.moves.put(RESURRECTION_BUTTERFLY, new EnemyMoveInfo(RESURRECTION_BUTTERFLY, Intent.DEBUFF, -1, 0, false));
        this.moves.put(INVITATION_FROM_NETHER_SIDE, new EnemyMoveInfo(INVITATION_FROM_NETHER_SIDE, IntentEnums.ATTACK_CURSE, maxHPReduction, 0, false));
        this.moves.put(SAIGYOUJI_PARINIRVANA, new EnemyMoveInfo(SAIGYOUJI_PARINIRVANA, IntentEnums.DEATH, -1, 0, false));
        this.moves.put(GIFTS, new EnemyMoveInfo(GIFTS, Intent.BUFF, -1, 0, false));

        this.FAN_REGION = new TextureRegion(FAN);
        Player.PlayerListener listener = new YuyukoListener(this);
        ((BetterSpriterAnimation)this.attackAnimations).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("BorderOfLife");
        addToBot(new ApplyPowerAction(this, this, new Deathtouch(this)));
        SpawnMinions();
        nextBlueSoul();
        nextPurpleSoul();
        playerInitialCurrentHP = AbstractDungeon.player.currentHealth;
        AbstractDungeon.actionManager.addToBottom(new SetMaxHealthToCurrentAction());
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
            case GHASTLY_DREAM: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                break;
            }
            case LAW_OF_MORTALITY: {
                useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
            case RESURRECTION_BUTTERFLY: {
                runAnim("ButterflyCircle");
                CardCrawlGame.sound.playV("Gensokyo:pest", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.3F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Butterfly(), statusAmt));
                break;
            }
            case INVITATION_FROM_NETHER_SIDE: {
                playGhostEffect(this);
                AbstractDungeon.actionManager.addToBottom(new TemporaryMaxHPLossAction(maxHPReduction));
                counter = COOLDOWN + 1;
                break;
            }
            case SAIGYOUJI_PARINIRVANA: {
//                runAnim("MagicCircle");
//                CardCrawlGame.sound.playV("Gensokyo:magic", 1.2F);
//                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                playGhostEffect(this);
                AbstractDungeon.actionManager.addToBottom(new YeetPlayerAction());
                break;
            }
            case GIFTS: {
                boolean gaveGift = false;
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (mo instanceof YuyukoSoul) {
                        if (!mo.isDeadOrEscaped()) {
                            gaveGift = true;
                            addToBot(new ApplyPowerAction(mo, this, new Empower(mo, empower), empower));
                        }
                    }
                }

                if (!gaveGift) {
                    addToBot(new GainBlockAction(this, BLOCK));
                }
                counter = COOLDOWN + 1;
                break;
            }
        }
        counter--;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public static void playGhostEffect(Yuyuko yuyuko) {
        yuyuko.runAnim("SoulGrab");
        CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.4F));
    }

    private void SpawnMinions() {
        float xOffsetIncrement = 75.0F;
        float yOffsetIncrement = 130.0F;
        float xOffset = 160.0F;
        float yOffset = 130.0F;
        for (int i = 0; i < BLUE_SOULS + PURPLE_SOULS; i++) {
            float x = 0 - (this.FAN_REGION.getRegionWidth() - xOffset);
            float y = 0 + ((this.FAN_REGION.getRegionHeight() + yOffset)) / 2 - 100.0F;
            YuyukoSoul soul;
            if (i < 5) {
                int bonusHealth = minionHealthIncrement * (4 - i);
                soul = new BlueSoul(x, y, this, bonusHealth);
                blueSouls.add(0, soul);
            } else {
                int bonusHealth = minionHealthIncrement * (i % 5);
                soul = new PurpleSoul(x, y, this, bonusHealth);
                purpleSouls.add(soul);
            }
            MinionPower power = new MinionPower(soul);
            soul.powers.add(power);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(soul, false));
            AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(soul));
            xOffset += xOffsetIncrement;
            if (i < 4) {
                yOffset += yOffsetIncrement;
            } else if (i > 4) {
                yOffset -= yOffsetIncrement;
            }
        }
    }

    public void nextBlueSoul() {
        if (blueSouls.size() > 0) {
            YuyukoSoul blueSoul = blueSouls.get(0);
            blueSoul.active = true;
            AbstractDungeon.actionManager.addToBottom(new RezAction(blueSoul));
            blueSoul.rollMove();
            blueSoul.createIntent();
        }
    }

    public void nextPurpleSoul() {
        if (purpleSouls.size() > 0) {
            YuyukoSoul purpleSoul = purpleSouls.get(0);
            purpleSoul.active = true;
            AbstractDungeon.actionManager.addToBottom(new RezAction(purpleSoul));
            purpleSoul.rollMove();
            purpleSoul.createIntent();
        }
    }

    @Override
    protected void getMove(final int num) {
        if (AbstractDungeon.player.maxHealth == 1 && TempHPField.tempHp.get(AbstractDungeon.player) == 0 && !firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
            setMoveShortcut(SAIGYOUJI_PARINIRVANA);
        } else if (counter <= 0) {
            if (blueSouls.size() > 0 || purpleSouls.size() > 0) {
                setMoveShortcut(GIFTS);
            } else {
                setMoveShortcut(INVITATION_FROM_NETHER_SIDE);
            }
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GHASTLY_DREAM)) {
                possibilities.add(GHASTLY_DREAM);
            }
            if (!this.lastMove(LAW_OF_MORTALITY) && !this.lastMoveBefore(LAW_OF_MORTALITY)) {
                possibilities.add(LAW_OF_MORTALITY);
            }
            if (!this.lastMove(RESURRECTION_BUTTERFLY) && !this.lastMoveBefore(RESURRECTION_BUTTERFLY)) {
                possibilities.add(RESURRECTION_BUTTERFLY);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void applyPowers() {
        //Make sure the intent isn't affected by damage modifiers
        if (intent == IntentEnums.ATTACK_CURSE) {
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", maxHPReduction);
            PowerTip intentTip = (PowerTip)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
            intentTip.body = TEXT[1] + maxHPReduction + TEXT[2];
        } else {
            super.applyPowers();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;
        sb.setColor(Color.WHITE);
        sb.draw(FAN_REGION, this.drawX - (this.FAN_REGION.getRegionWidth() + 50.0F) * scaleWidth, this.drawY + ((this.FAN_REGION.getRegionHeight() / 2.0F) - 100.0F) * scaleHeight, 0.0F, 0.0F, this.FAN_REGION.getRegionWidth(), this.FAN_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
        for (YuyukoSoul soul : blueSouls) {
            soul.realRender(sb);
        }
        for (YuyukoSoul soul : purpleSouls) {
            soul.realRender(sb);
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
        AbstractPower power = AbstractDungeon.player.getPower(BorderOfDeath.POWER_ID);
        if (power != null) {
            BorderOfDeath death = (BorderOfDeath)power;
            death.playerHpResetAmount = playerInitialCurrentHP; //prevent yuyuko from being a free heal
        }
        onBossVictoryLogic();
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