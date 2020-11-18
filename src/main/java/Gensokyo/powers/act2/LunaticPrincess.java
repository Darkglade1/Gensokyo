package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.ImpossibleRequests.ImpossibleRequest;
import Gensokyo.monsters.act2.Kaguya;
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


public class LunaticPrincess extends AbstractPower implements InvisiblePower {

    public static final String POWER_ID = GensokyoMod.makeID("LunaticPrincess");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private Kaguya kaguya;
    private ImpossibleRequest request;
    private boolean SKILL = false;
    private boolean POWER = false;
    private boolean ATTACK = false;
    public int counter = 0;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Infinity84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Infinity32.png"));

    public LunaticPrincess(AbstractCreature owner, int amount, Kaguya kaguya, ImpossibleRequest request) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.kaguya = kaguya;
        this.request = request;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (request.requestCounter == ImpossibleRequest.BUDDHA_BOWL) {
            SKILL = false;
            POWER = false;
            ATTACK = false;
        }
        if (request.requestCounter == ImpossibleRequest.BULLET_BRANCH) {
            counter = 0;
        }
        if (request.requestCounter == ImpossibleRequest.FIRE_RAT) {
            counter = 0;
        }
        if (request.requestCounter == ImpossibleRequest.JEWEL_FROM_DRAGON) {
            counter = 0;
        }
        if (request.requestCounter == ImpossibleRequest.SWALLOW_SHELL) {
            counter = 0;
        }
        updateRequest();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (request.requestCounter == ImpossibleRequest.BUDDHA_BOWL) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                ATTACK = true;
                updateRequest();
            } else if (card.type == AbstractCard.CardType.SKILL) {
                SKILL = true;
                updateRequest();
            } else if (card.type == AbstractCard.CardType.POWER) {
                POWER = true;
                updateRequest();
            }

            if (ATTACK && SKILL && POWER) {
                this.flash();
                request.completed = true;
                updateRequest();
                SKILL = false;
                POWER = false;
                ATTACK = false;
            }
        } else if (request.requestCounter == ImpossibleRequest.BULLET_BRANCH) {
            counter++;
            updateRequest();
            if (counter >= request.bulletBranchOfHourai.magicNumber) {
                this.flash();
                request.completed = true;
                counter = 0;
                updateRequest();
            }
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (request.requestCounter == ImpossibleRequest.FIRE_RAT) {
            counter++;
            updateRequest();
            if (counter >= request.fireRatsRobe.magicNumber) {
                this.flash();
                request.completed = true;
                counter = 0;
                updateRequest();
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (request.requestCounter == ImpossibleRequest.JEWEL_FROM_DRAGON) {
            if (info.owner == AbstractDungeon.player && damageAmount > 0) {
                counter += damageAmount;
                updateRequest();
                if (counter >= request.jewelFromDragon.magicNumber) {
                    this.flash();
                    request.completed = true;
                    counter = 0;
                    updateRequest();
                }
            }
        }
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        if (request.requestCounter == ImpossibleRequest.SWALLOW_SHELL) {
            counter += blockAmount;
            updateRequest();
            if (counter >= request.swallowsCowrieShell.magicNumber) {
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
            request.rawDescription += DESCRIPTIONS[15];
        } else {
            if (request.requestCounter == ImpossibleRequest.BUDDHA_BOWL) {
                if (ATTACK || SKILL || POWER) {
                    request.rawDescription += DESCRIPTIONS[1];
                    if (ATTACK) {
                        request.rawDescription += DESCRIPTIONS[2];
                    }
                    if (SKILL) {
                        if (ATTACK) {
                            request.rawDescription += DESCRIPTIONS[5];
                        }
                        request.rawDescription += DESCRIPTIONS[3];
                    }
                    if (POWER) {
                        if (ATTACK || SKILL) {
                            request.rawDescription += DESCRIPTIONS[5];
                        }
                        request.rawDescription += DESCRIPTIONS[4];
                    }
                    request.rawDescription += DESCRIPTIONS[6];
                }
            }
            if (request.requestCounter == ImpossibleRequest.BULLET_BRANCH) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[1];
                    request.rawDescription += counter;
                    if (counter == 1) {
                        request.rawDescription += DESCRIPTIONS[7];
                    } else {
                        request.rawDescription += DESCRIPTIONS[8];
                    }
                }
            }
            if (request.requestCounter == ImpossibleRequest.FIRE_RAT) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[9];
                    request.rawDescription += counter;
                    if (counter == 1) {
                        request.rawDescription += DESCRIPTIONS[7];
                    } else {
                        request.rawDescription += DESCRIPTIONS[8];
                    }
                }
            }
            if (request.requestCounter == ImpossibleRequest.JEWEL_FROM_DRAGON) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[10];
                    request.rawDescription += counter + DESCRIPTIONS[11];
                }
            }
            if (request.requestCounter == ImpossibleRequest.SWALLOW_SHELL) {
                if (counter > 0) {
                    request.rawDescription += DESCRIPTIONS[12];
                    request.rawDescription += counter;
                    request.rawDescription += DESCRIPTIONS[13];
                }
            }
        }
        request.initializeDescription();
    }

    public void pointToNewRequest(ImpossibleRequest request) {
        this.request = request;
        kaguya.request = request;
        updateRequest();
    }

    @Override
    public void updateDescription() {
        description = "";
    }
}
