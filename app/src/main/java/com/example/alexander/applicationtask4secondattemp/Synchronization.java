package com.example.alexander.applicationtask4secondattemp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Synchronization extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btn_sync;
    DBAdapter myDb;
    NavigationView navigation;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = (NavigationView)findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        btn_sync = (Button) findViewById(R.id.btn_sync);
        openDB();

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://polls.alextask4.org")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final UserAPI user_api = retrofit.create(UserAPI.class);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Cursor cursor  = myDb.getAllRows();

                while (cursor.moveToNext()) {
                    Call<User> call = user_api.saveTransaction(cursor.getString(1).toString(),Integer.parseInt(cursor.getString(0).toString()));
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(final Response<User> response, Retrofit retrofit) {
                            int status = response.code();
                            final User s =  response.body();
                            Toast.makeText(Synchronization.this, s.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(Synchronization.this, "Gagal bro", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings_dash) {
            Intent intent_dash1 = new Intent(this, Dashboard.class);
//            finish();
            startActivity(intent_dash1);
        } else if (item.getItemId() == R.id.action_settings_trans) {
            Intent intent_trans1 = new Intent(this, Transaction.class);
//            finish();
            startActivity(intent_trans1);
        } else if (item.getItemId() == R.id.action_settings_sync) {
            Intent intent_sync1 = new Intent(this, Synchronization.class);
//            finish();
            startActivity(intent_sync1);
        }
        return false;
    }
}