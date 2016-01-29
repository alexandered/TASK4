package com.example.alexander.applicationtask4secondattemp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    Activity currentActivity;
//    WaitingAsynctask waitingforsync;
    String messagefeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentActivity = this;
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        btn_sync = (Button) findViewById(R.id.btn_sync);
        openDB();

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                waitingforsync = new WaitingAsynctask();
//                waitingforsync.execute();
                retrofitIsOn();
            }

        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings_dash) {
            Intent intent_dash1 = new Intent(this, Dashboard.class);
            startActivity(intent_dash1);

        } else if (item.getItemId() == R.id.action_settings_trans) {
            Intent intent_trans1 = new Intent(this, Transaction.class);
            startActivity(intent_trans1);

        } else if (item.getItemId() == R.id.action_settings_sync) {
            Intent intent_sync1 = new Intent(this, Synchronization.class);
            startActivity(intent_sync1);

        }
        return false;
    }

//    class WaitingAsynctask extends AsyncTask<String , Void, String> {
//        ProgressDialog progress_dialog = new ProgressDialog(Synchronization.this);
//
//        @Override
//        protected void onPreExecute() {
//            progress_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress_dialog.setTitle("In Progress...");
//            progress_dialog.setCancelable(false);
//            progress_dialog.setMax(10);
//            progress_dialog.setProgress(0);
//            progress_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel Process", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            progress_dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String message = retrofitIsOn();
//            return message;
//        }
//
//            @Override
//            protected void onProgressUpdate (Void...values){
//                progress_dialog.setMessage("SABAR");
//            }
//
//            @Override
//            protected void onPostExecute (String result){
//                progress_dialog.dismiss();
//                Toast.makeText(Synchronization.this, result, Toast.LENGTH_SHORT).show();
//            }
//        }
    public String retrofitIsOn() {
        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://private-3863a7-task41.apiary-mock.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final UserAPI user_api = retrofit.create(UserAPI.class);
        final Cursor cursor = myDb.getAllRows();

        while (cursor.moveToNext()) {
            Call<User> call = user_api.saveTransaction(cursor.getString(1).toString(), cursor.getString(0).toString(), cursor.getString(2).toString());
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(final Response<User> response, Retrofit retrofit) {
                    int status = response.code();
                    final User s = response.body();
                    Toast.makeText(Synchronization.this, s.getMessage(), Toast.LENGTH_SHORT).show();

                }
                @Override
                public void onFailure(Throwable t) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(currentActivity);
                    alert.setCancelable(false)
                            .setMessage("Failed to Synchronize")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {
                                    retrofitIsOn();
                                }

                            })
                            .setNegativeButton("Skip", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.show();
                }
            });
        }
        return null;
    }
}