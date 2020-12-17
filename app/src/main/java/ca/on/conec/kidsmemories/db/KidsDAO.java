/* KidsDAO.java
   KidsMemories: DataBase Helper for manipulating kids data
    Revision History
        Yi Phyo Hong, 2020.12.10: Created
*/
package ca.on.conec.kidsmemories.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import ca.on.conec.kidsmemories.entity.Kids;

public class KidsDAO extends KidsMemoriesDBHelper{

    public static final String KID_TABLE_NAME = "kid";
    public static final String KID_COL1 = "kid_id";
    public static final String KID_COL2 = "first_name";
    public static final String KID_COL3 = "last_name";
    public static final String KID_COL4 = "date_of_birth";
    public static final String KID_COL5 = "gender";
    public static final String KID_COL6 = "nick_name";
    public static final String KID_COL7 = "province_code";
    public static final String KID_COL8 = "photo_uri";

    // Query for creating table
    public static final String CREATE_KID_TABLE = "CREATE TABLE " + KID_TABLE_NAME + " ( "
            + KID_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "   + KID_COL2 + " TEXT, "
            + KID_COL3 + " TEXT, " + KID_COL4 + " TEXT, " + KID_COL5 + " TEXT, "
            + KID_COL6 + " TEXT, " + KID_COL7 + " TEXT, " + KID_COL8 + " TEXT);";

    // Query for deleting table
    public static final String DROP_KID_TABLE = "DROP TABLE IF EXISTS " + KID_TABLE_NAME;

    KidsMemoriesDBHelper kDBHelper;

    /**
     * Constructor: Create Root DB Helper
     * @param context context
     */
    public KidsDAO(@Nullable Context context) {
        super(context);
        kDBHelper = new KidsMemoriesDBHelper(context);
    }

    /**
     * Add a kid by using ContentValues
     * @return success flag (Boolean)
     */
    public boolean insertKid(Kids objKid)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Set kid data for adding
        ContentValues cv = new ContentValues();
        cv.put(KID_COL2, objKid.getFirstName());
        cv.put(KID_COL3, objKid.getLastName());
        cv.put(KID_COL4, objKid.getDateOfBirth());
        cv.put(KID_COL5, objKid.getGender());
        cv.put(KID_COL6, objKid.getNickName());
        cv.put(KID_COL7, objKid.getProvinceCode());
        cv.put(KID_COL8, objKid.getPhotoPath());

        // Save kid data
        long result = db.insert(KID_TABLE_NAME, null, cv);
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
     * Update a kid by using ContentValues
     * @return success flag (Boolean)
     */
    public boolean updateKid(Kids objKid)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Set kid data for update
        ContentValues cv = new ContentValues();
        cv.put(KID_COL2, objKid.getFirstName());
        cv.put(KID_COL3, objKid.getLastName());
        cv.put(KID_COL4, objKid.getDateOfBirth());
        cv.put(KID_COL5, objKid.getGender());
        cv.put(KID_COL6, objKid.getNickName());
        cv.put(KID_COL7, objKid.getProvinceCode());
        cv.put(KID_COL8, objKid.getPhotoPath());

        // Update kid data
        long result = db.update(KID_TABLE_NAME, cv, "kid_id = " +  objKid.getKidId(), null);
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
     * Delete a kid
     * @return success flag (Boolean)
     */
    public boolean deleteKid(int kidId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete kid data corresponding to kidId
        long result = db.delete(KID_TABLE_NAME, "kid_id = " +  kidId, null);
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
     * @return Cursor for manipulating result of query
     */
    public Cursor getKids(String whereString)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        // If user select all data or not
        if (whereString.isEmpty())
        {
            cursor = db.rawQuery("SELECT * FROM " + KID_TABLE_NAME, null);
        }
        else
        {
            cursor = db.rawQuery("SELECT * FROM " + KID_TABLE_NAME + " " + whereString, null);
        }

        // If any data exist, go to first row
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
