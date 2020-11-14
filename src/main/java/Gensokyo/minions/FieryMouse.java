package Gensokyo.minions;

import Gensokyo.GensokyoMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import kobting.friendlyminions.monsters.MinionMove;

public class FieryMouse extends AbstractPet {
    public static String ID = GensokyoMod.makeID("FieryMouse");
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private static final int damage = 7;
    private static final int strength_hp_loss = 1;
    private static final int strength = 1;
    private static final int heal_hp_loss = 3;
    private static final int heal = 3;
    public static final String move1 = MOVES[0] + damage + MOVES[1];
    public static final String move2 = MOVES[2] + strength_hp_loss + MOVES[3] + MOVES[4] + strength + MOVES[5];
    public static final String move3 = MOVES[2] + heal_hp_loss + MOVES[3] + MOVES[6] + heal + MOVES[7];

    public FieryMouse(int MAX_HP, int current_hp, float x, float y) {
        super(NAME, ID, MAX_HP, current_hp, -8.0F, 10.0F, 130.0F, 140.0F, x, y);
        setAnimal("Mouse");
    }

    @Override
    protected void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/attack.png"), move1, () -> {
            DamageInfo info = new DamageInfo(this, damage, DamageInfo.DamageType.NORMAL);
            int[] newMultiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
            for (int i = 0; i < newMultiDamage.length; i++) {
                AbstractMonster mo = AbstractDungeon.getMonsters().monsters.get(i);
                info.applyPowers(this, mo);
                newMultiDamage[i] = info.output;
            }
            addToBot(new DamageAllEnemiesAction(this, newMultiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/buff.png"), move2, () -> {
            addToBot(new LoseHPAction(this, this, strength_hp_loss));
            addToBot(new VFXAction(AbstractDungeon.player, new InflameEffect(AbstractDungeon.player), 1.0F));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, strength), strength));
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/heal.png"), move3, () -> {
            addToBot(new LoseHPAction(this, this, heal_hp_loss));
            addToBot(new VFXAction(AbstractDungeon.player, new InflameEffect(AbstractDungeon.player), 1.0F));
            addToBot(new HealAction(AbstractDungeon.player, this, heal));
        }));
    }
}