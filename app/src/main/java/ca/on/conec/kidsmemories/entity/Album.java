package ca.on.conec.kidsmemories.entity;
 // Album data match and get data
public class Album {

    private String imgPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Album(String imgPath) {
        this.imgPath = imgPath;
    }
}
