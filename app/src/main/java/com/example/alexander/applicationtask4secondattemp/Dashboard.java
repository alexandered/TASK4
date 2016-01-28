package com.example.alexander.applicationtask4secondattemp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DBAdapter myDb;
    NavigationView navigation;
    TextView textViewBalance, textViewIncomenett, textViewExpensenett;
    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView listViewatas = (ListView) findViewById(R.id.listViewatas);
        ListView listViewbawah = (ListView) findViewById(R.id.listViewbawah);
        textViewBalance = (TextView) findViewById(R.id.textViewnett);
        textViewExpensenett = (TextView) findViewById(R.id.textViewTotalExpensenett);
        textViewIncomenett  = (TextView) findViewById(R.id.textViewTotalIncomenett);

        currentActivity = this;

        navigation = (NavigationView)findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        myDb = new DBAdapter(this);
        myDb.open();
        Cursor cursor_expense = myDb.getCountAmount("EXPENSE");
        if (cursor_expense.moveToFirst()) {
            textViewExpensenett.setText(String.valueOf(cursor_expense.getInt(0)));
        }
        Cursor cursor_income = myDb.getCountAmount("INCOME");
        if (cursor_income.moveToFirst()) {
            textViewIncomenett.setText(String.valueOf(cursor_income.getInt(0)));
        }
        int hasilbalance =(Integer.parseInt(String.valueOf(textViewIncomenett.getText())) - Integer.parseInt(String.valueOf(textViewExpensenett.getText())));
        textViewBalance.setText(String.valueOf(hasilbalance));

        Cursor cursorExpense = myDb.selectAllExpense();
        Cursor cursorIncome  = myDb.selectAllIncome();

        final ArrayList<Trans> transDataIn = new ArrayList<Trans>();
        JSONArray arr_incomes = new JSONArray();

        while (cursorIncome.moveToNext()) {
            StringBuffer buffer = new StringBuffer();

            buffer.append("{ description : " + cursorIncome.getString(0) + ",");
            buffer.append("amount : " + cursorIncome.getString(1) + ",");
            buffer.append("Id : " + cursorIncome.getInt(2) + "}");
            JSONObject expense = new JSONObject();

            Trans dataIn = new Trans();
            dataIn.setID(cursorIncome.getInt(2));
            dataIn.setDescription(cursorIncome.getString(0));
            dataIn.setAmount(cursorIncome.getString(1));
            transDataIn.add(dataIn);
            try {
                expense = new JSONObject(String.valueOf(buffer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr_incomes.put(expense);
        }

        ArrayList<String> listdatai = new ArrayList<String>();
        if (arr_incomes != null) {
            for (int i=0;i<arr_incomes.length();i++){
                try {
                    listdatai.add(arr_incomes.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        final ArrayList<Trans> transData = new ArrayList<Trans>();

        JSONArray arr_expenses = new JSONArray();
        while (cursorExpense.moveToNext()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{ Id : " + cursorExpense.getInt(0) + ",");
            buffer.append("description : " + cursorExpense.getString(1) + ",");
            buffer.append("amount : " + cursorExpense.getString(2) + "}");
            JSONObject expense = new JSONObject();

            Trans data = new Trans();
            data.setID(cursorExpense.getInt(0));
            data.setDescription(cursorExpense.getString(1));
            data.setAmount(cursorExpense.getString(2));
            transData.add(data);
            try {
                expense = new JSONObject(String.valueOf(buffer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr_expenses.put(expense);
        }

        ArrayList<String> listdata = new ArrayList<String>();
        if (arr_expenses != null) {
            for (int i=0;i<arr_expenses.length();i++){
                try {
                    listdata.add(arr_expenses.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        CustomAdaptertwo alexadapteri = new CustomAdaptertwo(this, listdatai);
        final CustomAdapter alexadapter = new CustomAdapter(this, listdata);
        listViewatas.setAdapter(alexadapter);
        listViewbawah.setAdapter(alexadapteri);


        listViewatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(currentActivity);
                alertdialog.setMessage("Whatcugonnado?").setTitle("Notification");
                alertdialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.alertdialogupdate, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                        builder.setView(dialogLayout);
                        builder.show();
                        final EditText editTextUpdateDesc = (EditText) dialogLayout.findViewById(R.id.editTextUpdateDesc);
                        final EditText editTextUpdateAm = (EditText) dialogLayout.findViewById(R.id.editTextUpdateAmount);
                        editTextUpdateDesc.setText(transData.get(position).getDescription());
                        editTextUpdateAm.setText(transData.get(position).getAmount());

                        Button btn_update = (Button) dialogLayout.findViewById(R.id.button_update);
                        btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(editTextUpdateDesc.getText()) && !TextUtils.isEmpty(editTextUpdateAm.getText())) {


                                    myDb.updateRow(transData.get(position).getID(), editTextUpdateDesc.getText().toString(), editTextUpdateAm.getText().toString(), "EXPENSE");


                                    Intent refresh = new Intent(Dashboard.this, Dashboard.class);
                                    startActivity(refresh);
                                }
                            }
                        });
                    }
                });

                alertdialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deleteRow(transData.get(position).getID());
                        Intent refresh = new Intent(Dashboard.this, Dashboard.class);
                        startActivity(refresh);
                    }
                });
                AlertDialog dialog = alertdialog.create();
                dialog.show();
            }
        });


        listViewbawah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(currentActivity);
                alertdialog.setMessage("Whatcugonnado?").setTitle("Notification");
                alertdialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.alertdialogupdate, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                        builder.setView(dialogLayout);
                        builder.show();

                        final EditText editTextUpdateDesc = (EditText) dialogLayout.findViewById(R.id.editTextUpdateDesc);
                        final EditText editTextUpdateAm = (EditText) dialogLayout.findViewById(R.id.editTextUpdateAmount);
                        editTextUpdateDesc.setText(transDataIn.get(position).getDescription());
                        editTextUpdateAm.setText(transDataIn.get(position).getAmount());

                        Button btn_update = (Button) dialogLayout.findViewById(R.id.button_update);
                        btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(editTextUpdateDesc.getText()) && !TextUtils.isEmpty(editTextUpdateAm.getText())) {


                                    myDb.updateRow(transDataIn.get(position).getID(), editTextUpdateDesc.getText().toString(), editTextUpdateAm.getText().toString(), "INCOME");


                                    Intent refresh = new Intent(Dashboard.this, Dashboard.class);
                                    startActivity(refresh);
                                }
                            }
                        });
                    }
                });

                alertdialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deleteRow(transDataIn.get(position).getID());
                        Intent refresh = new Intent(Dashboard.this, Dashboard.class);
                        startActivity(refresh);
                    }
                });
                AlertDialog dialog = alertdialog.create();
                dialog.show();
            }
        });
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
}

    class CustomAdapter extends ArrayAdapter<String>{
    private ArrayList<String> user_list;
        JSONObject JSONuserlist;
    public CustomAdapter(Context context, ArrayList<String> listdata) {
        super(context, R.layout.fragment_listview_item, listdata);
        user_list=listdata;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater alexinflater = LayoutInflater.from(getContext());
        View fragmentlistview = alexinflater.inflate(R.layout.fragment_listview_item, parent, false);
        TextView DescriptionExpense = (TextView)fragmentlistview.findViewById(R.id.textViewDesc);
        TextView AmountExpense = (TextView)fragmentlistview.findViewById(R.id.textViewAm);

        try {
            JSONuserlist = new JSONObject(user_list.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            DescriptionExpense.setText(JSONuserlist.getString("description").trim());
            AmountExpense.setText(JSONuserlist.getString("amount"));

            } catch (Exception e) {
                    e.printStackTrace();
                }
        return fragmentlistview;
        }
    }

class CustomAdaptertwo extends ArrayAdapter<String>{
    private ArrayList<String> user_list_income;
    JSONObject JSONuserlist_income;
    public CustomAdaptertwo(Context context, ArrayList<String> listdatai) {
        super(context, R.layout.fragment_listview_item, listdatai);
        user_list_income=listdatai;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater alexinflater = LayoutInflater.from(getContext());
        View fragmentlistview = alexinflater.inflate(R.layout.fragment_listview_item, parent, false);
        TextView Description = (TextView)fragmentlistview.findViewById(R.id.textViewDesc);
        TextView Amount = (TextView)fragmentlistview.findViewById(R.id.textViewAm);
        try {
            JSONuserlist_income = new JSONObject(user_list_income.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Description.setText(JSONuserlist_income.getString("description"));
            Amount.setText(JSONuserlist_income.getString("amount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragmentlistview;
    }
}