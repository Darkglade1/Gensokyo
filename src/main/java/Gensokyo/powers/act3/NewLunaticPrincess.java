package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.NewImpossibleRequests.NewImpossibleRequest;
import Gensokyo.monsters.act3.Mokou;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;


public class NewLunaticPrincess extends AbstractPower implements InvisiblePower {

    public static final String POWER_ID = GensokyoMod.makeID("NewLunaticPrincess");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private Mokou mokou;
    private NewImpossibleRequest request;
    public int counter = 0;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Infinity84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Infinity32.png"));

    public NewLunaticPrincess(AbstractCreature owner, Mokou mokou, NewImpossibleRequest request) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.mokou = mokou;
        this.request = request;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (request.requestCounter == NewImpossibleRequest.LUNAR_ILMENITE) {
            if (card.type == AbstractCard.CardType.POWER) {
                counter++;
                updateRequest();
                if (counter >= request.lunarIlmenite.magicNumber) {
                    this.flash();
                    request.completed = true;
                    counter = 0;
                    updateRequest();
                }
            }
        } else if (request.requestCounter == NewImpossibleRequest.RED_STONE) {
            if (card.type == AbstractCard.CardType.SKILL) {
                counter++;
                updateRequest();
                if (counter >= request.redStoneOfAja.magicNumber) {
                    this.flash();
                    request.completed = true;
                    counter = 0;
                    updateRequest();
                }
            }
        }
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            if (request.requestCounter == NewImpossibleRequest.SEAMLESS_CEILING) {
                if (AbstractDungeon.player.hand.size() >= request.seamlessCeiling.magicNumber) {
                    this.flash();
                    request.completed = true;
                    counter = 0;
                } else {
                    if (AbstractDungeon.player.hand.size() > counter) {
                        counter = AbstractDungeon.player.hand.size();
                    }
                }
                updateRequest();
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (request.requestCounter == NewImpossibleRequest.MYSTERIUM) {
            if (info.owner == AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
                if (info.output >= request.mysterium.magicNumber) {
                    this.flash();
                    request.completed = true;
                    counter = 0;
                } else {
                    if (info.output > counter) {
                        counter = info.output;
                    }
                }
                updateRequest();
            }
        }
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        if (request.requestCounter == NewImpossibleRequest.TREE_OCEAN) {
            counter += blockAmount;
            updateRequest();
            if (counter >= request.treeOceanOfHourai.magicNumber) {
                this.flash();
                request.completed = true;
                counter = 0;
                updateRequest();
            }
        }
    }

    private void updateRequest() {
        if (request.cardToTransform == null) {
            return;
        }
        request.rawDescription = languagePack.getCardStrings(request.cardToTransform.cardID).DESCRIPTION;
        if (request.completed) {
            request.rawDescription += DESCRIPTIONS[11];
        } else {
            if (request.requestCounter == NewImpossibleRequest.LUNAR_ILMENITE) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[0];
                    request.rawDescription += counter;
                    if (counter == 1) {
                        request.rawDescription += DESCRIPTIONS[1];
                    } else {
                        request.rawDescription += DESCRIPTIONS[2];
                    }
                }
            }
            if (request.requestCounter == NewImpossibleRequest.RED_STONE) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[0];
                    request.rawDescription += counter;
                    if (counter == 1) {
                        request.rawDescription += DESCRIPTIONS[3];
                    } else {
                        request.rawDescription += DESCRIPTIONS[4];
                    }
                }
            }
            if (request.requestCounter == NewImpossibleRequest.SEAMLESS_CEILING) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[5];
                    request.rawDescription += counter;
                    if (counter == 1) {
                        request.rawDescription += DESCRIPTIONS[6];
                    } else {
                        request.rawDescription += DESCRIPTIONS[7];
                    }
                }
            }
            if (request.requestCounter == NewImpossibleRequest.MYSTERIUM) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[5];
                    request.rawDescription += counter + DESCRIPTIONS[8];
                }
            }
            if (request.requestCounter == NewImpossibleRequest.TREE_OCEAN) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[9];
                    request.rawDescription += counter;
                    request.rawDescription += DESCRIPTIONS[10];
                }
            }
        }
        request.initializeDescription();
    }

    public void pointToNewRequest(NewImpossibleRequest request) {
        this.request = request;
        mokou.request = request;
        updateRequest();
    }

    @Override
    public void updateDescription() {
        description = "";
    }
}
