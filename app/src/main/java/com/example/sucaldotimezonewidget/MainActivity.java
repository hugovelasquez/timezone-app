package com.example.sucaldotimezonewidget;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

// implements "Navigation...Listener" needed for defining the drawer menu listeners
// this is a comment from sofia to show hugo how git works
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link xml layout to this activity
        setContentView(R.layout.activity_main);

        // Activate toolbar on top
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Assign xml object to drawer variable
        drawer = findViewById(R.id.drawer_layout);

        // Define Listener for drawer menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add toggle capability to the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Show Home as a default
        if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        // Show Home as selected in the drawer menu
        navigationView.setCheckedItem(R.id.nav_home);}
    }

    // Definition of listeners for each option in drawer menu
    public boolean onNavigationItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_settings:
                // when option "Settings" is selected, replace the xml object fragment_container with
                // the layout within SettingsFragment.java
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_timecalc:
                // when option "Time Calculator" is selected, replace the xml object fragment_container with
                // the layout within TimeCalculatorFragment.java
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TimeCalculatorFragment()).commit();
                break;
            case R.id.nav_home:
                // when option "Home" is selected, replace the xml object fragment_container with
                // the layout within MainActivity.java
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // For displaying purposes only (close drawer if user presses back button)
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}

