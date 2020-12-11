/**
 * FileName : PostDAO
 * Purpose : Database related to post with SQLiteOpenHelper
 * Revision History
 *          Created by Byounghak Yoo (Henry) 2020.12.10
 */
package ca.on.conec.kidsmemories.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.on.conec.kidsmemories.entity.Post;


public class PostDAO extends KidsMemoriesDBHelper{

    public static final String POST_TABLE_NAME = "Post";

    public static final String POST_COL1 = "postId";
    public static final String POST_COL2 = "title";
    public static final String POST_COL3 = "content";
    public static final String POST_COL4 = "photo_link";
    public static final String POST_COL5 = "write_date";
    public static final String POST_COL6 = "view_count";
    public static final String POST_COL7 = "kidId";

    public static final String CREATE_POST_TABLE = "CREATE TABLE " + POST_TABLE_NAME + "("
                                                + POST_COL1  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + POST_COL2  + " TEXT, "
                                                + POST_COL3  + " TEXT, "
                                                + POST_COL4  + " TEXT, "
                                                + POST_COL5  + " TEXT, "
                                                + POST_COL6  + " INTEGER, "
                                                + POST_COL7  + " INTEGER  "
                                                + ")";

    public static final String DROP_POST_TABLE = "DROP TABLE IF EXISTS " + POST_TABLE_NAME;

    KidsMemoriesDBHelper kdb;

    public PostDAO(@Nullable Context context) {
        super(context);
        kdb = new KidsMemoriesDBHelper(context);
    }

    /**
     * Create post record in Post table.
     * @param post
     * @return
     */
    public boolean createPost (Post post) {
        boolean returnValue = false;

        SQLiteDatabase db = kdb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(POST_COL2 , post.getTitle());
        contentValues.put(POST_COL3 , post.getContent());
        contentValues.put(POST_COL4 , post.getPhotoLink());
        contentValues.put(POST_COL5 , post.getWriteDate());
        contentValues.put(POST_COL6 , post.getViewCount());
        contentValues.put(POST_COL7 , post.getKidId());

        long result = db.insert(POST_TABLE_NAME , null , contentValues);

        if(result == -1) {
            returnValue = false;
        } else {
            returnValue = true;
        }

        return returnValue;
    }


    /**
     * Retrieve data from Post table by filtering kidId
     * @param kidId
     * @return
     */
    public ArrayList<Post> getPostList(int kidId) {
        ArrayList<Post> postArrayList = null;
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery(" SELECT "  + POST_COL1 + ", " + POST_COL2 + ", " + POST_COL3 + ", "
                                    + POST_COL4 + ", " + POST_COL5 + ", "+ POST_COL6 + ", " + POST_COL7 + " FROM "
                                    + POST_TABLE_NAME + " WHERE " + POST_COL7 + "= ?" , new String[]{String.valueOf(kidId)});

            if(cursor != null) {
                postArrayList = new ArrayList<Post>();

                if(cursor.moveToFirst()) {
                    do {
                        Post post = new Post();
                        post.setId(cursor.getInt(cursor.getColumnIndex(POST_COL1)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(POST_COL2)));
                        post.setContent(cursor.getString(cursor.getColumnIndex(POST_COL3)));
                        post.setPhotoLink(cursor.getString(cursor.getColumnIndex(POST_COL4)));
                        post.setWriteDate(cursor.getString(cursor.getColumnIndex(POST_COL5)));
                        post.setViewCount(cursor.getInt(cursor.getColumnIndex(POST_COL6)));
                        post.setKidId(cursor.getInt(cursor.getColumnIndex(POST_COL7)));

                        postArrayList.add(post);
                    } while(cursor.moveToNext());

                }

                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            cursor.close();
            db.close();
        }

        return postArrayList;
    }


}
