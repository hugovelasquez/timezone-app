package com.example.sucaldotimezonewidget;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class WidgetPreferences {

    // SharedPreferences stores constant values outside of the App so that they are available even
    // after relaunching the App
    private SharedPreferences sharedPreferences;
    private Context context;
    private Map<String, String> locationMap;

    public WidgetPreferences(Context context) {
        this.context = context;
        // R.string.widget_settings is just an ID
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.widget_settings), Context.MODE_PRIVATE);
        // Map creates a two-column list (in this case column 1 is city name, column 2 the complete timezone)
        locationMap = new HashMap<>();
        locationMap.put(context.getString(R.string.text_sydney), context.getString(R.string.sydney_timezone));
        locationMap.put(context.getString(R.string.text_bochum), context.getString(R.string.bochum_timezone));
        locationMap.put(context.getString(R.string.text_sydney), context.getString(R.string.sydney_timezone));
        locationMap.put(context.getString(R.string.text_sydney), context.getString(R.string.sydney_timezone));
    }

    // Method for storing time Pattern (Key for storing is R.string.time_pattern_key)
    public void setTimePatternToStore(String timePattern) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.time_pattern_key), timePattern);
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
        if (isTimeKeyPresent()) {
            return sharedPreferences.getString(context.getString(R.string.time_pattern_key), null);
        }
        return context.getString(R.string.hour_format_12);
    }

    // Method for retrieving stored city selection values
    public String getStoredCitySelection(String cityKey) {
        return sharedPreferences.getString(cityKey, null);
    }

    // Shorter version of defining an If-loop (return only if sharedPreferences contains time Key)
    public boolean isTimeKeyPresent() {
        return sharedPreferences.contains(context.getString(R.string.time_pattern_key));
    }

    // Shorter version of defining an If-loop (return only if sharedPreferences contains cityKey)
    public boolean isCityKeyPresent(String cityKey) {
        return sharedPreferences.contains(cityKey);
    }

    // Method for getting the Timezone of the city selected via spinners
    public String getTimezoneOfCity(String city) {
        // Map returns the value of the second column that corresponds to the input in the first column
        return locationMap.get(city);
    }
}
