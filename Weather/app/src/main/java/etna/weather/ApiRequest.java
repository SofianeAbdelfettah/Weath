package etna.weather;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yunori on 06/05/2016.
 */
public class ApiRequest {
    String url;
    private static Context mCtx;
    public String Temperature;
    String description;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 100000; // in Milliseconds
    protected LocationManager locationManager;
    TextView name;
    TextView temp;
    TextView humid;
    TextView status;
    TextView wind;
    int widget;
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    private String place;

    ApiRequest(Context context, String surl, final TextView name, final TextView temp, final TextView humid, final TextView MainStatus, final TextView wind, int widget) {
        this.name = name;
        this.temp = temp;
        this.humid = humid;
        this.status = MainStatus;
        this.wind = wind;
        this.widget = widget;
        mCtx = context;
        url = surl;
    }
    public void go()
    {
        if (url == null)
            locate();
        else {
            requete(name, temp, humid, status);
        }
    }

    public void requete(final TextView name, final TextView temp, final TextView humid, final TextView MainStatus) {
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject Jobject = new JSONObject(response);
                            String TownName = Jobject.getString("name");
                            if(name != null)
                                name.setText(TownName);

                            JSONArray status = Jobject.getJSONArray("weather");
                            for (int i = 0; i < status.length(); i++)
                            {
                                JSONObject jsonobject = status.getJSONObject(i);
                                description = jsonobject.getString("description");
                                if(MainStatus != null)
                                    MainStatus.setText("État : "+description);
                            }

                            // Température
                            String mainString = Jobject.getString("main");
                            JSONObject  mainObj = new JSONObject(mainString);
                            setTemperature(mainObj.getString("temp"));
                            int Tempint = getInt();
                            if(temp != null)
                                temp.setText("Température Actuelle : "+Tempint+"°");
                            // Température

                            String Humidity = mainObj.getString("humidity");
                            if(humid != null)
                                humid.setText("Humidité : "+Humidity+" %");

                            JSONObject windobj = Jobject.getJSONObject("wind");
                            String windSpeed = windobj.getString("speed");

                            wind.setText("Vitesse du vent "+windSpeed+" Km/h");
                            //WIDGET
                            if(widget == 1) {
                                String ok = Tempint + "";
                                String windspeed = windobj.getString("speed");
                                PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("Ville", TownName).apply();
                                PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("état", description).apply();
                                if (description.equals("Couvert") || description.equals("nuageux")) {
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putInt("pic", R.drawable.kumo).apply();
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("msgmarrant", "Le soleil joue a cache cache, impossible de le trouver").apply();
                                } else if (description.equals("légères pluies")) {
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putInt("pic", R.drawable.rain).apply();
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("msgmarrant", "J'espere que tu as pris ton parapluie").apply();

                                } else if (description.equals("ensoleillé")) {
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putInt("pic", R.drawable.sun).apply();
                                    PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("msgmarrant", "C'est quoi ce truc jaune ? ah oui le soleil").apply();
                                }
                                PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("Temps", ok).apply();
                                PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("Humide", Humidity).apply();
                                PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putString("Vent", windspeed).apply();
                                // FIN WIDGET
                            }
                            int s = ((Weather) mCtx.getApplicationContext()).getnotif();
                            if(getPlace() != null && s != 1) {
                                ((Weather) mCtx.getApplicationContext()).setnotif(1);
                                CustomNotif fck = new CustomNotif(mCtx, 18, 0, getPlace(), getInt(), getDesc());
                                fck.launch();
                            }
                            else
                                setPlace(TownName);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public int getInt()
    {
        int tempMath = (int) Math.round(Double.parseDouble(getTemperature()));
        return (tempMath);
    }
    public String getDesc()
    {
        return description;
    }
    public String getTemperature() { return Temperature; }
    public void setTemperature(String temperature) { Temperature = temperature; }


    public void locate() {
        System.out.println("Getting Location");
        locationManager = (LocationManager) mCtx.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new CustomLocationListener(mCtx)
        );
        showCurrentLocation();
    }
    public void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            String message = String.format
                    (
                            "Localisation..."
                    );
            Toast.makeText(mCtx, message,
                    Toast.LENGTH_SHORT).show();

            double intlon =  location.getLongitude();
            double intlat =  location.getLatitude();

            Geocoder gcd = new Geocoder(mCtx, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(intlat, intlon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());

                setPlace(addresses.get(0).getLocality());

                url = "http://api.openweathermap.org/data/2.5/weather?q=" + addresses.get(0).getLocality() + "&lang=fr&units=metric&APPID=34e6bd70a2df23135e9b9de80284ea67";
                requete(name, temp, humid, status);
                Toast.makeText(mCtx, "Ville : " + addresses.get(0).getLocality().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}