package com.example.sucaldotimezonewidget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeCalculatorFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Definition of variables
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button calculateBtn;
    private TimePicker timePicker;
    private TableLayout calculatedTimesTable;

    private String selectedCity;
    private String timePattern;
    private List<Integer> row1Ids = new ArrayList<>();

    private WidgetPreferences widgetPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Link xml layout to this fragment
        final View rootView = inflater.inflate(R.layout.fragment_time_calculator, container, false);

        // Assignment of variables
        widgetPreferences = new WidgetPreferences(getContext());
        timePattern = widgetPreferences.getStoredTimePattern();

        spinner = rootView.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Reference to TableLayout ID. This variable is used later to programmatically add rows to TableLayout.
        calculatedTimesTable = rootView.findViewById(R.id.calculated_times_table);

        calculateBtn = rootView.findViewById(R.id.calculate_btn);

        // Button Listener
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTimeOfCities(rootView);
            }
        });

        // Set 12h or 24h format
        timePicker = rootView.findViewById(R.id.datePicker);
        if (timePattern.equals(getString(R.string.hour_format_24))) {
            timePicker.setIs24HourView(true);
        }

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
    }

    private void displayTimeOfCities(View rootView) {
        // Delete current Table rows in order to start from zero again
        removeCalculationRows(rootView);

        Calendar selectedTime = getTimeOfSelectedTimeAndCity();
        int dayOfSelectedTime = selectedTime.get(Calendar.DAY_OF_YEAR);

        List<String> cities = getCitiesToConvertTo();

        for (String city : cities) {
            String timezone = widgetPreferences.getTimezoneOfCity(city);

            String timeOfCity = getFormattedTimeOfOtherCity(selectedTime.getTime(), timezone);
            String relativeDay = getDayRelativeToSelectedTime(selectedTime.getTime(), dayOfSelectedTime);

            // Create new table row and give it a random ID
            TableRow tr = new TableRow(getContext());
            int rowId = View.generateViewId();
            row1Ids.add(rowId);
            tr.setId(rowId);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Create 3 TextViews in newly created row
            TextView labelCity = new TextView(getContext());
            setTextViewStyle(labelCity, 2, city);
            tr.addView(labelCity);

            TextView labelTime = new TextView(getContext());
            setTextViewStyle(labelTime, 2, timeOfCity);
            tr.addView(labelTime);

            TextView labelDayDifference = new TextView(getContext());
            setTextViewStyle(labelDayDifference, 1, relativeDay);
            tr.addView(labelDayDifference);

            // Add row to TableLayout
            calculatedTimesTable.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void removeCalculationRows(View rootView) {
        for (int id : row1Ids) {
            TableRow row = rootView.findViewById(id);
            calculatedTimesTable.removeView(row);
        }
        row1Ids.clear();
    }

    private void setTextViewStyle(TextView textView, float weight, String text) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        params.setMargins(25,20,5,0);
        textView.setLayoutParams(params);
        textView.setTextSize(17);
        textView.setText(text);
    }

    // Remove the city that was selected as basis from the list of cities to convert the time to
    private List<String> getCitiesToConvertTo() {
        List<String> cities = Arrays.asList(getResources().getStringArray(R.array.cities));
        // apk level does not support streams
        List<String> citiesToCalc = new ArrayList<>();
        for (String city : cities) {
            if (!city.equals(selectedCity)) {
                citiesToCalc.add(city);
            }
        }
        return citiesToCalc;
    }

    private Calendar getTimeOfSelectedTimeAndCity() {
        String selectedTimezone = widgetPreferences.getTimezoneOfCity(selectedCity);

        TimeZone.setDefault(TimeZone.getTimeZone(selectedTimezone));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());

        return calendar;
    }

    private String getFormattedTimeOfOtherCity(Date selectedTime, String timezone) {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);

        return timeFormat.format(selectedTime);
    }

    private String getDayRelativeToSelectedTime(Date selectedTime, int dayOfSelectedLocation) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedTime);
        int dayOfOtherLocation = calendar.get(Calendar.DAY_OF_YEAR);

        int dayDifference = dayOfOtherLocation - dayOfSelectedLocation;

        String dayDifferenceText = "";

        if (dayDifference == -1) {
            dayDifferenceText = getString(R.string.day_diff_before_text);
        }
        if (dayDifference == 1) {
            dayDifferenceText = getString(R.string.day_diff_after_text);
        }

        return dayDifferenceText;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
