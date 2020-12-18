/*
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
import android.text.Html;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.on.conec.kidsmemories.entity.Album;
import ca.on.conec.kidsmemories.entity.Photo;
import ca.on.conec.kidsmemories.entity.Post;


public class PostDAO extends KidsMemoriesDBHelper{

    public static final String POST_TABLE_NAME = "Post";        // table name

    // DB Schema
    public static final String POST_COL1 = "postId";
    public static final String POST_COL2 = "title";
    public static final String POST_COL3 = "content";
    public static final String POST_COL4 = "photo_link";
    public static final String POST_COL5 = "write_date";
    public static final String POST_COL6 = "view_count";
    public static final String POST_COL7 = "kidId";

    // Create table statement
    public static final String CREATE_POST_TABLE = "CREATE TABLE " + POST_TABLE_NAME + "("
                                                + POST_COL1  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + POST_COL2  + " TEXT, "
                                                + POST_COL3  + " TEXT, "
                                                + POST_COL4  + " TEXT, "
                                                + POST_COL5  + " TEXT, "
                                                + POST_COL6  + " INTEGER, "
                                                + POST_COL7  + " INTEGER,  "
                                                + " FOREIGN KEY(" + POST_COL7 + ") REFERENCES "+ KidsDAO.KID_TABLE_NAME+ "(" + KidsDAO.KID_COL1+ ")"
                                                + ")";

    // Drop table statement
    public static final String DROP_POST_TABLE = "DROP TABLE IF EXISTS " + POST_TABLE_NAME;

    KidsMemoriesDBHelper kdb;

    /**
     * constructor DAO
     * assign DBHelper
     * @param context
     */
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

        try {
            ContentValues contentValues = new ContentValues();
            String htmlContent = post.getContent();

            Pattern imgPattern = Pattern.compile("(?i)< *[img][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
            Matcher captured = imgPattern.matcher(htmlContent);
            int imgCnt = 0;

            String base64Img = "";
            while (captured.find()) {
                base64Img = captured.group(1);  // Only first image captured
                imgCnt++;
                if (imgCnt == 1) {
                    //  do not need next image
                    break;
                }
            }

            contentValues.put(POST_COL2, post.getTitle());
            contentValues.put(POST_COL3, post.getContent());
            contentValues.put(POST_COL4, base64Img);
            contentValues.put(POST_COL5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            contentValues.put(POST_COL6, post.getViewCount());
            contentValues.put(POST_COL7, post.getKidId());

            long result = db.insert(POST_TABLE_NAME, null, contentValues);

            if (result == -1) {
                returnValue = false;
            } else {
                returnValue = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(db != null) {
                db.close();
            }
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
        Cursor cursor;

        try {

            cursor = db.rawQuery(" SELECT "  + POST_COL1 + ", " + POST_COL2 + ", " + POST_COL3 + ", "
                                    + POST_COL4 + ", strftime('%Y-%m-%d' , " + POST_COL5 + ") as write_date, "+ POST_COL6 + ", " + POST_COL7 + " FROM "
                                    + POST_TABLE_NAME + " WHERE " + POST_COL7 + "= ?" , new String[]{String.valueOf(kidId)});

            if(cursor != null) {
                postArrayList = new ArrayList<Post>();

                if(cursor.moveToFirst()) {
                    do {
                        Post post = new Post();


                        post.setId(cursor.getInt(cursor.getColumnIndex(POST_COL1)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(POST_COL2)));
                        post.setContent(Html.fromHtml(cursor.getString(cursor.getColumnIndex(POST_COL3))).toString().replace('\n', (char) 32).replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim());
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
            if(db != null) {
                db.close();
            }
        }

        return postArrayList;
    }


    /**
     * Retrive detail info for kids
     * @param postId
     * @param kidId
     * @return
     */
    public Post getPostView(int postId , int kidId) {
        Post post = null;
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery(" SELECT "  + POST_COL1 + ", " + POST_COL2 + ", " + POST_COL3 + ", "
                    + POST_COL4 + ", strftime('%Y-%m-%d' , " + POST_COL5 + ") as write_date, "+ POST_COL6 + ", " + POST_COL7 + " FROM "
                    + POST_TABLE_NAME + " WHERE " + POST_COL1 + "= ? AND " + POST_COL7 + "= ?"  , new String[]{String.valueOf(postId), String.valueOf(kidId)});


            if(cursor != null) {
                if(cursor.moveToFirst()) {

                        post = new Post();

                        post.setId(cursor.getInt(cursor.getColumnIndex(POST_COL1)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(POST_COL2)));
                        post.setContent(cursor.getString(cursor.getColumnIndex(POST_COL3)));
                        post.setPhotoLink(cursor.getString(cursor.getColumnIndex(POST_COL4)));
                        post.setWriteDate(cursor.getString(cursor.getColumnIndex(POST_COL5)));
                        post.setViewCount(cursor.getInt(cursor.getColumnIndex(POST_COL6)));
                        post.setKidId(cursor.getInt(cursor.getColumnIndex(POST_COL7)));

                }

                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(db != null) {
                db.close();
            }
        }

        return post;
    }

    /**
     * delete post
     * @param post
     * @return
     */
    public boolean deletePost(Post post) {

        boolean returnValue = false;
        SQLiteDatabase db = kdb.getWritableDatabase();


        try {

            int postId = post.getId();

            long result = db.delete(POST_TABLE_NAME , POST_COL1 + "= ?", new String[] {String.valueOf(postId)});

            if(result == -1) {
                returnValue = false;
            } else {
                returnValue = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(db != null) {
                db.close();
            }
        }

        return returnValue;
    }

    /**
     * update post
     * @param post
     * @return
     */
    public boolean modifyPost(Post post) {

        boolean returnValue = false;
        SQLiteDatabase db = kdb.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();

            String htmlContent = post.getContent();

            Pattern imgPattern = Pattern.compile("(?i)< *[img][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
            Matcher captured = imgPattern.matcher(htmlContent);
            int imgCnt = 0;

            String base64Img = "";
            while (captured.find()) {
                base64Img = captured.group(1);
                imgCnt++;
                if (imgCnt == 1) {
                    break;
                }
            }


            int postId = post.getId();
            int kidId = post.getKidId();

            contentValues.put(POST_COL2, post.getTitle());
            contentValues.put(POST_COL3, post.getContent());
            contentValues.put(POST_COL4, base64Img);


            long result = db.update(POST_TABLE_NAME, contentValues, POST_COL1 + "= ? and " + POST_COL7 + "= ?", new String[]{String.valueOf(postId), String.valueOf(kidId)});

            if (result == -1) {
                returnValue = false;
            } else {
                returnValue = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(db != null) {
                db.close();
            }
        }

        return returnValue;
    }

    public ArrayList<Album> getPostImgList(int kidId, String startDate , String endDate) {
        ArrayList<Album> imgArrayList = null;
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor cursor;

        try {

            Log.d("info" , ">>" + startDate);
            Log.d("info" , ">>" + endDate);
            cursor = db.rawQuery(" SELECT "  + POST_COL1 + ", " + POST_COL2 + ", " + POST_COL3 + ", "
                    + POST_COL4 + ", strftime('%Y-%m-%d' , " + POST_COL5 + ") as write_date, "+ POST_COL6 + ", " + POST_COL7 + " FROM "
                    + POST_TABLE_NAME + " WHERE " + POST_COL7 + "= ? AND strftime('%Y-%m-%d' , " + POST_COL5 + ") BETWEEN ? AND ? "  , new String[]{String.valueOf(kidId) , String.valueOf(startDate) , String.valueOf(endDate)});

            if(cursor != null) {

                if(cursor.moveToFirst()) {
                    imgArrayList = new ArrayList<Album>();
                    do {

                        String originContent = cursor.getString(cursor.getColumnIndex(POST_COL3));


                        Pattern imgPattern = Pattern.compile("(?i)< *[img][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
                        Matcher captured = imgPattern.matcher(originContent);



                        while (captured.find()) {
                            String imgPath = captured.group(1);
                            Album album = new Album(imgPath);

                            imgArrayList.add(album);
                        }
                    } while(cursor.moveToNext());

                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(db != null) {
                db.close();
            }
        }

        return imgArrayList;
    }

}
