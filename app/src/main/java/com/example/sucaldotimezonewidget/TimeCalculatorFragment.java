package com.example.sucaldotimezonewidget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//public class TimeCalculatorFragment extends Fragment implements AdapterView.OnItemSelectedListener {

public class TimeCalculatorFragment extends Fragment {

    // Definition of variables
    // Test2
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                 // Link xml layout file with this activity (fragment)
                View rootView = inflater.inflate(R.layout.fragment_time_calculator_dummy,container,false);

//                // Assign xml object to variable
//                spinner = (Spinner) rootView.findViewById(R.id.spinner);
//                // give my string-array "cities" a pre-defined android array layout "simple_spinner_item"
//                adapter =  ArrayAdapter.createFromResource(getActivity(),R.array.cities,android.R.layout.simple_spinner_item);
//                // set dropdown-menu
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                // Link everything to spinner
//                spinner.setAdapter(adapter);
//
//
//                // Define listener for menu items
//                spinner.setOnItemSelectedListener(this);
//
        return rootView;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String textSpinner = parent.getItemAtPosition(position).toString();
//            Toast.makeText(parent.getContext(), textSpinner, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
