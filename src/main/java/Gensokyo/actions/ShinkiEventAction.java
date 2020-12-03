package Gensokyo.actions;

import Gensokyo.monsters.act3.Shinki.Shinki;
import Gensokyo.patches.RenderHandPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.events.GenericEventDialog;

public class ShinkiEventAction extends AbstractGameAction
{
	private boolean hasBuilt;
	private Shinki shinki;
	
    public ShinkiEventAction(Shinki shinki) {
		this.hasBuilt = false;
		this.shinki = shinki;
    }
    
    @Override
    public void update() {
		if (!this.hasBuilt) {
			shinki.imageEventText.show();
			this.hasBuilt = true;
			RenderHandPatch.plsDontRenderHand = true;
		}
		shinki.imageEventText.update();
        if (!GenericEventDialog.waitForInput && !this.isDone) {
            shinki.currentEvent.buttonEffect(shinki.imageEventText.getSelectedOption());
            RenderHandPatch.plsDontRenderHand = false;
			this.isDone = true;
        }
    }
}