package ca.on.conec.kidsmemories.entity;

public class Album {
    /*
    private String image1;
    private  String image2;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public Album() {
    }

    */
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
