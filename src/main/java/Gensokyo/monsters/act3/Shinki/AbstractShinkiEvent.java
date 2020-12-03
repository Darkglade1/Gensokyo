
package Gensokyo.monsters.act3.Shinki;

import java.util.ArrayList;

public abstract class AbstractShinkiEvent {

    public String title;
    public String bodyText;
    public String image;
    ArrayList<String> options;

    public AbstractShinkiEvent(String title, String bodyText, ArrayList<String> options) {
        this.title = title;
        this.bodyText = bodyText;
        this.options = options;
    }

    public abstract void buttonEffect(int buttonPressed);


}
