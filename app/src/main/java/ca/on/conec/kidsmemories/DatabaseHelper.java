package ca.on.conec.kidsmemories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    public  static  final int version = 1;
    public  static String dbName = "VaccinationSchedule.db";

    // Table Name
    public static final String TABLE_NAME = "Vaccination";
    public static final String TABLE_NAME1 = "Memo";

    // Table Columns
    public static final String COL1 = "id";
    public static final String COL2 = "province";
    public static final String COL3 = "vaccines";
    public static final String COL4 = "first";
    public static final String COL5 = "second";
    public static final String COL6 = "third";
    public static final String COL7 = "fourth";
    public static final String COL8 = "fifth";

    // Table1 Columns
    public static final String COL11 = "id";
    public static final String COL12 = "memoDate";
    public static final String COL13 = "memo";
    public static final String COL14 = "kidId";



    //private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT NOT NULL," + COL3 + " TEXT NOT NULL, " + COL4 + " INTEGER," +COL5 + " INTEGER," + COL6 + " INTEGER," +COL7 + " INTEGER," + COL8 + " INTEGER); INSERT INTO " + TABLE_NAME + " VALUES ( 'ON', 'DTaP-IPV-Hib', 2,4,6,18,0),('ON','MMR',12,0,0,0,0),('ON','Var',15,0,0,0,0),('ON','Men-C-C',12,0,0,0,0),('ON','Pneu-C-13',2,4,12,0,0),('ON','Rota',2,4,6,0,0); create table " + TABLE_NAME1 + " ( " + COL11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL12 + " TEXT NOT NULL," + COL13 + " TEXT, " + COL14 + " INTEGER);";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT NOT NULL," + COL3 + " TEXT NOT NULL, " + COL4 + " INTEGER," +COL5 + " INTEGER," + COL6 + " INTEGER," +COL7 + " INTEGER," + COL8 + " INTEGER);";
    private static final String INSERT_TABLE = "INSERT INTO " + TABLE_NAME + " ( " + COL2 + ", " + COL3 + ", " + COL4 + ", " + COL5 + ", " + COL6 + ", " + COL7 + ", " + COL8 + ") VALUES ( 'ON', 'DTaP-IPV-Hib', 2,4,6,18,0),('ON','MMR',12,0,0,0,0),('ON','Var',15,0,0,0,0),('ON','Men-C-C',12,0,0,0,0),('ON','Pneu-C-13',2,4,12,0,0),('ON','Rota',2,4,6,0,0);";
    private static final String CREATE_TABLE1 = "create table " + TABLE_NAME1 + " ( " + COL11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL12 + " TEXT NOT NULL," + COL13 + " TEXT, " + COL14 + " INTEGER);";
   // private static final String CREATE_TABLE1 = "create table " + TABLE_NAME1 + " ( " + COL11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL12 + " TEXT NOT NULL," + COL13 + " TEXT, " + COL14 + " INTEGER);";

    private  static final String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;

    // Invoke automatically when instance of this class is created
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, dbName, null, version);
    }

    // Execute the SQL query to create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(INSERT_TABLE);
        db.execSQL(CREATE_TABLE1);
    }

    // When version changes,
    // execute the SQL query to drop the already existing table and  create a new table
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    // Add memo information to the table
    public boolean Insert(String memoDate, String memo){
        // instance of SQL Lite Database
        SQLiteDatabase db = getWritableDatabase();
        // Getting the instance of content values
        ContentValues cv = new ContentValues();
        // Taking Content value instance and putting data into the columns
        cv.put(COL12, memoDate);
        cv.put(COL13, memo);
        cv.put(COL14, 0);

        try{
            db.execSQL("DELETE FROM " + TABLE_NAME1 + " WHERE "+ COL12 +"='"+memoDate+"'");

            // If data is not inserted, return false
            long result = db.insert(TABLE_NAME1, null, cv);
            if(result == -1){
                return false;
            }
            else{
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    // Retrieve the data in the table according to the condition
    public Cursor ViewData(String memoDate, String screen){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        String query;
        // Retrieve all data in a table
        if(screen == "History"){
            query = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + COL12 + ";";
        }
        else{
            query = "SELECT * FROM " + TABLE_NAME1 + " where memoDate = '" + memoDate + "';";
        }

        cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor RetrieveVaccinationData(String province){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        String query;
        // Retrieve all data in a table
        query = "SELECT * FROM " + TABLE_NAME + " where province = '" + province + "';";

        cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0)
            cursor.moveToFirst();
        return cursor;
    }

    // Delete the data in the table according to the condition
    public Boolean DeleteData(String memoDate){
        // instance of SQL Lite Database
        SQLiteDatabase db = getWritableDatabase();

        try{
            db.execSQL("DELETE FROM " + TABLE_NAME1 + " WHERE "+ COL12 +"='"+memoDate+"'");
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

}

