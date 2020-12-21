package Gensokyo.cards;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class CustomPotion extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(CustomPotion.class.getSimpleName());
    public static final String IMG = makeCardPath("PotionSkill.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -1;

    public CustomPotion() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.type == CardType.ATTACK) {
            final Texture texture = TextureLoader.getTexture(makeCardPath("PotionAttack_p.png"));
            return texture;
        } else {
            return super.getPortraitImage();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}
