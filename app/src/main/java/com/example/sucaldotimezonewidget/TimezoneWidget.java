package com.example.sucaldotimezonewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Implementation of App Widget functionality.
 */
public class TimezoneWidget extends AppWidgetProvider {

    // Definition of variables
    private static String timePattern;
    private static String datePattern;
    private static Date currentDate;
    private static List<String> selectedCities = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Assignment of variables from SharedPreferences
        WidgetPreferences widgetPreferences = new WidgetPreferences(context);
        timePattern = widgetPreferences.getStoredTimePattern();
        selectedCities = widgetPreferences.getSelectedCitiesList();

        // Default value for date format
        datePattern = context.getString(R.string.date_format);

        // Link the xml layout file to this activity (WidgetProvider)
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timezone_widget);

        // Android calculates current Date
        currentDate = new Date();

        // Clean widget before setting new data
        cleanWidgetTextViews(R.id.header_row1, R.id.time_row1, R.id.date_row1, views);
        cleanWidgetTextViews(R.id.header_row2, R.id.time_row2, R.id.date_row2, views);
        cleanWidgetTextViews(R.id.header_row3, R.id.time_row3, R.id.date_row3, views);
        cleanWidgetTextViews(R.id.header_row4, R.id.time_row4, R.id.date_row4, views);

        // Set widget data according to selected cities in settings
        // Widget does not allow dynamic adding of rows, therefore this static approach
        for (int i = 0; i < selectedCities.size(); i++) {
            int header = 0;
            int time = 0;
            int date = 0;
            if (i == 0) {
                header = R.id.header_row1;
                time = R.id.time_row1;
                date = R.id.date_row1;
            }
            if (i == 1) {
                header = R.id.header_row2;
                time = R.id.time_row2;
                date = R.id.date_row2;
            }
            if (i == 2) {
                header = R.id.header_row3;
                time = R.id.time_row3;
                date = R.id.date_row3;
            }
            if (i == 3) {
                header = R.id.header_row4;
                time = R.id.time_row4;
                date = R.id.date_row4;
            }
            String timezone = widgetPreferences.getTimezoneOfCity(selectedCities.get(i));
            setHeaderTimeDateOfLocation(timezone, selectedCities.get(i), header, time, date, views);
        }

        /*
        CODE FOR REFRESHING INFORMATION
         */
        // Create a new intent that links to this java class
        Intent intentUpdate = new Intent(context, TimezoneWidget.class);
        // set the update action to the intent. The app knows it has to launch an update
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Get the app Id into a new array because the update needs it
        int[] idArray = new int[]{appWidgetId};
        // Pass on the app id with the intent
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        // "Transform" the intent into a PendingIntent (widget only works with Pending Intents...)
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        // Assign the Pending Intent to the xml refresh button
        views.setOnClickPendingIntent(R.id.refresh_button, pendingUpdate);
        /*
        END OF CODE FOR REFRESHING INFORMATION
         */

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void cleanWidgetTextViews(int headerViewId, int timeViewId, int dateViewId, RemoteViews views) {
        views.setTextViewText(headerViewId, "");
        views.setTextViewText(timeViewId, "");
        views.setTextViewText(dateViewId, "");
    }

    private static void setHeaderTimeDateOfLocation(String timezone, String selectedCity, int headerViewId, int timeViewId, int dateViewId, RemoteViews views) {
        views.setTextViewText(headerViewId, selectedCity);
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

