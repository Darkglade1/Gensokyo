package Gensokyo.monsters.act3.Shinki;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.ShinkiEventAction;
import Gensokyo.actions.UsePreBattleActionAction;
import Gensokyo.monsters.AbstractSpriterMonster;
import Gensokyo.powers.act3.UnstableReality;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.screens.DeathScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shinki extends AbstractSpriterMonster
{
    public static final String ID = GensokyoMod.makeID("Shinki");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;
    private static final byte UNKNOWN = 0;

    private static final int DAMAGE = 7;
    private static final int DEBUFF = 1;
    private static final int HEAL = 5;

    private static final int HP = 300;

    public GenericEventDialog imageEventText;
    public AbstractShinkiDelusion currentDelusion;
    private ArrayList<AbstractShinkiDelusion> delusionList = new ArrayList<>();
    public int delusionsDefeated = 0;
    public AbstractShinkiEvent currentEvent;

    private static final int NUM_DELUSIONS_TO_FIGHT = 2;
    private static final float THRESHOLD1 = 0.70f;
    private static final float THRESHOLD2 = 0.40f;

    private Map<Byte, EnemyMoveInfo> moves;

    public Shinki() {
        this(0.0f, 0.0f);
    }

    public Shinki(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 225.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Marisa/Spriter/MarisaAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.moves = new HashMap<>();
        this.moves.put(UNKNOWN, new EnemyMoveInfo(UNKNOWN, Intent.UNKNOWN, DAMAGE, 0, false));

        AbstractEvent.type = AbstractEvent.EventType.IMAGE;
        this.imageEventText = new GenericEventDialog();
        this.imageEventText.hide();
        this.imageEventText.clear();
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.getCurrRoom().playBgmInstantly("Wind God Girl");
        delusionList.add(new Alice(-480.0f, 0.0f, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstableReality(this)));
    }

    @Override
    public void takeTurn() {
        AbstractEvent.type = AbstractEvent.EventType.IMAGE;
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1 && currentDelusion != null) {
            info.applyPowers(this, currentDelusion);
        }
        switch (this.nextMove) {
            case UNKNOWN: {
                if (delusionsDefeated >= NUM_DELUSIONS_TO_FIGHT) {
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                    this.onBossVictoryLogic();
                } else if (currentDelusion == null) {
                    currentDelusion = delusionList.remove(0);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(currentDelusion, false));
                    AbstractDungeon.actionManager.addToBottom(new UsePreBattleActionAction(currentDelusion));
                    runEvent(currentDelusion.event1);
                } else if (currentDelusion.currentHealth <= (int)(currentDelusion.maxHealth * THRESHOLD1)) {
                    runEvent(currentDelusion.event2);
                } else if (currentDelusion.currentHealth <= (int)(currentDelusion.maxHealth * THRESHOLD2)) {
                    runEvent(currentDelusion.event3);
                } else {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(currentDelusion, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void runEvent(AbstractShinkiEvent event) {
        this.currentEvent = event;
        ReflectionHacks.setPrivate(this.imageEventText, GenericEventDialog.class, "title", event.title);
        this.imageEventText.loadImage(event.image);
        this.imageEventText.updateBodyText(event.bodyText);
        for (String option : event.options) {
            this.imageEventText.setDialogOption(option);
        }
        AbstractDungeon.actionManager.addToBottom(new ShinkiEventAction(this));
    }

    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    protected void getMove(final int num) {
        setMoveShortcut(UNKNOWN);
    }

    @Override
    public void die(boolean triggerRelics) {
        AbstractDungeon.player.isDead = true;
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        AbstractDungeon.actionManager.clearPostCombatActions();
        super.die(triggerRelics);
    }
}