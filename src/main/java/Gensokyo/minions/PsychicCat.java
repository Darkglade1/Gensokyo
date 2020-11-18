package Gensokyo.minions;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kobting.friendlyminions.monsters.MinionMove;

public class PsychicCat extends AbstractPet {
    public static String ID = GensokyoMod.makeID("PsychicCat");
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private static final int vulnerable = 1;
    private static final int weak = 1;
    private static final int draw = 1;
    public static final String move1 = MOVES[0] + vulnerable + MOVES[1];
    public static final String move2 = MOVES[2] + weak + MOVES[3];
    public static final String move3 = MOVES[4] + draw + MOVES[5];

    public PsychicCat(int MAX_HP, int current_hp, float x, float y) {
        super(NAME, ID, MAX_HP, current_hp, -8.0F, 10.0F, 130.0F, 140.0F, x, y);
        setAnimal("Cat");
    }

    @Override
    protected void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/debuff.png"), move1, () -> {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(mo, this, new WeakPower(mo, weak, false), weak));
            }

        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/debuff.png"), move2, () -> {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(mo, this, new VulnerablePower(mo, vulnerable, false), vulnerable));
            }
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/buff.png"), move3, () -> {
            addToBot(new DrawCardAction(draw));
        }));
    }
}