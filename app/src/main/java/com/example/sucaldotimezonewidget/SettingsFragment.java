package com.example.sucaldotimezonewidget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Definition of variables
    private Button btn12HourFormat, btn24HourFormat;
    private Spinner spinnerCity1, spinnerCity2, spinnerCity3, spinnerCity4;
    private ArrayAdapter<CharSequence> adapter;
    private String selectedCity1, selectedCity2, selectedCity3, selectedCity4;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Link xml layout file with this activity (fragment)
        View rootView = inflater.inflate(R.layout.fragment_app_settings, container, false);

        // Assign variables
        btn12HourFormat = rootView.findViewById(R.id.btnhhformat);
        btn24HourFormat = rootView.findViewById(R.id.btnHHformat);
        spinnerCity1 = rootView.findViewById(R.id.spinnerselectcity1);
        spinnerCity2 = rootView.findViewById(R.id.spinnerselectcity2);
        spinnerCity3 = rootView.findViewById(R.id.spinnerselectcity3);
        spinnerCity4 = rootView.findViewById(R.id.spinnerselectcity4);

        // Assign drop-down menus to spinners and define default
        assignDropDownMenuToSpinner(spinnerCity1,getString(R.string.selected_city1_key));
        assignDropDownMenuToSpinner(spinnerCity2,getString(R.string.selected_city2_key));
        assignDropDownMenuToSpinner(spinnerCity3,getString(R.string.selected_city3_key));
        assignDropDownMenuToSpinner(spinnerCity4,getString(R.string.selected_city4_key));

        // Define default button configuration
        highlightButtonOfPreChosenFormat();

        // Define button listeners
        btn12HourFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWidgetTimeSettings(getString(R.string.hour_format_12)); // R.string. references the strings defined in xml strings file
                setColorOfButtonClicked(btn12HourFormat);
                setColorOfUnselectedButton(btn24HourFormat);
            }
        });

        btn24HourFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWidgetTimeSettings(getString(R.string.hour_format_24));
                setColorOfButtonClicked(btn24HourFormat);
                setColorOfUnselectedButton(btn12HourFormat);
            }
        });
        return rootView;
    }

    private void assignDropDownMenuToSpinner(Spinner spinner, String cityKey) {
        // R.array.settings_select_city is defined in strings.xml
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.settings_select_city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // If spinner selections have already been stored then retrieve them and use them
        int selectedCity = 0;
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        if (widgetPreferences.isCityKeyPresent(cityKey)) {
            if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_sydney))) {
                selectedCity = 0;
            } else if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_nyc))) {
                selectedCity = 1;
            } else if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_sivar))) {
                selectedCity = 2;
            } else if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_bochum))) {
                selectedCity = 3;
            } else if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_toronto))) {
                selectedCity = 4;
            } else if (widgetPreferences.getStoredCitySelection(cityKey).equals(getString(R.string.text_none))) {
                selectedCity = 5;
            }
        }
        spinner.setSelection(selectedCity);
    }

    // Store selected hour format outside of the App (--> WidgetPreferences.java)
    private void setWidgetTimeSettings(String timePattern) {
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        widgetPreferences.setTimePatternToStore(timePattern);
    }

    // Default Button Configuration: this method is used only each time the App is launched
    private void highlightButtonOfPreChosenFormat() {
        Button buttonToHighlight = btn12HourFormat;
        // If a timePattern has already been stored then retrieve it and highlight the button instead of the default
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        if (widgetPreferences.isTimeKeyPresent()) {
            if (widgetPreferences.getStoredTimePattern().equals(getString(R.string.hour_format_24))) {
                buttonToHighlight = btn24HourFormat;
            }
        }
        setColorOfButtonClicked(buttonToHighlight);
    }

    // Method for storing selected cities (--> WidgetPreferences.java)
    private void setWidgetCitySettings(String citySelection, String storeKey) {
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        widgetPreferences.setCitySelectionToStore(citySelection, storeKey);
    }

    // Listeners for Spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerselectcity1:
                selectedCity1 = parent.getItemAtPosition(position).toString();
                setWidgetCitySettings(selectedCity1, getString(R.string.selected_city1_key));
                break;
            case R.id.spinnerselectcity2:
                selectedCity2 = parent.getItemAtPosition(position).toString();
                setWidgetCitySettings(selectedCity2, getString(R.string.selected_city2_key));
                break;
            case R.id.spinnerselectcity3:
                selectedCity3 = parent.getItemAtPosition(position).toString();
                setWidgetCitySettings(selectedCity3, getString(R.string.selected_city3_key));
                break;
            case R.id.spinnerselectcity4:
                selectedCity4 = parent.getItemAtPosition(position).toString();
                setWidgetCitySettings(selectedCity4, getString(R.string.selected_city4_key));
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setColorOfButtonClicked(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    private void setColorOfUnselectedButton(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightGrey));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
    }


}
