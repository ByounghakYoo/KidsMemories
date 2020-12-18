/*
 * FileName : KidsMEmoriesDBHElper
 * Purpose : Database related to post with SQLiteOpenHelper
 * Revision History
 *          Created by Byounghak Yoo (Henry) 2020.12.10
 */

package ca.on.conec.kidsmemories.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class KidsMemoriesDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "KidsMemories.db";

    /**
     * DBHelper constructor
     * @param context context
     */
    public KidsMemoriesDBHelper(@Nullable Context context) {
        super(context , DB_NAME , null , DB_VERSION);
    }

    /**
     * create life cycle
     * @param db db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(KidsDAO.CREATE_KID_TABLE);
            db.execSQL(PostDAO.CREATE_POST_TABLE);
            db.execSQL(ImmunizationDAO.VAC_CREATE_TABLE);
            db.execSQL(ImmunizationDAO.MEMO_CREATE_TABLE);
            db.execSQL(ImmunizationDAO.VAC_INSERT_TABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * upgrade life cycle
     * @param db db
     * @param oldVersion old db version
     * @param newVersion new db version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KidsDAO.DROP_KID_TABLE);
        db.execSQL(PostDAO.DROP_POST_TABLE);
        db.execSQL(ImmunizationDAO.VAC_DROP_TABLE);
        db.execSQL(ImmunizationDAO.MEMO_DROP_TABLE);
        onCreate(db);
    }
}
