package com.example.sucaldotimezonewidget;

import android.os.Bundle;
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

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Link xml layout file with this activity (fragment)
        View rootView = inflater.inflate(R.layout.fragment_app_settings,container,false);

        // Assign variables
        btn12HourFormat = rootView.findViewById(R.id.btnhhformat);
        btn24HourFormat = rootView.findViewById(R.id.btnHHformat);
        spinnerCity1 = rootView.findViewById(R.id.spinnerselectcity1);
        spinnerCity2 = rootView.findViewById(R.id.spinnerselectcity2);
        spinnerCity3 = rootView.findViewById(R.id.spinnerselectcity3);
        spinnerCity4 = rootView.findViewById(R.id.spinnerselectcity4);

        // Assign drop-down menus to spinners
        assignDropDownMenuToSpinner(spinnerCity1);
        assignDropDownMenuToSpinner(spinnerCity2);
        assignDropDownMenuToSpinner(spinnerCity3);
        assignDropDownMenuToSpinner(spinnerCity4);

        // As a default highlight btn12format
        highlightButtonOfPreChosenFormat();

        // Define button listeners
        btn12HourFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWidgetSettings(getString(R.string.hour_format_12)); // R.string. references the strings defined in xml strings file
                setColorOfButtonClicked(btn12HourFormat);
                setColorOfUnselectedButton(btn24HourFormat);
            }
        });

        btn24HourFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWidgetSettings(getString(R.string.hour_format_24));
                setColorOfButtonClicked(btn24HourFormat);
                setColorOfUnselectedButton(btn12HourFormat);
            }
        });

        return rootView;
    }

    // Store constant values outside of the App (--> WidgetPreferences.java)
    private void setWidgetSettings(String timePattern) {
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        widgetPreferences.setTimePatternToStore(timePattern);
    }

    // This method is used only every time the App is launched (Default config)
    private void highlightButtonOfPreChosenFormat() {
        Button buttonToHighlight = btn12HourFormat;
        // If a timePattern has already been stored then retrieve it and highlight the button instead of the default
        WidgetPreferences widgetPreferences = new WidgetPreferences(getActivity().getApplicationContext());
        if (widgetPreferences.isTimeKeyPresent()) {
            if (widgetPreferences.getStoredTimePattern().equals( getString(R.string.hour_format_24))) {
                buttonToHighlight = btn24HourFormat;
            }
        }
        setColorOfButtonClicked(buttonToHighlight);
    }

    private void assignDropDownMenuToSpinner(Spinner spinner){
        // R.array.settings_select_city is defined in strings.xml
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.settings_select_city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setColorOfButtonClicked(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
        button.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
    }

    private void setColorOfUnselectedButton(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightGrey));
        button.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey));
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
