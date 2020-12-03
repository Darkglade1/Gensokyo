
package Gensokyo.monsters.act3.Shinki;

import java.util.ArrayList;

public abstract class AbstractShinkiEvent {

    public String title;
    public String bodyText;
    public String image;
    ArrayList<String> options = new ArrayList<>();

    public AbstractShinkiEvent() {

    }

    public abstract void buttonEffect(int buttonPressed);


}
