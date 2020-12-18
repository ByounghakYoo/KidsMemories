/*
   FileName : Post.java
   Purpose : Post(Writing) Entity
   Revision History :
           Creted by Byounghak Yoo (Henry) 2020 12.10
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
     * @param title title
     * @param content content
     * @param photoLink photo address
     * @param writeDate write date
     * @param viewCount view Count
     * @param kidId Kid ID
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
     * @param id post Id
     * @param title title
     * @param content contetn
     * @param photoLink photo address
     * @param writeDate write date
     * @param viewCount view count
     * @param kidId kid Id
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
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter ID
     * @param id post id value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter title
     * @param title title value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter content
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter content
     * @param content content value
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter write date
     * @return write date
     */
    public String getWriteDate() {
        return writeDate;
    }

    /**
     * Setter write date
     * @param writeDate write date value
     */
    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    /**
     * Getter Photo Link
     * @return  photo address
     */
    public String getPhotoLink() {
        return photoLink;
    }

    /**
     * Setter Photo Link
     * @param photoLink photo address value
     */
    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    /**
     * Getter View Count
     * @return  view count
     */
    public int getViewCount() {
        return viewCount;
    }

    /**
     * Setter View Count
     * @param viewCount view count value
     */
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }


    /**
     * Getter Kids ID
     * @return  kid id
     */
    public int getKidId() {
        return kidId;
    }

    /**
     * Setter Kid ID
     * @param kidId kid id value
     */
    public void setKidId(int kidId) {
        this.kidId = kidId;
    }

}
