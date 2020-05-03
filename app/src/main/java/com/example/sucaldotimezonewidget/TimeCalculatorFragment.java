package com.example.sucaldotimezonewidget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeCalculatorFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button calculateBtn;
    private TimePicker timePicker;

    private TextView cityName1;
    private TextView cityTime1;

    private String selectedCity;
    private String timePattern;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_calculator, container, false);

        WidgetPreferences widgetPreferences = new WidgetPreferences(getContext());
        timePattern = widgetPreferences.getStoredTimePattern();

        spinner = rootView.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        cityName1 = rootView.findViewById(R.id.header_city_1);
        cityTime1 = rootView.findViewById(R.id.time_city_1);

        calculateBtn = rootView.findViewById(R.id.calculate_btn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTime();
            }
        });

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

    private void calculateTime() {
        Date selectedTime = getTimeOfTimePickerFromCity();

        String formattedOtherTime = getFormattedTimeOfOtherCity(selectedTime);

        cityName1.setText(getString(R.string.text_nyc));
        cityTime1.setText(formattedOtherTime);

    }

    private Date getTimeOfTimePickerFromCity() {
        String selectedTimezone = getString(R.string.sydney_timezone);

        TimeZone.setDefault(TimeZone.getTimeZone(selectedTimezone));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());

        return calendar.getTime();
    }

    private String getFormattedTimeOfOtherCity(Date selectedTime) {
        String otherTimezone = getString(R.string.nyc_timezone);

        TimeZone.setDefault(TimeZone.getTimeZone(otherTimezone));
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);

        return timeFormat.format(selectedTime);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
