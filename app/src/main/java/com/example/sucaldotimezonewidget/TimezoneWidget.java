package com.example.sucaldotimezonewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Implementation of App Widget functionality.
 */
public class TimezoneWidget extends AppWidgetProvider {

    private static String timePattern;
    private static String datePattern;
    private static Date currentDate;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        WidgetPreferences widgetPreferences = new WidgetPreferences(context);
        timePattern = widgetPreferences.getStoredTimePattern();
        datePattern = context.getString(R.string.date_format); // Default value

        // Link the xml layout file to this activity (WidgetProvider)
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timezone_widget);
        // Android calculates current Date
        currentDate = new Date();
        // Call method to set Time and Date of my desired locations
        setTimeDateOfLocation(context.getString(R.string.sydney_timezone),R.id.time_sydney,R.id.date_sydney,views);
        setTimeDateOfLocation(context.getString(R.string.nyc_timezone),R.id.time_nyc,R.id.date_nyc,views);
        setTimeDateOfLocation(context.getString(R.string.sivar_timezone),R.id.time_sivar,R.id.date_sivar,views);
        setTimeDateOfLocation(context.getString(R.string.bochum_timezone),R.id.time_bochum,R.id.date_bochum,views);
        Log.d("WIDGET", "finished setting time zones");


        /*
        CODE FOR REFRESHING INFORMATION
         */
        // Create a new intent that links to this java class
        Intent intentUpdate = new Intent(context,TimezoneWidget.class);
        // set the update action to the intent. The app knows it has to launch an update
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Get the app Id into a new array because the update needs it
        int [] idArray = new int[] {appWidgetId};
        // Pass on the app id with the intent
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray);
        // "Transform" the intent into a PendingIntent (widget only works with Pending Intents...)
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,appWidgetId,intentUpdate,PendingIntent.FLAG_UPDATE_CURRENT);
        // Assign the Pending Intent to the xml refresh button
        views.setOnClickPendingIntent(R.id.refresh_button,pendingUpdate);
        /*
        END OF CODE FOR REFRESHING INFORMATION
         */

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static void setTimeDateOfLocation (String timezone, int timeViewId, int dateViewId, RemoteViews views){
        String locationTime = getTimeOfLocationFormatted(timezone);
        views.setTextViewText(timeViewId, locationTime);
        String locationDate = getDateOfLocationFormatted(timezone);
        views.setTextViewText(dateViewId, locationDate);
    }

    private static String getTimeOfLocationFormatted(String timezone) {
        // Calculates time and date of a timezone other than mine
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern); //HH = 24h format, hh = 12h format, aa = am/pm legend
        String formattedTime = timeFormat.format(currentDate);
        return formattedTime;
    }

    private static String getDateOfLocationFormatted(String timezone) {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern); //EEEE = day of the week, dd = day, MMM = month, yyyy = year
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
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

