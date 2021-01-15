package Gensokyo.CardMods;

import Gensokyo.GensokyoMod;
import Gensokyo.relics.act2.UndefinedDarkness;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ObscureMod extends AbstractCardModifier {

    static Texture attackImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOAttack.png"));
    static TextureAtlas.AtlasRegion attackImgAtlas = new TextureAtlas.AtlasRegion(attackImg, 0, 0, attackImg.getWidth(), attackImg.getHeight());

    static Texture skillImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOSkill.png"));
    static TextureAtlas.AtlasRegion skillImgAtlas = new TextureAtlas.AtlasRegion(skillImg, 0, 0, skillImg.getWidth(), skillImg.getHeight());

    static Texture powerImg = TextureLoader.getTexture(GensokyoMod.makeCardPath("UFOPower.png"));
    static TextureAtlas.AtlasRegion powerImgAtlas = new TextureAtlas.AtlasRegion(powerImg, 0, 0, powerImg.getWidth(), powerImg.getHeight());

    public static final String ID = GensokyoMod.makeID("ObscureMod");
    private static String obscuredText = CardCrawlGame.languagePack.getRelicStrings(UndefinedDarkness.ID).DESCRIPTIONS[2];

    private TextureAtlas.AtlasRegion originalTexture;
    private String originalName;

    public ObscureMod() {
        this.priority = 999; //need this to go last for modifyDescription
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ObscureMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        originalTexture = card.portrait;
        originalName = card.name;

        if (card.type == AbstractCard.CardType.ATTACK) {
            card.portrait = attackImgAtlas;
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            card.portrait = skillImgAtlas;
        }
        if (card.type == AbstractCard.CardType.POWER) {
            card.portrait = powerImgAtlas;
        }
        card.name = obscuredText;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return obscuredText;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.portrait = originalTexture;
        card.name = originalName;
    }
}
