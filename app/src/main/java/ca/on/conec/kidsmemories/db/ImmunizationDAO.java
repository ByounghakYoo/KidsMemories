package ca.on.conec.kidsmemories.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class ImmunizationDAO extends KidsMemoriesDBHelper{

    KidsMemoriesDBHelper kdb;


    // Table Name
    public static final String VAC_TABLE_NAME = "Vaccination";
    public static final String MEMO_TABLE_NAME = "Memo";

    // Table Columns
    public static final String VAC_COL1 = "id";
    public static final String VAC_COL2 = "province";
    public static final String VAC_COL3 = "vaccines";
    public static final String VAC_COL4 = "first";
    public static final String VAC_COL5 = "second";
    public static final String VAC_COL6 = "third";
    public static final String VAC_COL7 = "fourth";
    public static final String VAC_COL8 = "fifth";

    // Table1 Columns
    public static final String MEMO_COL11 = "id";
    public static final String MEMO_COL12 = "memoDate";
    public static final String MEMO_COL13 = "memo";
    public static final String MEMO_COL14 = "kidId";

    public static final String VAC_CREATE_TABLE = "create table " + VAC_TABLE_NAME + " ( " + VAC_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VAC_COL2 + " TEXT NOT NULL," + VAC_COL3 + " TEXT NOT NULL, " + VAC_COL4 + " INTEGER," + VAC_COL5 + " INTEGER," + VAC_COL6 + " INTEGER," + VAC_COL7 + " INTEGER," + VAC_COL8 + " INTEGER);";
    // Insert vaccination schedule information according to Provence
    public static final String VAC_INSERT_TABLE = "INSERT INTO " + VAC_TABLE_NAME + " ( " + VAC_COL2 + ", " + VAC_COL3 + ", " + VAC_COL4 + ", " + VAC_COL5 + ", " + VAC_COL6 + ", " + VAC_COL7 + ", " + VAC_COL8 + ") VALUES ( 'ON', 'DTaP-IPV-Hib', 2,4,6,18,0),('ON','MMR',12,0,0,0,0),('ON','Var',15,0,0,0,0),('ON','Men-C-C',12,0,0,0,0),('ON','Pneu-C-13',2,4,12,0,0),('ON','Rota',2,4,6,0,0)," +
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
    public static final String MEMO_CREATE_TABLE = "create table " + MEMO_TABLE_NAME + " ( " + MEMO_COL11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MEMO_COL12 + " TEXT NOT NULL," + MEMO_COL13 + " TEXT, " + MEMO_COL14 + " INTEGER);";
    public static final String VAC_DROP_TABLE = " DROP TABLE IF EXISTS " + VAC_TABLE_NAME;
    public static final String MEMO_DROP_TABLE = " DROP TABLE IF EXISTS " + MEMO_TABLE_NAME;

    public ImmunizationDAO(@Nullable Context context) {
        super(context);
        kdb = new KidsMemoriesDBHelper(context);
    }

    // Add memo information to the table
    public boolean Insert(String memoDate, String memo, Integer kidId){
        // instance of SQL Lite Database
        SQLiteDatabase db = getWritableDatabase();
        // Getting the instance of content values
        ContentValues cv = new ContentValues();
        // Taking Content value instance and putting data into the columns
        cv.put(MEMO_COL12, memoDate);
        cv.put(MEMO_COL13, memo);
        cv.put(MEMO_COL14, kidId);

        try{
            db.execSQL("DELETE FROM " + MEMO_TABLE_NAME + " WHERE "+ MEMO_COL12 +"='"+memoDate+"'");

            // If data is not inserted, return false
            long result = db.insert(MEMO_TABLE_NAME, null, cv);
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
        SQLiteDatabase db =  getWritableDatabase();
        Cursor cursor = null;

        String query;
        // Retrieve data for memo history
        if(screen == "History"){
            query = "SELECT * FROM " + MEMO_TABLE_NAME + " where kidId = " + kidId + " ORDER BY " + MEMO_COL12 + ";";
        }
        // Retrieve data for calendar
        else{
            query = "SELECT * FROM " + MEMO_TABLE_NAME + " where memoDate = '" + memoDate + "'" + " and kidId = " + kidId + ";";
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

        String query = "SELECT * FROM " + VAC_TABLE_NAME + " where province = '" + province + "';";
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
            db.execSQL("DELETE FROM " + MEMO_TABLE_NAME + " WHERE "+ MEMO_COL12 +"='"+memoDate+"'" + " and kidId = " + kidId + ";");
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
