package com.example.alexander.applicationtask4secondattemp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigation = (NavigationView)findViewById(R.id.navigation);

        navigation.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings_dash) {
            Intent intent_dash = new Intent(this, Dashboard.class);
            startActivity(intent_dash);
        } else if (item.getItemId() == R.id.action_settings_trans) {
            Intent intent_trans = new Intent(this, Transaction.class);
            startActivity(intent_trans);
        } else if (item.getItemId() == R.id.action_settings_sync) {
            Intent intent_sync = new Intent(this, Synchronization.class);
            startActivity(intent_sync);
        }
        return false;
    }
}
