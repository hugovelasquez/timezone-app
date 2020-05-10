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
    private static List<String> timezones = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Assignment of variables from SharedPreferences
        WidgetPreferences widgetPreferences = new WidgetPreferences(context);
        timePattern = widgetPreferences.getStoredTimePattern();
        // Fill the string list selectedCities - remember: first item has index zero
        selectedCities.add(widgetPreferences.getStoredCitySelection(context.getString(R.string.selected_city1_key)));
        selectedCities.add(widgetPreferences.getStoredCitySelection(context.getString(R.string.selected_city2_key)));
        selectedCities.add(widgetPreferences.getStoredCitySelection(context.getString(R.string.selected_city3_key)));
        selectedCities.add(widgetPreferences.getStoredCitySelection(context.getString(R.string.selected_city4_key)));
        datePattern = context.getString(R.string.date_format); // Default value
        // Assignment of timezone based on city selection
        for (String selectedCity : selectedCities) {
            // Fill the string list timezones - remember: first item has index zero
            timezones.add(getTimezoneOfSpinnerSelectedCities(selectedCity, context));
        }

        Log.e("WIDGET", "selected timezone 1 is " + timezones.get(0));
        Log.e("WIDGET", "selected timezone 2 is " + timezones.get(1));
        Log.e("WIDGET", "selected timezone 3 is " + timezones.get(2));
        Log.e("WIDGET", "selected timezone 4 is " + timezones.get(3));

        // Link the xml layout file to this activity (WidgetProvider)
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timezone_widget);

        // Android calculates current Date
        currentDate = new Date();

        // Call method to set Header, Time and Date of my desired locations
        setHeaderTimeDateOfLocation(timezones.get(0),selectedCities.get(0),R.id.header_row1, R.id.time_row1,R.id.date_row1,views);
        setHeaderTimeDateOfLocation(timezones.get(1),selectedCities.get(1),R.id.header_row2, R.id.time_row2,R.id.date_row2,views);
        setHeaderTimeDateOfLocation(timezones.get(2),selectedCities.get(2),R.id.header_row3, R.id.time_row3,R.id.date_row3,views);
        setHeaderTimeDateOfLocation(timezones.get(3),selectedCities.get(3),R.id.header_row4, R.id.time_row4,R.id.date_row4,views);
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


    private static void setHeaderTimeDateOfLocation(String timezone, String selectedCity, int headerViewId, int timeViewId, int dateViewId, RemoteViews views){
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

    private static String getTimezoneOfSpinnerSelectedCities (String selectedCity, Context context){
        String selectedTimezone = "";
        if (selectedCity.equals(context.getString(R.string.text_sydney))){
            selectedTimezone = context.getString(R.string.sydney_timezone);
        } else if (selectedCity.equals(context.getString(R.string.text_bochum))){
            selectedTimezone = context.getString(R.string.bochum_timezone);
        } else if (selectedCity.equals(context.getString(R.string.text_nyc))){
            selectedTimezone = context.getString(R.string.nyc_timezone);
        } else if (selectedCity.equals(context.getString(R.string.text_sivar))){
            selectedTimezone = context.getString(R.string.sivar_timezone);
        } else if (selectedCity.equals(context.getString(R.string.text_toronto))){
            selectedTimezone = context.getString(R.string.toronto_timezone);
        }
        return selectedTimezone;
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

