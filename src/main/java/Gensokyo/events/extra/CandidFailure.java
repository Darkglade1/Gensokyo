//package Gensokyo.events.extra;
//
//import Gensokyo.rooms.nitori.Nitori;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.events.AbstractEvent;
//import com.megacrit.cardcrawl.events.RoomEventDialog;
//import com.megacrit.cardcrawl.localization.EventStrings;
//import com.megacrit.cardcrawl.monsters.MonsterGroup;
//
//import static Gensokyo.GensokyoMod.makeID;
//
//public class CandidFailure extends AbstractEvent {
//    public static final String ID = makeID(CandidFailure.class.getSimpleName());
//    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
//    public static final String NAME = eventStrings.NAME;
//    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
//    public static final String[] OPTIONS = eventStrings.OPTIONS;
//    private static final String INTRO_MSG = DESCRIPTIONS[0];
//    private CurScreen screen = CurScreen.INTRO;
//    private enum CurScreen { INTRO, END }
//    private Nitori nitori = new Nitori();
//    public CandidFailure() {
//        this.body = INTRO_MSG;
//        this.roomEventText.addDialogOption(OPTIONS[0]);
//        this.hasDialog = true;
//        this.hasFocus = true;
//        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(nitori);
//    }
//
//    public void update() {
//        super.update();
//        if (!RoomEventDialog.waitForInput) { buttonEffect(this.roomEventText.getSelectedOption()); }
//    }
//
//    protected void buttonEffect(int buttonPressed) {
//        switch (this.screen) {
//            case INTRO:
//                switch (buttonPressed) {
//                    case 0:
//                        this.screen = CurScreen.END;
//                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
//                        this.roomEventText.updateDialogOption(0, OPTIONS[0]);
//                        this.roomEventText.clearRemainingOptions();
//                        return;
//                }
//                break;
//            case END:
//                openMap();
//                break;
//        }
//    }
//}