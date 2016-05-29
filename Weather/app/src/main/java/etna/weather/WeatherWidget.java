package etna.weather;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        //état
        String etat = PreferenceManager.getDefaultSharedPreferences(context).getString("état","error");
        views.setTextViewText(R.id.état, etat);

        //msgmarrant
        String msgmarrant = PreferenceManager.getDefaultSharedPreferences(context).getString("msgmarrant","error");
        views.setTextViewText(R.id.msgmarrant, msgmarrant);

//Vent
        String vent = PreferenceManager.getDefaultSharedPreferences(context).getString("Vent","error");
        views.setTextViewText(R.id.vent, "vitesse du vent : "+vent+"kmh");

        //Ville
        String ville = PreferenceManager.getDefaultSharedPreferences(context).getString("Ville","error");
        views.setTextViewText(R.id.ville, ville);

        //Humid
        String humid = PreferenceManager.getDefaultSharedPreferences(context).getString("Humide","error");
        views.setTextViewText(R.id.humide, "humidité : "+humid+"%");

        //Temps
        String weather = PreferenceManager.getDefaultSharedPreferences(context).getString("Temps", "error");
        views.setTextViewText(R.id.temps, weather+"°C");

        //photo
        int pic = PreferenceManager.getDefaultSharedPreferences(context).getInt("pic",R.drawable.rain);
        RemoteViews views5 = new RemoteViews(context.getPackageName(), pic);
        views.setImageViewResource(R.id.picwidget, pic);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

