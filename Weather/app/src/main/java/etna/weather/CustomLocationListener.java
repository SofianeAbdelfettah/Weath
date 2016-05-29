package etna.weather;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Yunori on 10/05/2016.
 */
public class CustomLocationListener implements LocationListener {
    Context context;
    CustomLocationListener(Context mCtx){
        this.context = mCtx;
    }
    public void onLocationChanged(Location location) {
//        String message = String.format(
//                "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                location.getLongitude(), location.getLatitude()
//        );
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String s, int i, Bundle b) {
        Toast.makeText(context, "Provider status changed",
                Toast.LENGTH_LONG).show();
    }

    public void onProviderDisabled(String s) {
        Toast.makeText(context,
                "Provider disabled by the user. Internet turned off",
                Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String s) {
        Toast.makeText(context, "Provider enabled by the user. Internet turned on",Toast.LENGTH_LONG).show();
    }
}
