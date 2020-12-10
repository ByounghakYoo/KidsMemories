package ca.on.conec.kidsmemories.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.on.conec.kidsmemories.model.Kids;

/**
 * DataBase Helper Class for manipulating kids data
 */
public class KidsDBHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String dbName = "kidsmemories_test.db";
    public static final String TABLE_NAME = "kid";
    public static final String COL1 = "kid_id";
    public static final String COL2 = "first_name";
    public static final String COL3 = "last_name";
    public static final String COL4 = "date_of_birth";
    public static final String COL5 = "gender";
    public static final String COL6 = "nick_name";
    public static final String COL7 = "province_code";
    public static final String COL8 = "photo_uri";

    // Query for creating table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " TEXT, " + COL8 + " TEXT);";

    // Query for deleting table
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Constructor: invoked when initiating
    public KidsDBHelper(Context context)
    {
        super(context, dbName, null, version);
    }

    // Create a table before using it
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
    }

    // Deleting a table if it exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    /**
     * Add a kid by using ContentValues
     * @Return success flag (Boolean)
     */
    public boolean insertKid(Kids objKid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, objKid.getFirstName());
        cv.put(COL3, objKid.getLastName());
        cv.put(COL4, objKid.getDateOfBirth());
        cv.put(COL5, objKid.getGender());
        cv.put(COL6, objKid.getNickName());
        cv.put(COL7, objKid.getProvinceCode());
        cv.put(COL8, objKid.getPhotoURI());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Get kids list by using ContentValues
     * @Return Cursor for manipulating result of query
     */
    public Cursor getKids(String whereString)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        if (whereString.isEmpty())
        {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        }
        else
        {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " " + whereString, null);
        }

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
