package Gensokyo.minions;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import kobting.friendlyminions.monsters.MinionMove;

public class JeweledCobra extends AbstractPet {
    public static String ID = GensokyoMod.makeID("JeweledCobra");
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private static final int poison = 5;
    private static final int aoePoison = 3;
    private static final int block = 6;
    public static final String move1 = MOVES[0] + poison + MOVES[1];
    public static final String move2 = MOVES[2] + aoePoison + MOVES[3];
    public static final String move3 = MOVES[4] + block + MOVES[5];

    public JeweledCobra(int MAX_HP, int current_hp, float x, float y) {
        super(NAME, ID, MAX_HP, current_hp, -8.0F, 10.0F, 130.0F, 140.0F, x, y);
        setAnimal("Snake");
    }

    @Override
    protected void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/debuff.png"), move1, () -> {
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(target, this, new PoisonPower(target, this, poison), poison));
        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/debuff.png"), move2, () -> {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(mo, this, new PoisonPower(mo, this, aoePoison), aoePoison));
            }
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/defend.png"), move3, () -> {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, block));
        }));
    }
}