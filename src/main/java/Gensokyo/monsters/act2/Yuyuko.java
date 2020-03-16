package Gensokyo.monsters.act2;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.RezAction;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.powers.act2.Reflowering;
import Gensokyo.vfx.EmptyEffect;
import actlikeit.dungeons.CustomDungeon;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
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
    private static final byte GHASTLY_DREAM = 0;
    private static final byte LAW_OF_MORTALITY = 1;
    private static final byte RESURRECTION_BUTTERFLY = 2;

    private static final int NORMAL_ATTACK_DAMAGE = 13;
    private static final int A3_NORMAL_ATTACK_DAMAGE = 14;
    private static final int NORMAL_ATTACK_HITS = 3;

    private static final int DEBUFF_ATTACK_DAMAGE = 13;
    private static final int A3_DEBUFF_ATTACK_DAMAGE = 14;
    private static final int DEBUFF_ATTACK_HITS = 2;
    private static final int DEBUFF_AMOUNT = 1;

    private static final int STRENGTH = 3;
    private static final int A18_STRENGTH = 4;

    private static final int HP_MIN = 95;
    private static final int HP_MAX = 97;
    private static final int A_2_HP_MIN = 98;
    private static final int A_2_HP_MAX = 101;

    private static final int MINION_HEALTH_INCREMENT = 2;
    private static final int A8_MINION_HEALTH_INCREMENT = 3;

    private static final int STR_LOSS = 1;
    private static final int BLUE_SOULS = 5;
    private static final int PURPLE_SOULS = 5;
    private static final int DEAD_MINIONS_THRESHOLD = 7;

    private int normalDamage;
    private int debuffDamage;
    private int strength;
    private int minionHealthIncrement;

    private Map<Byte, EnemyMoveInfo> moves;
    public ArrayList<YuyukoSoul> blueSouls = new ArrayList<>();
    public ArrayList<YuyukoSoul> purpleSouls = new ArrayList<>();
    private AbstractAnimation attackAnimations = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/AttackAnimations/Spriter/AttackAnimations.scml");

    public Yuyuko() {
        this(50.0F, 0.0f);
    }

    public Yuyuko(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strength = A18_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
            minionHealthIncrement = A8_MINION_HEALTH_INCREMENT;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            minionHealthIncrement = MINION_HEALTH_INCREMENT;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.debuffDamage = A3_DEBUFF_ATTACK_DAMAGE;
            this.normalDamage = A3_NORMAL_ATTACK_DAMAGE;
        } else {
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GHASTLY_DREAM, new EnemyMoveInfo(GHASTLY_DREAM, Intent.ATTACK, normalDamage, NORMAL_ATTACK_HITS, true));
        this.moves.put(LAW_OF_MORTALITY, new EnemyMoveInfo(LAW_OF_MORTALITY, Intent.ATTACK_DEBUFF, debuffDamage, DEBUFF_ATTACK_HITS, true));
        this.moves.put(RESURRECTION_BUTTERFLY, new EnemyMoveInfo(RESURRECTION_BUTTERFLY, Intent.BUFF, -1, 0, false));

        this.FAN_REGION = new TextureRegion(FAN);
        Player.PlayerListener listener = new YuyukoListener(this);
        ((BetterSpriterAnimation)this.attackAnimations).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        CustomDungeon.playTempMusicInstantly("BorderOfLife");
        this.addToBot(new ApplyPowerAction(this, this, new Reflowering(this, STR_LOSS)));
        SpawnMinions();
        nextBlueSoul();
        nextPurpleSoul();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GHASTLY_DREAM: {
                runAnim("SoulGrab");
                CardCrawlGame.sound.playV("Gensokyo:ghost", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                }
                break;
            }
            case LAW_OF_MORTALITY: {
                runAnim("ButterflyCircle");
                CardCrawlGame.sound.playV("Gensokyo:pest", 1.3F);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.3F));

                for (int i = 0; i < DEBUFF_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                break;
            }
            case RESURRECTION_BUTTERFLY: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
                break;
            }
//            case SAIGYOUJI_PARINIRVANA: {
//                runAnim("MagicCircle");
//                CardCrawlGame.sound.playV("Gensokyo:magic", 1.2F);
//                AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 1.0F));
//                break;
 //           }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
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
        if (firstMove) {
            this.setMoveShortcut(GHASTLY_DREAM);
        } else if (deadMinions() >= DEAD_MINIONS_THRESHOLD && !this.lastMove(RESURRECTION_BUTTERFLY) && !this.lastMoveBefore(RESURRECTION_BUTTERFLY)) {
            this.setMoveShortcut(RESURRECTION_BUTTERFLY);
        } else if (this.lastMove(RESURRECTION_BUTTERFLY)) {
            this.setMoveShortcut(LAW_OF_MORTALITY);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GHASTLY_DREAM)) {
                possibilities.add(GHASTLY_DREAM);
            }
            if (!this.lastMove(LAW_OF_MORTALITY)) {
                possibilities.add(LAW_OF_MORTALITY);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private int deadMinions() {
        int dead = 0;
        dead += BLUE_SOULS - blueSouls.size();
        dead += PURPLE_SOULS - purpleSouls.size();
        return dead;
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