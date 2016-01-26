package com.example.alexander.applicationtask4secondattemp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter"; //used for logging database version changes
			
	// Field Names:
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_AMOUNT = "amount";
	public static final String TYPE = "type";
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_AMOUNT, TYPE};

	
	// Column Numbers for each Field Name:
	public static final int COL_ROWID = 0;
	public static final int COL_DESCRIPTION = 1;
	public static final int COL_AMOUNT = 2;
	public static final int COL_TYPE = 3;

	// DataBase info:
	public static final String DATABASE_NAME = "dbTrans";
	public static final String DATABASE_TABLE = "mainTrans";
	public static final int DATABASE_VERSION = 2; // The version number must be incremented each time a change to DB structure occurs.
		
	//SQL statement to create database
	private static final String DATABASE_CREATE_SQL = 
			"CREATE TABLE " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_DESCRIPTION + " TEXT NOT NULL, "
			+ KEY_AMOUNT + " TEXT, "
			+ TYPE + " TEXT "
			+ ");";
	
	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to be inserted into the database.
	public long insertRow(String description, String amount, String type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DESCRIPTION, description);
		initialValues.put(KEY_AMOUNT, amount);
		initialValues.put(TYPE, type);
				
		// Insert the data into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	public Cursor selectAllExpense(){
		Cursor c = db.rawQuery("select _id, description, amount from mainTrans where type like ?", new String[]{"EXPENSE"});
		return c;
	}

	public Cursor getCountAmount(String type){
		Cursor c = db.rawQuery("select sum(amount) from mainTrans where type like ?", new String[] { type });
		return c;
	}

	public Cursor selectAllIncome(){
		Cursor c = db.rawQuery("select description, amount, _id from mainTrans where type like ?", new String[]{"INCOME"});
		return c;
	}
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}



	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(String rowId, String description, String amount, String type) {
		String where = KEY_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_DESCRIPTION, description);
		newValues.put(KEY_AMOUNT, amount);
		newValues.put(TYPE, type);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}