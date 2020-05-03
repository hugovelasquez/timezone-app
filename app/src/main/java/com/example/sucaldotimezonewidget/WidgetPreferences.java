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

    // Key under which time Pattern will be stored ("TIME_PATTERN")
    public void setTimePatternToStore(String timePattern) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.time_pattern_key),timePattern);
        editor.commit();
    }
    // Method for retrieving stored value
    public String getStoredTimePattern() {
        return sharedPreferences.getString(context.getString(R.string.time_pattern_key),null);
    }
    // Shorter version of defining an If-loop (return only if true)
    public boolean isTimeKeyPresent() {
        return sharedPreferences.contains(context.getString(R.string.time_pattern_key));
    }
}