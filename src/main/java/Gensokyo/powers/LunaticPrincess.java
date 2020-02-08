package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import Gensokyo.actions.SetFlipAction;
import Gensokyo.cards.ImpossibleRequests.ImpossibleRequest;
import Gensokyo.monsters.bossRush.Kaguya;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;


public class LunaticPrincess extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("LunaticPrincess");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private Kaguya kaguya;
    private ImpossibleRequest request;
    private boolean SKILL = false;
    private boolean POWER = false;
    private boolean ATTACK = false;
    private boolean tookDamage = false;
    private int counter = 0;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Evasive84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Evasive32.png"));

    public LunaticPrincess(AbstractCreature owner, Kaguya kaguya, ImpossibleRequest request) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
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
                request.requestCounter++;
                request.transform();
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
                request.requestCounter++;
                request.transform();
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
                request.requestCounter++;
                request.transform();
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
                    request.requestCounter++;
                    request.transform();
                    counter = 0;
                    updateRequest();
                }
            }
        }
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (request.requestCounter == ImpossibleRequest.SWALLOW_SHELL) {
            if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
                System.out.println("took damage");
                tookDamage = true;
            }
        }
    }

    @Override
    public void atEndOfRound() {
        if (request.requestCounter == ImpossibleRequest.SWALLOW_SHELL) {
            System.out.println(tookDamage);
            if (tookDamage) {
                counter = 0;
                updateRequest();
            } else {
                counter++;
                updateRequest();
                if (counter >= request.swallowsCowrieShell.magicNumber) {
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (mo instanceof Kaguya) {
                            AbstractDungeon.getCurrRoom().cannotLose = false;
                            AbstractDungeon.actionManager.addToBottom(new TalkAction(kaguya, Kaguya.DIALOG[1]));
                            AbstractDungeon.actionManager.addToBottom(new SetFlipAction(kaguya));
                            AbstractDungeon.actionManager.addToBottom(new EscapeAction(kaguya));
                        } else {
                            mo.die();;
                        }
                    }
                }
            }
            tookDamage = false;
        }
    }

    private void updateRequest() {
        request.rawDescription = languagePack.getCardStrings(request.cardToTransform.cardID).DESCRIPTION;
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
                if (counter == 1) {
                    request.rawDescription += DESCRIPTIONS[13];
                } else {
                    request.rawDescription += DESCRIPTIONS[14];
                }
            }
        }
        request.initializeDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
