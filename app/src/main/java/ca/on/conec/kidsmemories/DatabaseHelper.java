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

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT NOT NULL," + COL3 + " TEXT NOT NULL, " + COL4 + " INTEGER," +COL5 + " INTEGER," + COL6 + " INTEGER," +COL7 + " INTEGER," + COL8 + " INTEGER);";
    // Insert vaccination schedule information according to Provence
    private static final String INSERT_TABLE = "INSERT INTO " + TABLE_NAME + " ( " + COL2 + ", " + COL3 + ", " + COL4 + ", " + COL5 + ", " + COL6 + ", " + COL7 + ", " + COL8 + ") VALUES ( 'ON', 'DTaP-IPV-Hib', 2,4,6,18,0),('ON','MMR',12,0,0,0,0),('ON','Var',15,0,0,0,0),('ON','Men-C-C',12,0,0,0,0),('ON','Pneu-C-13',2,4,12,0,0),('ON','Rota',2,4,6,0,0)," +
                                                                                  "('BC','DDTaP-IPV-Hib',18,0,0,0,0),('BC','DTaP-HB-IPV-Hib',2,4,6,0,0),('BC','MMR',12,0,0,0,0),('BC','Var',12,0,0,0,0),('BC','Men-C-C',2,12,0,0,0),('BC','Pneu-C-13',2,4,12,0,0),('BC','Rota',2,4,6,0,0)," +
                                                                                  "('AB','DTaP-IPV-Hib',18,0,0,0,0),('AB','DTaP-HB-IPV-Hib',2,4,6,0,0),('AB','MMR-V',12,18,0,0,0),('AB','Men-C-C',4,12,0,0,0),('AB','Pneu-C-13',2,4,12,0,0),('AB','Rota',2,4,6,0,0)," +
                                                                                  "('SK','DTaP-IPV-Hib',2,4,6,18,0),('SK','MMR-V',12,18,0,0,0),('SK','Men-C-C',12,0,0,0,0),('SK','Pneu-C-13',2,4,12,0,0),('SK','Rota',2,4,6,0,0)," +
                                                                                  "('MB','DTaP-IPV-Hib',2,4,6,18,0),('MB','MMR-V',12,0,0,0,0),('MB','Men-C-C',12,0,0,0,0),('MB','Pneu-C-13',2,4,12,0,0),('MB','Rota',2,4,6,0,0)," +
                                                                                  "('QC','DTaP-IPV-Hib',12,0,0,0,0),('QC','DTaP-HB-IPV-Hib',2,4,0,0,0),('QC','MMR-V',12,18,0,0,0),('QC','Men-C-C',18,0,0,0,0),('QC','Pneu-C-13',12,0,0,0,0),('QC','Rota',2,4,0,0,0)," +
                                                                                  "('NB','DTaP-IPV-Hib',2,4,6,18,0),('NB','HB',2,6,0,0,0),('NB','MMR-V',12,18,0,0,0),('NB','Men-C-C',12,0,0,0,0),('NB','Pneu-C-13',2,4,12,0,0),('NB','Rota',2,4,6,0,0)," +
                                                                                  "('NS','DTaP-IPV-Hib',2,4,6,18,0),('NS','MMR-V',12,18,0,0,0),('NS','Men-C-C',12,0,0,0,0),('NS','Pneu-C-13',2,4,12,0,0),('NS','Rota',2,4,6,0,0)," +
                                                                                  "('PE','DTaP-IPV-Hib',18,0,0,0,0),('PE','DTaP-HB-IPV-Hib',2,4,6,0,0),('PE','MMR-V',12,18,0,0,0),('PE','Men-C-C',12,0,0,0,0),('PE','Pneu-C-13',2,4,12,0,0),('PE','Rota',2,4,6,0,0)," +
                                                                                  "('NL','DTaP-IPV-Hib',2,4,6,18,0),('NL','MMR-V',12,18,0,0,0),('NL','Men-C-C',12,0,0,0,0),('NL','Pneu-C-13',2,4,12,0,0),('NL','Rota',2,4,6,0,0)," +
                                                                                  "('YT','DTaP-IPV-Hib',18,0,0,0,0),('YT','DTaP-HB-IPV-Hib',2,4,6,0,0),('YT','MMR',12,0,0,0,0),('YT','Var',12,0,0,0,0),('YT','Men-C-C',2,12,0,0,0),('YT','Pneu-C-13',2,4,6,12,0),('YT','Rota',2,4,6,0,0)," +
                                                                                  "('NT','DTaP-IPV-Hib',2,4,6,18,0),('NT','HB',1,6,0,0,0),('NT','MMR-V',12,36,0,0,0),('NT','Men-C-C',2,12,0,0,0),('NT','Pneu-C-13',2,4,6,18,0),('NT','Rota',2,4,6,0,0)," +
                                                                                  "('NU','DTaP-IPV-Hib',2,4,6,18,0),('NU','HB',1,9,0,0,0),('NU','MMR-V',12,18,0,0,0),('NU','Men-C-C',12,0,0,0,0),('NU','Pneu-C-13',2,4,6,15,0),('NU','Rota',2,4,6,0,0);";
    private static final String CREATE_TABLE1 = "create table " + TABLE_NAME1 + " ( " + COL11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL12 + " TEXT NOT NULL," + COL13 + " TEXT, " + COL14 + " INTEGER);";
    private  static final String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;
    private  static final String DROP_TABLE1 = " DROP TABLE IF EXISTS " + TABLE_NAME1;

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
        db.execSQL(DROP_TABLE1);
        onCreate(db);
    }

    // Add memo information to the table
    public boolean Insert(String memoDate, String memo, Integer kidId){
        // instance of SQL Lite Database
        SQLiteDatabase db = getWritableDatabase();
        // Getting the instance of content values
        ContentValues cv = new ContentValues();
        // Taking Content value instance and putting data into the columns
        cv.put(COL12, memoDate);
        cv.put(COL13, memo);
        cv.put(COL14, kidId);

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
    public Cursor ViewData(String memoDate, String screen, Integer kidId){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        String query;
        // Retrieve data for memo history
        if(screen == "History"){
            query = "SELECT * FROM " + TABLE_NAME1 + " where kidId = " + kidId + " ORDER BY " + COL12 + ";";
        }
        // Retrieve data for calendar
        else{
            query = "SELECT * FROM " + TABLE_NAME1 + " where memoDate = '" + memoDate + "'" + " and kidId = " + kidId + ";";
        }

        cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0)
            cursor.moveToFirst();
        return cursor;
    }

    // Retrieve vaccination information for province
    public Cursor RetrieveVaccinationData(String province){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        String query = "SELECT * FROM " + TABLE_NAME + " where province = '" + province + "';";
        cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0)
            cursor.moveToFirst();
        return cursor;
    }

    // Delete the data in the table according to the condition
    public Boolean DeleteData(String memoDate, Integer kidId){
        // instance of SQL Lite Database
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.execSQL("DELETE FROM " + TABLE_NAME1 + " WHERE "+ COL12 +"='"+memoDate+"'" + " and kidId = " + kidId + ";");
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

}

