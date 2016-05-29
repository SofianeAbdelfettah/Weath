package etna.weather;

import android.app.Application;

/**
 * Created by Yunori on 12/05/2016.
 */
public class Weather extends Application {

    private String background;
    private int notif = 0;

    public int getnotif() {
        return notif;
    }
    public void setnotif(int notif) {
        this.notif = notif;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
