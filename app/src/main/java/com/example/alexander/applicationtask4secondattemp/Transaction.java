package com.example.alexander.applicationtask4secondattemp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Transaction extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
NavigationView navigation;
DBAdapter myDb;
EditText editTextExpenseA,editTextExpenseD,editTextIncomeA,editTextIncomeD;
Button btn_expense,btn_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openDB();
        editTextExpenseD = (EditText)findViewById(R.id.editTextExpenseD);
        editTextExpenseA = (EditText)findViewById(R.id.editTextExpenseA);
        editTextIncomeA = (EditText)findViewById(R.id.editTextIncomeA);
        editTextIncomeD = (EditText)findViewById(R.id.editTextIncomeD);

        navigation = (NavigationView)findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);
        btn_expense = (Button)findViewById(R.id.btn_expense);
        btn_income = (Button)findViewById(R.id.btn_income);
        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editTextExpenseA.getText())&&!TextUtils.isEmpty(editTextExpenseD.getText())){
                    myDb.insertRow(editTextExpenseD.getText().toString(),editTextExpenseA.getText().toString(),"EXPENSE");
                    editTextExpenseD.setText("");
                    editTextExpenseA.setText("");
                }
                }
        });
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editTextIncomeA.getText())&&!TextUtils.isEmpty(editTextIncomeD.getText())){
                    myDb.insertRow(editTextIncomeD.getText().toString(),editTextIncomeA.getText().toString(),"INCOME");
                    editTextIncomeD.setText("");
                    editTextIncomeA.setText("");
                }
            }
        });
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
    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }
}




