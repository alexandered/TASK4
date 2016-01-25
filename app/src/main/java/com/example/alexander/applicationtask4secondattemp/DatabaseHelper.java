package com.example.alexander.applicationtask4secondattemp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "alex.db";

    public static final String TABLE_NAME = "transaction";

    public static final String COL_1 = "ID";

    public static final String COL_2 = "AMOUNT";

    public static final String COL_3 = "DESCRIPTION";

    public static final String COL_4 = "TYPE";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +

            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            COL_2 + " NUMBER, " +

            COL_3 + " TEXT, " +

            COL_4 + " TEXT );";

    public static final String CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_NAME + " ( " +

            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            COL_2 + " ID, " +

            COL_3 + " TEXT, " +

            COL_4 + " TEXT );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//    public DatabaseHelper() {
//
//
//    super(DATABASE_NAME, null, DATABASE_VERSION);
//
//}

    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
        db.execSQL("INSERT INTO EXPENSE(AMOUNT) VALUES('120')");
    }


    public boolean save_transaction(String amount, String description, String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_2, amount);

        content_values.put(COL_3, description);

        content_values.put(COL_4, type);

        long result = db.insert(TABLE_NAME, null, content_values);

        return result != -1;
    }

    public Cursor list_transaction(String TYPE) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor transaction = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TYPE=" + TYPE, null);

        return transaction;

    }


    public boolean update_transaction(String id, String amount, String description, String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_1, id);

        content_values.put(COL_2, amount);

        content_values.put(COL_3, description);

        content_values.put(COL_4, type);

        db.update(TABLE_NAME, content_values, "ID = ? ", new String[]{id});

        return true;

    }


    public Integer delete_transaction(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});

    }

}