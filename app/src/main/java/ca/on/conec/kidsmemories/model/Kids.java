package ca.on.conec.kidsmemories.model;

/**
 * Kid Model Class
 */
public class Kids {
    private int kidId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String nickName;
    private String provinceCode;
    private String photoURI;

    // Constructor without member variables
    public Kids() {
    }

    // Constructor with all member variables
    public Kids(int kidId, String firstName, String lastName, String dateOfBirth, String gender, String nickName, String provinceCode, String photoURI) {
        this.kidId = kidId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nickName = nickName;
        this.provinceCode = provinceCode;
        this.photoURI = photoURI;
    }

    // Setters and Getters

    public int getKidId() {
        return kidId;
    }

    public void setKidId(int kidId) {
        this.kidId = kidId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }
}
