package com.example.sucaldotimezonewidget;

import android.content.Context;
import android.content.SharedPreferences;

public class WidgetPreferences {

    // SharedPreferences stores constant values outside of the App so that they are available even
    // after relaunching the App
    private SharedPreferences sharedPreferences;
    private Context context;

    public WidgetPreferences(Context context) {
        this.context = context;
        // R.string.widget_settings is just an ID
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.widget_settings), Context.MODE_PRIVATE);
    }

    // Method for storing time Pattern (Key for storing is R.string.time_pattern_key)
    public void setTimePatternToStore(String timePattern) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.time_pattern_key),timePattern);
        editor.commit();
    }

    // Method for storing city selection (Key for storing is now an input for the method)
    public void setCitySelectionToStore(String citySelection, String storeKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(storeKey, citySelection);
        editor.commit();
    }

    // Method for retrieving stored time pattern value
    public String getStoredTimePattern() {
        if(isTimeKeyPresent()) {
            return sharedPreferences.getString(context.getString(R.string.time_pattern_key),null);
        }
        return context.getString(R.string.hour_format_12);
    }

    // Method for retrieving stored city selection values
    public String getStoredCitySelection(String cityKey) {
        return sharedPreferences.getString(cityKey,null);
    }


    // Shorter version of defining an If-loop (return only if true)
    public boolean isTimeKeyPresent() {
        return sharedPreferences.contains(context.getString(R.string.time_pattern_key));
    }
}
