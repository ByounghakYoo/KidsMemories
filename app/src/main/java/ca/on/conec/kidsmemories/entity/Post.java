/**
 *  FileName : Post.java
 *  Purpose : Post(Writing) Entity
 *  Revision History :
 *          Creted by Byounghak Yoo (Henry) 2020 12.10
 */
package ca.on.conec.kidsmemories.entity;


public class Post {
    private int id;     // Post Id AutoGenerate.
    private String title;   // Post Title
    private String content;         // Post Content / form of HTML
    private String photoLink;       // Photo Link // Just Link Url;
    private String writeDate;       // SYS DATE
    private int viewCount;          // View Count
    private int kidId;              // kid Id for foreign key

    /**
     * Basic Constroctor
     */
    public Post() {
    }

    /**
     * When the create post record, use this constructor.
     * Id is Automatically generated, so this point, do not need Id.
     * @param title
     * @param content
     * @param photoLink
     * @param writeDate
     * @param viewCount
     * @param kidId
     */
    public Post(String title , String content , String photoLink, String writeDate , int viewCount, int kidId) {
        this.title = title;
        this.content = content;
        this.photoLink = photoLink;
        this.writeDate = writeDate;
        this.viewCount = viewCount;
        this.kidId = kidId;
    }


    /**
     * When the retrieve post record, use this constructor.
     * WHen retrieve from database, all field information need.
     * @param id
     * @param title
     * @param content
     * @param photoLink
     * @param writeDate
     * @param viewCount
     * @param kidId
     */
    public Post(int id, String title , String content , String photoLink, String writeDate , int viewCount, int kidId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.photoLink = photoLink;
        this.writeDate = writeDate;
        this.viewCount = viewCount;
        this.kidId = kidId;
    }

    /**
     * Getter ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter content
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter content
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter write date
     * @return
     */
    public String getWriteDate() {
        return writeDate;
    }

    /**
     * Setter write date
     * @param writeDate
     */
    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    /**
     * Getter Photo Link
     * @return
     */
    public String getPhotoLink() {
        return photoLink;
    }

    /**
     * Setter Photo Link
     * @param photoLink
     */
    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    /**
     * Getter View Count
     * @return
     */
    public int getViewCount() {
        return viewCount;
    }

    /**
     * Setter View Count
     * @param viewCount
     */
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }


    /**
     * Getter Kids ID
     * @return
     */
    public int getKidId() {
        return kidId;
    }

    /**
     * Setter Kid ID
     * @param kidId
     */
    public void setKidId(int kidId) {
        this.kidId = kidId;
    }
}
