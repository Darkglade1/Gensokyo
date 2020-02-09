package Gensokyo.events;

import Gensokyo.GensokyoMod;
import Gensokyo.monsters.bossRush.Eiki;
import Gensokyo.monsters.bossRush.Kaguya;
import Gensokyo.monsters.bossRush.Yuyuko;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;

import java.util.ArrayList;

import static Gensokyo.GensokyoMod.makeEventPath;

public class AHistoryOfViolence extends AbstractImageEvent {

    public static final String ID = GensokyoMod.makeID("AHistoryOfViolence");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("BossRush.png");

    private int screenNum = 0;

    private ArrayList<AbstractMonster> bosses = new ArrayList<>();
    private ArrayList<Boolean> defeatedBosses = new ArrayList<>();
    private AbstractMonster fightingBoss;

    public AHistoryOfViolence() {
        super(NAME, DESCRIPTIONS[0], IMG);
        bosses.add(new Yuyuko());
        bosses.add(new Eiki());
        bosses.add(new Kaguya());
        defeatedBosses.add(Boolean.FALSE);
        defeatedBosses.add(Boolean.FALSE);
        defeatedBosses.add(Boolean.FALSE);
        setDialog();
    }

    @Override
    protected void buttonEffect(int buttonPressed) { // This is the event:
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    default:
                        MonsterGroup group = new MonsterGroup(bosses.get(buttonPressed));
                        fightingBoss = bosses.get(buttonPressed);
                        AbstractDungeon.getCurrRoom().monsters = group;
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().rewardAllowed = false;
                        AbstractDungeon.scene.nextRoom(AbstractDungeon.getCurrRoom()); //switches bg
                        this.enterCombatFromImage();
                        break;
                }
                break;
            default:
                this.openMap();
        }
    }

    private void setDialog() {
        this.imageEventText.clearAllDialogs();
        for (int i = 0; i < bosses.size(); i++) {
            if (!defeatedBosses.get(i)) {
                this.imageEventText.setDialogOption(OPTIONS[0] + bosses.get(i).name);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[1] + bosses.get(i).name + OPTIONS[2], true);
            }
        }
    }

    private boolean defeatedAllBosses() {
        for (boolean boo : defeatedBosses) {
            if (!boo) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reopen() {
        AbstractDungeon.resetPlayer();
        AbstractDungeon.player.drawX = (float) Settings.WIDTH * 0.25F;
        AbstractDungeon.player.preBattlePrep();
        int index = bosses.indexOf(fightingBoss);
        defeatedBosses.set(index, true);
        if (defeatedAllBosses()) {
            MapRoomNode node = new MapRoomNode(1, 1);
            node.room = new TrueVictoryRoom();
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.nextRoomTransitionStart();
        } else {
            setDialog();
            this.enterImageFromCombat();
        }
    }
}
