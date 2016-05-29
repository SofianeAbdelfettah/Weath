package etna.weather;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by Yunori on 04/05/2016.
 */
public class CustomNotif {
    private Context mCtx;
    int ih;
    int im;
    String city;
    String temp;
    String comment;
    String finaltext;

    CustomNotif(Context context, int h, int m, String cityd, int tempd, String commentd) {
        ih = h;
        im = m;
        mCtx = context;
        city = cityd;
        temp = Integer.toString(tempd);
        comment = commentd;
    }

    public int timeleft() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int finalmilli = 0;
        int reshour = 0;
        int resmin = 0;
        int ressec = 0;

        if (hour > ih || (hour == ih && min >= im)) {
            reshour = 24 + ih-1 - hour;
            resmin = im-1 - min;
            ressec = 60 - sec;
        } else {
            reshour = ih-1 - hour;
            if (reshour < 0)
                reshour = 0;
            resmin = im-1 - min;
            ressec = 60 - sec;
        }
        finalmilli = (((reshour * 60) + resmin) * 60 + ressec) * 1000;
        String lol = Integer.toString(finalmilli);
        Log.e("error PD ", lol);
        return finalmilli;
    }

    /*public void run() {
        finaltext = temp + "°\n" + comment;
        PugNotification.with(mCtx)
                .load()
                .title(city)
                .message(finaltext)
                .smallIcon(R.drawable.pugnotification_ic_launcher)
                .largeIcon(R.drawable.showerraincirclenoche)
                .flags(Notification.DEFAULT_ALL)
                .color(android.R.color.background_dark)
                .simple()
                .build();
    }*/

    public void launch() {
        finaltext = temp + "°\n" + comment;
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {

            @Override
            public void run() {
                finaltext = temp + "°\n" + comment;
                PugNotification.with(mCtx)
                        .load()
                        .title(city)
                        .message(finaltext)
                        .smallIcon(R.drawable.pugnotification_ic_launcher)
                        .largeIcon(R.drawable.showerraincirclenoche)
                        .flags(Notification.DEFAULT_ALL)
                        .color(android.R.color.background_dark)
                        .simple()
                        .build();
            }
        };
        timer.schedule(hourlyTask, 0, 3600000);
    }
}
