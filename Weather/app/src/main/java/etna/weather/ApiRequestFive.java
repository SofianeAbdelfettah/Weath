package etna.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.ListView;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yunori on 12/05/2016.
 */
public class ApiRequestFive {
    private Context context;
    ArrayList<String> values = new ArrayList<String>();
    ArrayList<String> Imagesvalues = new ArrayList<String>();
    private String desc;
    private String ALLInfo;
    ListView mListView;
    private CustomAdapter adaptateur;
    String url;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 100000; // in Milliseconds
    protected LocationManager locationManager;

    ApiRequestFive(Context context, String surl, ArrayList<String> values, ArrayList<String> Imagesvalues, ListView listview) {
        this.context = context;
        this.values = values;
        this.Imagesvalues = Imagesvalues;
        this.mListView = listview;
        this.url = surl;
    }
    public void go()
    {
        if (url == null)
            locate();
        else {
            requete();
        }
    }
    public void locate() {
        System.out.println("Getting Location");
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new CustomLocationListener(context)
        );
        showCurrentLocation();
    }
    public void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            String message = String.format
                    (
                            "Localisation..."
                    );
            Toast.makeText(context, message,
                    Toast.LENGTH_SHORT).show();

            double intlon = location.getLongitude();
            double intlat = location.getLatitude();

            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(intlat, intlon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());

                this.url = "http://api.openweathermap.org/data/2.5/forecast?q=" + addresses.get(0).getLocality() + "&lang=fr&units=metric&APPID=34e6bd70a2df23135e9b9de80284ea67";
                requete();
                Toast.makeText(context, "Ville : " + addresses.get(0).getLocality().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    public void requete() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response");
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONArray list = Jobject.getJSONArray("list");

                            for (int k = 0; k < list.length(); k++) {
                                JSONObject o = list.getJSONObject(k);
                                String m = o.getString("weather");
                                JSONArray a = new JSONArray(m);
                                JSONObject aobject = a.getJSONObject(0);
                                desc = aobject.getString("description");

                                String icon = aobject.getString("icon");
                                String iconImages = "http://openweathermap.org/img/w/" + icon + ".png";

                                JSONObject alllist = list.getJSONObject(k);

                                String dt = alllist.getString("dt");
                                long timestamp = Long.parseLong(dt) * 1000;

                                DateFormat sdf = new SimpleDateFormat("E dd/MM  HH:mm ", Locale.FRENCH);
                                Date netDate = (new Date(timestamp));
                                String FUllDate = sdf.format(netDate);

                                String main2 = alllist.getString("main");
                                JSONObject main2ToObject = new JSONObject(main2);
                                String temp = main2ToObject.getString("temp");
                                double tempRound = Math.round(Double.parseDouble(String.valueOf(temp)));
                                int doubleToInt = (int) tempRound;
                                int tempint = Integer.parseInt(String.valueOf(doubleToInt));

                                ALLInfo = FUllDate + "\n" + "Température moyenne : " + tempint + "° " + desc;
                                values.add(ALLInfo);
                                Imagesvalues.add(iconImages);
                            }
                            adaptateur = new CustomAdapter(context, values, Imagesvalues);
                            mListView.setAdapter(adaptateur);
                            //adaptateur.setnew(values, Imagesvalues);
                            //adaptateur.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("No response");
            }
        });
        queue.add(stringRequest);
    }
}
