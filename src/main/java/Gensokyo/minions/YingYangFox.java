package Gensokyo.minions;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class YingYangFox extends AbstractPet {
    public static String ID = GensokyoMod.makeID("YinYangFox");
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private AbstractMonster target;
    private static final int damage = 8;
    private static final int block = 6;
    private static final int heal = 4;
    public static final String damage_string = MOVES[0] + damage + MOVES[1];
    public static final String block_string = MOVES[2] + block + MOVES[3];
    public static final String heal_string = MOVES[4] + heal + MOVES[5];

    public YingYangFox(int MAX_HP, int current_hp, float x, float y) {
        super(NAME, ID, MAX_HP, current_hp, -8.0F, 10.0F, 130.0F, 140.0F, x, y);
        setAnimal("Fox");
    }

    @Override
    protected void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/attack move.png"), damage_string, () -> {
            target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, damage, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/defend move.png"),block_string, () -> {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, this, block));
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/heal.png"),heal_string, () -> {
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, heal));
        }));
    }
}